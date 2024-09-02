package com.dreamseeker.bus.integrations;

import com.dreamseeker.bus.BusApplication;
import com.dreamseeker.bus.integrations.configs.TestConfig;
import com.dreamseeker.bus.services.LocationSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = {BusApplication.class, TestConfig.class})
@AutoConfigureMessageVerifier
@Testcontainers
public abstract class ContractBase {
    @Autowired
    LocationSender locationSender;

    @Container
    private static final RabbitMQContainer rabbit = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.12.14-alpine"));

    @DynamicPropertySource
    static void rabbitProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
    }

    protected void triggerLocationSending() {
        locationSender.send();
    }
}