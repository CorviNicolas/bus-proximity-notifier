package com.corvinicolas.bus_logger_notifier;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BusProximityModel implements Serializable {
    private String routeId;
    private String line;
    private String textCa;
    private int timeInSeconds;
    private int timeInMinutes;
}