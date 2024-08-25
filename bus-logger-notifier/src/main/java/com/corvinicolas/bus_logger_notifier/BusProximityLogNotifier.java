package com.corvinicolas.bus_logger_notifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@Slf4j
public class BusProximityLogNotifier {

    @KafkaListener(topics = "busProximity")
    public void busProximityListener(@Payload BusProximityModel busProximity) {
        LOGGER.info("Next bus will come at {}", busProximity.getTimeInSeconds());
    }
}
