package com.dreamseeker.bus.integrations;

import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

@AllArgsConstructor
@Slf4j
public class RabbitMessageVerifierReceiver implements MessageVerifierReceiver<Message<?>> {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public Message<?> receive(String destination, long timeout, TimeUnit timeUnit, @Nullable YamlContract contract) {
        org.springframework.amqp.core.Message message = rabbitTemplate.receive(destination, timeout);
        log.info("Message retrieved: {}", message);
        return new GenericMessage<>(message.getBody(), message.getMessageProperties().getHeaders());
    }

    @Override
    public Message<?> receive(String destination, YamlContract contract) {
        return receive(destination, 60000, TimeUnit.MILLISECONDS, contract);
    }
}
