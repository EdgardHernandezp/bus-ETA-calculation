package com.dreamseeker.bus.integrations;

import com.dreamseeker.bus.BusApplication;
import com.dreamseeker.bus.integrations.configs.TestConfig;
import com.dreamseeker.bus.services.LocationSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {BusApplication.class, TestConfig.class})
@AutoConfigureMessageVerifier
@Testcontainers
public abstract class ContractBase {
    @Autowired
    LocationSender locationSender;

    private static final DockerImageName RABBITMQ_IMAGE = DockerImageName.parse("rabbitmq:3.12.14-alpine");

    @Container
    static RabbitMQContainer rabbit = new RabbitMQContainer(RABBITMQ_IMAGE);

    @DynamicPropertySource
    static void rabbitProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
    }

    protected void triggerLocationSending() throws JsonProcessingException {
        locationSender.send();
    }
}