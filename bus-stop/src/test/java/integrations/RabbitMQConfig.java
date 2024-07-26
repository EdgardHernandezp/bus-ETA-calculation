package integrations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

@TestConfiguration
class RabbitMQConfig {

    @Bean
    public Queue queue() {
        return new Queue("bus-location-queue");
    }

    @Bean()
    public DirectExchange exchange() {
        return new DirectExchange("bus-location-exchange");
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with("routing-key");
    }

    @Bean
    public MessageVerifierSender<Message<?>> messageVerifierSender(RabbitTemplate rabbitTemplate) {
        return new RabbitMessageVerifierSender(rabbitTemplate);

    }
}
