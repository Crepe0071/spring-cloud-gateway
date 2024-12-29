package com.example.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class CustomRoutingFilter implements GlobalFilter {

    private final WasStateManager wasStateManager;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return retryRequest(exchange, chain);
    }

    private Mono<Void> retryRequest(ServerWebExchange exchange,GatewayFilterChain chain) {
        return tryRequest(exchange, chain)
                .onErrorResume(e -> handleRetry(exchange, chain));
    }

    private Mono<Void> tryRequest(ServerWebExchange exchange,GatewayFilterChain chain) {
        String wasAddress = wasStateManager.getRandomUpInstance();

        URI uri = URI.create("http://" + wasAddress + exchange.getRequest().getPath());
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, uri);
        return chain.filter(exchange);
    }

    private Mono<Void> handleRetry(ServerWebExchange exchange,GatewayFilterChain chain) {
        String failedAddress = extractFailedAddress(exchange);
        if (failedAddress != null) {
            wasStateManager.markDown(failedAddress);
        }

        if (wasStateManager.getUpInstances().isEmpty()) {
            return Mono.error(new IllegalStateException("No available WAS instances after retries."));
        }

        return retryRequest(exchange, chain);
    }

    private String extractFailedAddress(ServerWebExchange exchange) {
        URI uri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        return uri != null ? uri.getHost() + ":" + uri.getPort() : null;
    }
}

