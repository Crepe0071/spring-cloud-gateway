package com.example.apigateway.config;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WasStateManager {

    private final Set<String> upInstances = new ConcurrentSkipListSet<>();
    private final Set<String> downInstances = new ConcurrentSkipListSet<>();
    private final Environment env;

    @PostConstruct
    public void init() {
        for (String activeProfile : env.getActiveProfiles()) {
            if ("local".equals(activeProfile)) {
                upInstances.add("localhost:8082");
            } else if ("dev".equals(activeProfile)) {
                upInstances.add("172.31.19.11:8080");
            }
        }
    }

    public void markUp(String wasAddress) {
        downInstances.remove(wasAddress);
        upInstances.add(wasAddress);
    }

    public void markDown(String wasAddress) {
        upInstances.remove(wasAddress);
        downInstances.add(wasAddress);
    }

    public Set<String> getUpInstances() {
        return Collections.unmodifiableSet(upInstances);
    }

    public Set<String> getDownInstances() {
        return Collections.unmodifiableSet(downInstances);
    }

    public String getRandomUpInstance() {
        if (upInstances.isEmpty()) {
            throw new IllegalStateException("No up instances available");
        }
        return upInstances.iterator().next();
    }
}

