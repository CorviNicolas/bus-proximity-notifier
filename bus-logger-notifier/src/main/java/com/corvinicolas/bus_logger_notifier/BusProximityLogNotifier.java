package com.corvinicolas.bus_logger_notifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class BusProximityLogNotifier {

    @KafkaListener(topics = "busProximity", containerFactory = "busProximityModelListenerContainerFactory")
    public void busProximityListener(@Payload BusProximityModel busProximity) {
        LOGGER.info("Next bus will come in {} seconds", busProximity.getTimeInSeconds());
    }
}
