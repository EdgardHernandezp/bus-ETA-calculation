package com.dreamseeker.bus.domain;

public record BusEvent(String busNumber, String routeNumber, Location location) {
}
