package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import com.example.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .csrf(csrf-> csrf.disable())
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/api/admin/**").hasRole("ADMIN")
                        .pathMatchers("/users/**").hasAnyRole("SEEKER", "ADMIN")
                        .pathMatchers("/companies/**").hasAnyRole("RECRUITER", "ADMIN")
                        .pathMatchers("/jobs/**").hasAnyRole("RECRUITER", "ADMIN")
                        .pathMatchers("/experiences/**").hasAnyRole("RECRUITER", "ADMIN", "SEEKER")
                        .pathMatchers("/applications/**").hasAnyRole("SEEKER", "ADMIN")
                        .pathMatchers("/recruiter/**").hasAnyRole("RECRUITER", "ADMIN")
                        .pathMatchers("/user/**").hasAnyRole("SEEKER", "ADMIN")
                        .pathMatchers("/admin/**").hasRole("ADMIN")
                        .anyExchange().authenticated())
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }

}
