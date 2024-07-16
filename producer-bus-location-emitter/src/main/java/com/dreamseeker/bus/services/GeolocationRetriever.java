package com.dreamseeker.bus.services;

import com.dreamseeker.bus.domain.Location;

public interface GeolocationRetriever {
    Location getCurrentLocation();
}
