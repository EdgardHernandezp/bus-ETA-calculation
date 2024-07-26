package integrations;

import java.util.List;

import com.dreamseeker.bus.stop.ArrivalsRepo;
import com.dreamseeker.bus.stop.BusStopAppApplication;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {BusStopAppApplication.class, RabbitMQConfig.class})
@AutoConfigureStubRunner(ids = "com.dreamseeker:bus", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@Testcontainers
public class LocationReceptorIntegrationTest {
    private static final DockerImageName RABBITMQ_IMAGE = DockerImageName.parse("rabbitmq:3.12.14-alpine");

    @Container
    static RabbitMQContainer rabbit = new RabbitMQContainer(RABBITMQ_IMAGE);
    @Autowired
    private StubTrigger stubTrigger;
    @Autowired
    private ArrivalsRepo arrivalsRepo;

    @DynamicPropertySource
    static void rabbitProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
    }

    @Test
    void whenMessageReceiveAddToArrivalRepo() {
        stubTrigger.trigger("message-sent-ok");

        Awaitility.await().untilAsserted(() -> {
                    List<ArrivalsRepo.Record> arrivals = arrivalsRepo.get(5);
                    assertThat(arrivals).hasSize(1);
                }
        );

    }
}
