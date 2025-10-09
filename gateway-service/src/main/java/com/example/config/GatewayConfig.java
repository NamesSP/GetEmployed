package com.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://auth-service"))
                .route("user-service", r -> r.path("/api/users/**")
                        .uri("lb://user-service"))
                .route("company-service", r -> r.path("/api/companies/**")
                        .uri("lb://company-service"))
                .route("job-service", r -> r.path("/api/jobs/**")
                        .uri("lb://job-service"))
                .route("application-service", r -> r.path("/api/applications/**")
                        .uri("lb://application-service"))
                .route("experience-service", r -> r.path("/api/experience/**")
                        .uri("lb://experience-service"))
                .build();
    }

    private static final Logger log = LoggerFactory.getLogger(GatewayConfig.class);

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter logRequestResponseFilter() {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            String method = exchange.getRequest().getMethod().name();
            log.info("➡️ Request: {} {}", method, path);

            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                        log.info("⬅️ Response for {} {}: status {}",
                                method, path, exchange.getResponse().getStatusCode());
                    })
            );
        };
    }
}
