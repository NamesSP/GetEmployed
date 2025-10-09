package com.example.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.server.csrf.CsrfWebFilter;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.server.WebFilter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;
import org.springframework.security.web.server.csrf.CsrfWebFilter;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://auth-service")) // Eureka-aware
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


    @Bean
    public ApplicationRunner showSecurityBeans(ApplicationContext ctx) {
        return args -> {
            System.out.println("=== SecurityWebFilterChain beans ===");
            for (String name : ctx.getBeanNamesForType(SecurityWebFilterChain.class)) {
                System.out.println(name + " -> " + ctx.getBean(name).getClass().getName());
            }

            System.out.println("=== WebFilter beans (some relevant types) ===");
            for (String name : ctx.getBeanNamesForType(WebFilter.class)) {
                System.out.println(name + " -> " + ctx.getBean(name).getClass().getName());
            }

            System.out.println("=== CsrfWebFilter beans ===");
            for (String name : ctx.getBeanNamesForType(CsrfWebFilter.class)) {
                System.out.println(name + " -> " + ctx.getBean(name).getClass().getName());
            }
        };
    }



    @Configuration
    public class LoggingFilterConfig {

        private static final Logger log = LoggerFactory.getLogger(LoggingFilterConfig.class);

        @Bean
        @Order(Ordered.HIGHEST_PRECEDENCE)
        public GlobalFilter logRequestResponseFilter() {
            return (exchange, chain) -> {
                String path = exchange.getRequest().getPath().toString();
                String method = exchange.getRequest().getMethod().name();
                log.info("➡️ Request: {} {}", method, path);

                exchange.getRequest().getHeaders()
                        .forEach((k, v) -> log.debug("Request Header {} = {}", k, v));

                return chain.filter(exchange).then(
                        Mono.fromRunnable(() -> {
                            log.info("⬅️ Response for {} {}: status {}", method, path,
                                    exchange.getResponse().getStatusCode());
                        })
                );
            };
        }
    }

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void checkCorsBeans() {
        Map<String, CorsConfigurationSource> beans = context.getBeansOfType(CorsConfigurationSource.class);
        System.out.println("CorsConfigurationSource beans found:");
        beans.forEach((name, bean) -> System.out.println(" - " + name + ": " + bean));
    }

}
