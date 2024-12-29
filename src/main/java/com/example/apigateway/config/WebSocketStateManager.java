package com.example.apigateway.config;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class WebSocketStateManager {

    private final Set<String> upInstances = new ConcurrentSkipListSet<>();
    private final Set<String> downInstances = new ConcurrentSkipListSet<>();

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
        return  upInstances.iterator().next();
    }
}

