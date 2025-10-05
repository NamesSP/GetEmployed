package com.example.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // ❌ disable CSRF
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/auth/**").permitAll() // allow login/register
                        .anyExchange().authenticated()            // require auth for others
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // disable default login popup
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // disable form login
                .build();
    }
}

