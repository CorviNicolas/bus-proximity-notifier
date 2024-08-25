package com.corvinicolas.bus_tracker.application.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
