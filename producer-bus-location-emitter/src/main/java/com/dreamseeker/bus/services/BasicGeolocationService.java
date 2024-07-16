package com.dreamseeker.bus.services;

import java.util.Random;

import com.dreamseeker.bus.domain.Location;
import org.springframework.stereotype.Service;

@Service
public class BasicGeolocationService implements GeolocationRetriever {
    @Override
    public Location getCurrentLocation() {
        Random random = new Random();
        return new Location(random.nextDouble(), random.nextDouble());
    }
}
