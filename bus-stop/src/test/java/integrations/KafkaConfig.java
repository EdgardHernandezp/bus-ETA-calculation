package integrations;

import java.util.HashMap;
import java.util.Map;

import com.dreamseeker.bus.stop.domains.BusEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.Message;

@TestConfiguration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<String, BusEvent> kafkaTemplate(ProducerFactory<String, BusEvent> producerFactory) {
        KafkaTemplate<String, BusEvent> template = new KafkaTemplate<>(producerFactory);
        template.setDefaultTopic("bus-location-queue");
        return template;
    }

    @Bean
    public ProducerFactory<String, BusEvent> producerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServer) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }


    @Bean
    public MessageVerifierSender<Message<?>> messageVerifierSender(KafkaTemplate<String, BusEvent> kafkaTemplate) {
        return new KafkaMessageVerifierSender(kafkaTemplate);
    }
}
