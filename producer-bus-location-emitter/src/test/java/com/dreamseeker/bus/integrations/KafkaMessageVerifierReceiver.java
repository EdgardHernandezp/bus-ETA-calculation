package com.dreamseeker.bus.integrations;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.dreamseeker.bus.domain.BusEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.header.Headers;
import org.jetbrains.annotations.Nullable;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

@AllArgsConstructor
@Slf4j
public class KafkaMessageVerifierReceiver implements MessageVerifierReceiver<Message<?>> {
    private final KafkaConsumer<String, BusEvent> consumer;

    @Override
    public Message<?> receive(String destination, long timeout, TimeUnit timeUnit, @Nullable YamlContract contract) {
        ConsumerRecords<String, BusEvent> records = consumer.poll(Duration.of(timeout, ChronoUnit.MILLIS));
        Message<?> message = null;
        for (ConsumerRecord<String, BusEvent> consumerRecord : records) {
            log.info("Message retrieved: {}", consumerRecord.value());
            message = new GenericMessage<>(consumerRecord.value(), convertHeadersType(consumerRecord.headers()));
        }
        return message;
    }

    private static MessageHeaders convertHeadersType(Headers headers) {
        Map<String, Object> headersMap = new HashMap<>();
        headers.forEach(header -> headersMap.put(header.key(), new String(header.value())));
        return new MessageHeaders(headersMap);
    }

    @Override
    public Message<?> receive(String destination, YamlContract contract) {
        return receive(destination, 60000, TimeUnit.MILLISECONDS, contract);
    }
}
