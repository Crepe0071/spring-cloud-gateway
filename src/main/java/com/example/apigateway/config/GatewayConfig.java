package com.example.apigateway.config;

import static com.example.apigateway.EnumCode.WAS_ROUTE;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(WAS_ROUTE.getValue(),
                        r -> r.path("/tpms/**")
                              .uri(String.format("lb://%s", WAS_ROUTE.getValue())))
                .build();
    }

}

