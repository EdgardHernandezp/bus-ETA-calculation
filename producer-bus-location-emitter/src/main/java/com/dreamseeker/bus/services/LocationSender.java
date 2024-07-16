package com.dreamseeker.bus.services;

import java.util.Map;

import com.dreamseeker.bus.domain.BusEvent;
import com.dreamseeker.bus.domain.Location;
import com.dreamseeker.bus.messaging.MessageBrokerTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static org.springframework.amqp.support.AmqpHeaders.CONTENT_TYPE;

@Service
@Slf4j
public class LocationSender {
    private final GeolocationRetriever geolocationRetriever;
    private final MessageBrokerTemplate template;
    private final String busNumber;
    private final String routeNumber;

    public LocationSender(GeolocationRetriever geolocationRetriever,
                          MessageBrokerTemplate template,
                          @Value("${bus-info.number}") String busNumber,
                          @Value("${bus-info.route.number}") String routeNumber) {
        this.geolocationRetriever = geolocationRetriever;
        this.template = template;
        this.busNumber = busNumber;
        this.routeNumber = routeNumber;
    }

    @Scheduled(fixedRateString = "${location.send.rate}", initialDelayString = "${location.send.initial.delay}")
    public void send() {
        Location location = geolocationRetriever.getCurrentLocation();
        Map<String, Object> headers = Map.of(CONTENT_TYPE, "application/json");
        template.publish(new BusEvent(busNumber, routeNumber, location), headers);
    }
}
