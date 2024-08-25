package com.corvinicolas.bus_tracker.application.service.impl;

import com.corvinicolas.bus_tracker.application.service.BusProximityNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggerBusProximityNotifier implements BusProximityNotifier {

    @Override
    public void notifyProximity() {
        LOGGER.info("Bus arriving");
    }
}
