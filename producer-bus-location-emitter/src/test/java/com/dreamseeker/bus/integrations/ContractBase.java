package com.dreamseeker.bus.integrations;

import com.dreamseeker.bus.BusApplication;
import com.dreamseeker.bus.services.LocationSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@AutoConfigureMessageVerifier
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration
public abstract class ContractBase {
    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka"));
    @Autowired
    private LocationSender locationSender;

    protected void triggerLocationSending() {
        locationSender.send();
    }

    @ComponentScan(
            basePackages = {"com.dreamseeker.bus"},
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = BusApplication.class)
    )
    @Configuration
    static class ContextConfig {

    }
}
