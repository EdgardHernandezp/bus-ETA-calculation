package com.dreamseeker.bus.stop;

import com.dreamseeker.bus.stop.domains.BusEvent;
import com.dreamseeker.bus.stop.domains.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LocationReceptor {
    private final ArrivalTimeEstimator arrivalTimeEstimator;
    private final Location busStopLocation;
    private final ArrivalsRepo repo;

    public LocationReceptor(ArrivalTimeEstimator arrivalTimeEstimator,
                            @Value("${bus.stop.altitude}") double busStopAltitude,
                            @Value("${bus.stop.longitude}") double busStopLongitude,
                            ArrivalsRepo repo) {
        this.arrivalTimeEstimator = arrivalTimeEstimator;
        this.repo = repo;
        this.busStopLocation = new Location(busStopAltitude, busStopLongitude);
    }

    @RabbitListener(queues = {"${rabbitmq.queues.bus-stops}"})
    public void receiveLocation(BusEvent busEvent) {
        //Validate if busEvent already passed according to location, skip if true
        int eta = arrivalTimeEstimator.estimate(busStopLocation, busEvent.location());
        log.info("BusNumber {} - RouteNumber {} - ETA {} min", busEvent.busNumber(), busEvent.routeNumber(), eta);
        repo.add(busEvent.busNumber(), busEvent.routeNumber(), eta);
    }

}
