package com.dreamseeker.bus.stop.domains;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BusEvent(@JsonProperty("busNumber") String busNumber,
                       @JsonProperty("routeNumber") String routeNumber,
                       @JsonProperty("location") Location location) {
}
