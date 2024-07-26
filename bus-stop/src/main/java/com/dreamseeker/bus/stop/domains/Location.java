package com.dreamseeker.bus.stop.domains;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Location(@JsonProperty("altitude") double altitude, @JsonProperty("longitude") double longitude) {
}
