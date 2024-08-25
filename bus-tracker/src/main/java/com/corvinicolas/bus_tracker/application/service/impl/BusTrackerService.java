package com.corvinicolas.bus_tracker.application.service.impl;

import com.corvinicolas.bus_tracker.providers.tmb_app.client.TmbAppClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
@EnableScheduling
public class BusTrackerService implements InitializingBean {

    private final TmbAppClient tmbAppClient;
    private final TaskScheduler scheduler;
    private final int notifyBeforeSeconds;
    @Value("${scheduler.error.retry-time-duration:5s}")
    private Duration retryTime;
    private final List<ScheduledFuture<?>> scheduledFutures = new ArrayList<>();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private String busTopic;


    public BusTrackerService(
            TmbAppClient tmbAppClient,
            TaskScheduler scheduler,
            @Value("${bus.notify-before-seconds:160}") int notifyBeforeSeconds,
            @Value("${scheduler.error.retry-time-duration:5s}") Duration retryTime,
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${bus.kafka.topic-name}") String busTopic) {
        if(notifyBeforeSeconds < 10 || retryTime.isNegative() || retryTime.isZero()) {
            throw new IllegalStateException("Parameter notifyBeforeSeconds cant be less than 10 and retry time must be greater than zero. Configured values are notifyBeforeSeconds: "
                    + notifyBeforeSeconds + " and retryTime: "
                    + retryTime);
        }

        this.tmbAppClient = tmbAppClient;
        this.scheduler = scheduler;
        this.notifyBeforeSeconds = notifyBeforeSeconds;
        this.retryTime = retryTime;
        this.kafkaTemplate = kafkaTemplate;
        this.busTopic = busTopic;
    }

    public void configureTasks() {
        if(!scheduledFutures.isEmpty()){
            scheduledFutures.forEach(scheduledFuture -> {
                LOGGER.warn("Task: {} will be cancelled if not running", scheduledFuture);
                scheduledFuture.cancel(false);
            });
        }
        scheduledFutures.clear();
        LOGGER.info("test");
        try{

            tmbAppClient.getStopPrevision().subscribe(busProximityModel -> {
                LOGGER.info("Next arrive will be in {} seconds", busProximityModel.getTimeInSeconds());
                int timeInSeconds = busProximityModel.getTimeInSeconds();
                if(timeInSeconds <= this.notifyBeforeSeconds) {
                    LOGGER.info("Sending an event since next bus will come in the next {} seconds", timeInSeconds);
                    kafkaTemplate.send(busTopic, busProximityModel.getLine(), busProximityModel);
                    // trigger event notification and schedule new trigger
                    Instant nextTrigger = Instant.now().plusSeconds(timeInSeconds + 10);
                    scheduleNextProximityCheckAt(nextTrigger);
                } else {
                    Instant nextTrigger = Instant.now().plusSeconds(timeInSeconds - notifyBeforeSeconds + 5);
                    LOGGER.info("Dont emit an event, re-schedule next proximity check at {}", nextTrigger);
                    scheduleNextProximityCheckAt(nextTrigger);
                }
            });
        } catch (Exception e) {
            LOGGER.error("Error", e);
            //schedule new trigger to retry
            scheduleNextProximityCheckAt(Instant.now().plus(retryTime));
        }

    }

    @Override
    public void afterPropertiesSet() {
        executor.execute(this::configureTasks);
    }

    private void scheduleNextProximityCheckAt(Instant nextTrigger){
        LOGGER.info("Scheduling next proximity check at: {}", nextTrigger);
        ScheduledFuture<?> task = scheduler.schedule(() -> executor.execute(this::configureTasks), nextTrigger);
        scheduledFutures.add(task);
    }

}
