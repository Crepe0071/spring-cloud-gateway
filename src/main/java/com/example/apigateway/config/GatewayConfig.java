package com.example.apigateway.config;

import static com.example.apigateway.EnumCode.*;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

	private static final String DOMAIN = "tpms";

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				   .route(WAS_ROUTE.getValue(),
					   r -> r.path("/")
								.filters(f -> f.setPath(String.format("/%s", DOMAIN)))
								.uri(String.format("lb://%s", WAS_ROUTE.getValue())))
				   .route(WAS_ROUTE.getValue(),
					   r -> r.path(String.format("/%s/**", DOMAIN))
								.uri(String.format("lb://%s", WAS_ROUTE.getValue())))
				   .build();
	}

}

