package com.dreamseeker.bus.integrations.configs;

import java.util.List;
import java.util.Properties;

import com.dreamseeker.bus.domain.BusEvent;
import com.dreamseeker.bus.integrations.KafkaMessageVerifierReceiver;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessage;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessaging;
import org.springframework.cloud.contract.verifier.messaging.noop.NoOpStubMessages;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.Message;

@TestConfiguration
public class TestConfig {

    @Bean
    ContractVerifierMessaging<Message<?>> customContractVerifierMessaging(MessageVerifierReceiver<Message<?>> messageVerifier) {
        return new ContractVerifierMessaging<>(new NoOpStubMessages<>(), messageVerifier) {
            @Override
            protected ContractVerifierMessage convert(Message message) {
                if (message == null)
                    return null;

                return new ContractVerifierMessage(message.getPayload(), message.getHeaders());
            }
        };
    }

    @Bean
    KafkaMessageVerifierReceiver kafkaMessageVerifierReceiver(KafkaConsumer<String, BusEvent> kafkaConsumer) {
        return new KafkaMessageVerifierReceiver(kafkaConsumer);
    }

    @Bean
    public KafkaConsumer<String, BusEvent> kafkaConsumer(@Value("${message.broker.topic}") String topicName) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "1");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        KafkaConsumer<String, BusEvent> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of(topicName));
        return consumer;
    }


}
