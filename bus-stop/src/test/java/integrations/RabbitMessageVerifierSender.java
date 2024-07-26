package integrations;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

@AllArgsConstructor
@Slf4j
public class RabbitMessageVerifierSender implements MessageVerifierSender<Message<?>> {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void send(Message<?> message, String destination, @Nullable YamlContract contract) {
        log.info("Sending a message to destination [{}]", destination);
        rabbitTemplate.send(destination, "routing-key", toMessage(message));
    }

    @Override
    public <T> void send(T payload, Map<String, Object> headers, String destination, @Nullable YamlContract contract) {
        send(org.springframework.messaging.support.MessageBuilder.withPayload(payload).copyHeaders(headers).build(), destination, contract);
    }

    private org.springframework.amqp.core.Message toMessage(Message<?> msg) {
        Object payload = msg.getPayload();
        MessageHeaders headers = msg.getHeaders();
        Map<String, Object> newHeaders = new HashMap<>(headers);
        MessageProperties messageProperties = new MessageProperties();
        newHeaders.forEach(messageProperties::setHeader);
        if (payload instanceof String) {
            String json = (String) payload;
            return MessageBuilder.withBody(json.getBytes(StandardCharsets.UTF_8)).andProperties(messageProperties).build();
        } else {
            throw new IllegalStateException("Payload is not a String");
        }
    }
}
