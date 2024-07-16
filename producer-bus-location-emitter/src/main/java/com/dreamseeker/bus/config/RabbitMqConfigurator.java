package com.dreamseeker.bus.config;

import com.dreamseeker.bus.utils.EnvironmentWrapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@AllArgsConstructor
@Profile("default")
public class RabbitMqConfigurator {

    private final EnvironmentWrapper environment;

    @Bean
    public Queue queue() {
        String queue = environment.getProperty("rabbitmq.queues.bus-queue").orElseThrow();
        return new Queue(queue);
    }

    @Bean
    public DirectExchange exchange() {
        String exchange = environment.getProperty("rabbitmq.exchanges.bus-location").orElseThrow();
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding binding() {
        String routingKey = environment.getProperty("rabbitmq.routing-keys.main-key").orElseThrow();
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
