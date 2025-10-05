package com.example.config;

import com.example.security.UserDetailsServiceImpl;
import com.example.util.JwtReactiveFilter;
import com.example.util.JwtUtil; // From common
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder; // CORRECTED IMPORT
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestTemplate;
// Removed unnecessary WebClient import
// import org.springframework.web.reactive.function.client.WebClient; 

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil; // From common (autowired as @Component)
    private final UserDetailsService userDetailsService; // Gateway's impl

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // If needed elsewhere; optional for gateway
    }

    @Bean
    public RestTemplate restTemplate() {
        // NOTE: For a fully reactive Spring Cloud Gateway, you typically use WebClient
        // instead of RestTemplate,
        // but keeping it as per your original code.
        return new RestTemplate();
    }

    @Bean
    public JwtReactiveFilter jwtReactiveFilter() {
        // Cast is necessary because the required UserDetailsService implementation
        // (UserDetailsServiceImpl)
        // is needed by the filter constructor.
        return new JwtReactiveFilter(jwtUtil, (UserDetailsServiceImpl) userDetailsService);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
            JwtReactiveFilter jwtReactiveFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Public paths (no auth needed)
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers("/api/auth/**").permitAll() // Login/register/validate
                        .pathMatchers("/public/**", "/error", "/actuator/**").permitAll()

                        // Centralized RBAC (enforced before routing)
                        .pathMatchers("/api/admin/**").hasRole("ADMIN")
                        .pathMatchers("/api/recruiters/**").hasRole("RECRUITER")
                        .pathMatchers("/api/seekers/**").hasRole("SEEKER") // Example; add as needed

                        // All other paths: Require authentication (any role)
                        .anyExchange().authenticated())

                // Add your custom JWT filter before Spring Security's standard authentication
                // filter
                .addFilterAt(jwtReactiveFilter, SecurityWebFiltersOrder.AUTHENTICATION)

                .exceptionHandling(ex -> ex
                        // Handle unauthenticated requests (e.g., missing token)
                        .authenticationEntryPoint((exchange, ex1) -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            // Complete the response Mono<Void>
                            return exchange.getResponse().setComplete();
                        })
                        // Adding authorization failure handler is also recommended here (Forbidden/403)
                        .accessDeniedHandler((exchange, ex2) -> {
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }))
                .build();
    }
}
