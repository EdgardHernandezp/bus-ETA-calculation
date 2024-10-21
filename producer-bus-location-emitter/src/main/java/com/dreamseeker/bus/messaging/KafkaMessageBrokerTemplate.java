package com.dreamseeker.bus.messaging;

import java.util.Map;

import com.dreamseeker.bus.domain.BusEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class KafkaMessageBrokerTemplate implements MessageBrokerTemplate {

    private final KafkaTemplate<String, BusEvent> kafkaTemplate;

    @Override
    public void publish(BusEvent message, Map<String, Object> headers) {
        log.info("Sending message [{}] to topic [{}]", message, kafkaTemplate.getDefaultTopic());
        kafkaTemplate.send(new GenericMessage<>(message, headers));
    }
}
