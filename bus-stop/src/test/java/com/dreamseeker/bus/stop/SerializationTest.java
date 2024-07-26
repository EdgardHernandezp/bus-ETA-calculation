package com.dreamseeker.bus.stop;

import java.io.IOException;
import java.io.InputStream;

import com.dreamseeker.bus.stop.domains.BusEvent;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = {BusStopAppApplication.class})
public class SerializationTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MessageConverter messageConverter;

    @Test
    void name() throws IOException {
        InputStream resourceAsStream = SerializationTest.class.getResourceAsStream("/event.json");
        BusEvent busEvent = objectMapper.readValue(resourceAsStream, BusEvent.class);
        System.out.println(busEvent.toString());
    }

    @Test
    void testConverter() throws IOException {
        InputStream resourceAsStream = SerializationTest.class.getResourceAsStream("/event.json");
        MessageProperties properties = new MessageProperties();
        properties.setInferredArgumentType(BusEvent.class);
        properties.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        Message message = MessageBuilder
                .withBody(resourceAsStream.readAllBytes())
                .copyProperties(properties)
                .build();
        BusEvent result = (BusEvent) messageConverter.fromMessage(message);
        System.out.println(result);
    }
}
