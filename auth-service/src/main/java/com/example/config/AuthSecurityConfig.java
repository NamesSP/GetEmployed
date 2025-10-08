package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class AuthSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http
                // Disable CSRF for API usage
                .csrf(csrf -> csrf.disable())

                // Configure route access
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/auth/**").permitAll() // allow all auth endpoints
                        .pathMatchers(HttpMethod.OPTIONS).permitAll() // allow CORS preflight requests
                        .anyExchange().authenticated() // all other endpoints require authentication
                )

        // Optional: you can configure basic auth or JWT filter here
        //.httpBasic(withDefaults())
        //.addFilterAt(jwtAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)

        ;

        return http.build();
    }
}
