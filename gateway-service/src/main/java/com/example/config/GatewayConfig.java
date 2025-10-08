//package com.example.config;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class GatewayConfig {
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("auth-service", r -> r.path("/api/auth/**")
//                        .uri("lb://auth-service")) // Eureka-aware
//                .route("user-service", r -> r.path("/api/users/**")
//                        .uri("lb://user-service"))
//                .route("company-service", r -> r.path("/api/companies/**")
//                        .uri("lb://company-service"))
//                .route("job-service", r -> r.path("/api/jobs/**")
//                        .uri("lb://job-service"))
//                .route("application-service", r -> r.path("/api/applications/**")
//                        .uri("lb://application-service"))
//                .route("experience-service", r -> r.path("/api/experience/**")
//                        .uri("lb://experience-service"))
//                .build();
//    }
//
//    @Bean
//    public CorsWebFilter corsWebFilter() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.setAllowedOriginPatterns(List.of("*"));
//        corsConfig.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
//        corsConfig.setAllowedHeaders(List.of("*"));
//        corsConfig.setAllowCredentials(true);
//        corsConfig.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig);
//
//        return new CorsWebFilter(source);
//    }
//}
package com.example.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("http://localhost:8082"))
                .route("user-service", r -> r.path("/users/**")
                        .uri("http://localhost:8090"))
                .route("company-service", r -> r.path("/companies/**")
                        .uri("http://localhost:8083"))
                .route("job-service", r -> r.path("/jobs/**")
                        .uri("http://localhost:8084"))
                .route("application-service", r -> r.path("/applications/**")
                        .uri("http://localhost:8085"))
                .route("experience-service", r -> r.path("/experiences/**")
                        .uri("http://localhost:8086"))
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(List.of("*"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
