package com.dreamseeker.bus.integrations.configs;

import com.dreamseeker.bus.integrations.RabbitMessageVerifierReceiver;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessage;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessaging;
import org.springframework.cloud.contract.verifier.messaging.noop.NoOpStubMessages;
import org.springframework.context.annotation.Bean;
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
    RabbitMessageVerifierReceiver rabbitMessageVerifierReceiver(RabbitTemplate rabbitTemplate) {
        return new RabbitMessageVerifierReceiver(rabbitTemplate);
    }
}
