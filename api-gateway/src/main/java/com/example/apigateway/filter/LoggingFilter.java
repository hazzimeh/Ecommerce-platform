package com.example.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            System.out.println("Logging Filter: " + exchange.getRequest().getURI());
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                System.out.println("Logging Filter response: " + exchange.getResponse().getStatusCode());
            }));
        };
    }

    public static class Config {
        // Configuration properties if any
    }
}
