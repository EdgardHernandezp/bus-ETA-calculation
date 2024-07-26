package com.dreamseeker.bus.stop;

import java.util.Arrays;
import java.util.Random;

import com.dreamseeker.bus.stop.domains.BusEvent;
import com.dreamseeker.bus.stop.domains.Location;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationReceptorTest {

    @Mock
    private ArrivalTimeEstimator arrivalTimeEstimator;

    private ArrivalsRepo arrivalsRepo = new ArrivalsRepo();

    private double busStopAltitude = 40.7128;
    private double busStopLongitude = -74.0060;

    private LocationReceptor locationReceptor;

    @BeforeEach
    public void setup() {
        locationReceptor = new LocationReceptor(arrivalTimeEstimator, busStopAltitude, busStopLongitude, arrivalsRepo);
    }

    @Test
    void whenEventReceiveValidateRepoSizeGrows() {

        Location location = new Location(0.222d, 0.222d);
        BusEvent event1 = new BusEvent("1", "415", location);
        BusEvent event2 = new BusEvent("2", "520", location);
        BusEvent event3 = new BusEvent("1", "405", location);

        Random random = new Random();
        when(arrivalTimeEstimator.estimate(any(Location.class), any(Location.class))).thenReturn(random.nextInt(10));

        for (BusEvent busEvent : Arrays.asList(event1, event2, event3)) {
            locationReceptor.receiveLocation(busEvent);
        }


        Assertions.assertThat(arrivalsRepo.get(3)).hasSize(2);
    }
}