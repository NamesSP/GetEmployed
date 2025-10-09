
// package com.example.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Primary;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.cors.CorsConfigurationSource;

// import java.util.List;

// @Configuration
// @Primary
// public class CorsConfig {

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration configuration = new CorsConfiguration();

//         configuration.setAllowedOrigins(List.of("http://localhost:3000"));
//         configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         configuration.setAllowedHeaders(List.of("*")); // or be explicit, e.g. "Authorization", "Content-Type"
//         configuration.setAllowCredentials(true);

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

//         source.registerCorsConfiguration("/api/**", configuration);
//         source.registerCorsConfiguration("/auth/**", configuration);

//         return source;
//     }
// }

//package com.example.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.config.CorsRegistry;
//import org.springframework.web.reactive.config.WebFluxConfigurer;
//
//@Configuration
//public class CorsConfig implements WebFluxConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")
//                .allowedOrigins("http://localhost:3000")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//
//        registry.addMapping("/auth/**")
//                .allowedOrigins("http://localhost:3000")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
//}