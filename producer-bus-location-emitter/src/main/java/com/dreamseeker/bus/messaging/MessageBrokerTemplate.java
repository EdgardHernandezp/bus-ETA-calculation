package com.dreamseeker.bus.messaging;

import java.util.Map;

import com.dreamseeker.bus.domain.BusEvent;

public interface MessageBrokerTemplate {

    void publish(BusEvent message, Map<String, Object> headers);
}
