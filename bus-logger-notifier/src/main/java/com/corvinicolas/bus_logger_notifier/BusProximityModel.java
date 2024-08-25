package com.corvinicolas.bus_logger_notifier;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusProximityModel {
    private String routeId;
    private String line;
    private String textCa;
    private int timeInSeconds;
    private int timeInMinutes;
}