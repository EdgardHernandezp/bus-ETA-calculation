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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {BusStopAppApplication.class, KafkaConfig.class})
@AutoConfigureStubRunner(ids = "com.dreamseeker:bus", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@Testcontainers
public class LocationReceptorIntegrationTest {

    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Autowired
    private StubTrigger stubTrigger;
    @Autowired
    private ArrivalsRepo arrivalsRepo;

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
