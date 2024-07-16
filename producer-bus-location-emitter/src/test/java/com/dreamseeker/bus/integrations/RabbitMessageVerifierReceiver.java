package com.dreamseeker.bus.integrations;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
import org.springframework.messaging.Message;

public class RabbitMessageVerifierReceiver implements MessageVerifierReceiver<Message<?>> {

    private static final Logger log = LoggerFactory.getLogger(RabbitMessageVerifierReceiver.class);

    private final LinkedBlockingQueue<Message<?>> queue = new LinkedBlockingQueue<>();

    @Override
    public Message<?> receive(String destination, long timeout, TimeUnit timeUnit, @Nullable YamlContract contract) {
        try {
            return queue.poll(timeout, timeUnit);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queues.bus-queue}")
    public void listen(Message<?> message) {
        log.info("Got a message! [{}]", message);
        queue.add(message);
    }

    @Override
    public Message<?> receive(String destination, YamlContract contract) {
        return receive(destination, 1, TimeUnit.SECONDS, contract);
    }
}
