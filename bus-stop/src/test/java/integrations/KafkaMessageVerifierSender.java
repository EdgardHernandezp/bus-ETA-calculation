package integrations;

import java.util.Map;

import com.dreamseeker.bus.stop.domains.BusEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;

@AllArgsConstructor
@Slf4j
public class KafkaMessageVerifierSender implements MessageVerifierSender<Message<?>> {
    private final KafkaTemplate<String, BusEvent> kafkaTemplate;

    @Override
    public void send(Message<?> message, String destination, @Nullable YamlContract contract) {
        log.info("Sending message [{}] to topic [{}]", message, kafkaTemplate.getDefaultTopic());
        kafkaTemplate.send(new GenericMessage<>(message, message.getHeaders()));
    }

    @Override
    public <T> void send(T payload, Map<String, Object> headers, String destination, @Nullable YamlContract contract) {
        Message<T> message = MessageBuilder.withPayload(payload).copyHeaders(headers).build();
        send(message, destination, contract);
    }
}
