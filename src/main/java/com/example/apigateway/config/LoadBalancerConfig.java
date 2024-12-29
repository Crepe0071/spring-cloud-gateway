package com.example.apigateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@LoadBalancerClients(value = {
        @LoadBalancerClient(name = "was-route", configuration = WasLoadBalancerConfig.class),
        @LoadBalancerClient(name = "websocket-route", configuration = LoadBalancerProperties.StickySession.class)
})
public class LoadBalancerConfig {




}
