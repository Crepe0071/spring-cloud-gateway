package com.example.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.apigateway.EnumCode.WAS_ROUTE;
import static com.example.apigateway.EnumCode.WEB_SOCKET_ROUTE;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(WAS_ROUTE.getValue(),
                r -> r.path("/api/**")
                                .uri(String.format("lb://%s", WAS_ROUTE.getValue())))
                .route(WEB_SOCKET_ROUTE.getValue(),
                r -> r.path("/ws/**")
                        .uri(String.format("lb://%s", WEB_SOCKET_ROUTE.getValue())))
                .build();
    }
}

