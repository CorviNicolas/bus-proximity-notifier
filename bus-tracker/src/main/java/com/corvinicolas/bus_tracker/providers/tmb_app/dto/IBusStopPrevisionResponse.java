package com.corvinicolas.bus_tracker.providers.tmb_app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IBusStopPrevisionResponse {

    private String status;
    private Data data;


    @Getter
    @Setter
    public static class Data {
        private List<IBus> ibus;
    }

    @Getter
    @Setter
    public static class IBus {
        private String destination;
        private String routeId;
        @JsonProperty(value = "text-ca")
        private String textCa;
        @JsonProperty(value = "t-in-s")
        private int timeInSeconds;
        @JsonProperty(value = "t-in-m")
        private int timeInMinutes;
    }
}
