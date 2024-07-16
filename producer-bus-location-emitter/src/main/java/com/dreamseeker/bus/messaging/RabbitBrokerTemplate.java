package com.dreamseeker.bus.messaging;

import java.util.Map;

import com.dreamseeker.bus.domain.BusEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("default")
public class RabbitBrokerTemplate implements MessageBrokerTemplate {
    private final RabbitMessagingTemplate rabbitMessagingTemplate;
    private final String exchange;
    private final String routingKey;

    public RabbitBrokerTemplate(RabbitMessagingTemplate rabbitMessagingTemplate,
                                @Value("${rabbitmq.exchanges.bus-location}") @NonNull String exchange,
                                @Value("${rabbitmq.routing-keys.main-key}") @NonNull String routingKey) {
        this.rabbitMessagingTemplate = rabbitMessagingTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public void publish(BusEvent location, Map<String, Object> headers) {
        log.info("Sending message [{}] to {} with {}", location.toString(), exchange, routingKey);
        rabbitMessagingTemplate.convertAndSend(exchange, routingKey, location, headers);
    }
}
