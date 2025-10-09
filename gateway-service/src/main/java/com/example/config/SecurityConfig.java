package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import com.example.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    // @Bean
    // public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http)
    // {
    // return http
    // .csrf(ServerHttpSecurity.CsrfSpec::disable) // ❌ disable CSRF
    // .authorizeExchange(exchanges -> exchanges
    // .pathMatchers("/api/auth/**").permitAll() // allow login/register
    // .anyExchange().authenticated() // require auth for others
    // )
    // .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // disable default
    // login popup
    // .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // disable form login
    // .build();
    // }
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        System.out.println(">>> Security config loaded <<<");
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/test/**").permitAll()
                        .pathMatchers("/api/admin/**").hasRole("ADMIN")
                        .pathMatchers("/users/**").hasAnyRole("SEEKER", "ADMIN","RECRUITER")
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
