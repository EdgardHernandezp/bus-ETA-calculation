package com.dreamseeker.bus.utils;

import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class EnvironmentWrapper {
    private final Environment environment;

    public Optional<String> getProperty(String key) {
        return Optional.ofNullable(environment.getProperty(key));
    }
}
