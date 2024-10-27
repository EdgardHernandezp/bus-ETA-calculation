package com.dreamseeker.bus.stop;

import com.dreamseeker.bus.stop.domains.Location;
import org.springframework.stereotype.Component;

@Component
public class ArrivalTimeEstimator {
    private static final double EARTH_RADIUS = 6371;
    public static final int SPEED = 50;

    public int estimate(Location busStopLocation, Location busLocation) {
        double distance = calculateDistance(busStopLocation.altitude(), busStopLocation.longitude(), busLocation.altitude(), busLocation.longitude());
        double timeInHours = distance / SPEED;
        return (int) Math.round(timeInHours * 60);
    }

    double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        double distance = Math.sqrt(x * x + y * y) * EARTH_RADIUS;

        return distance;
    }
}
