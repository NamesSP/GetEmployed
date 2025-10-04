package com.example.filter;

import com.example.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class GatewayJwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public GatewayJwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Skip authentication for public endpoints
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return handleUnauthorized(exchange, "Missing or invalid authorization header");
        }

        String token = authHeader.substring(7);
        
        try {
            // Validate token
            if (!isValidToken(token)) {
                return handleUnauthorized(exchange, "Invalid token");
            }

            // Extract user information and add to headers for downstream services
            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractAllClaims(token).get("role", String.class);
            
            // Add user context to request headers for downstream services
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Name", username)
                    .header("X-User-Role", role)
                    .header("X-User-Id", extractUserIdFromToken(token))
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
            
        } catch (Exception e) {
            return handleUnauthorized(exchange, "Token validation failed: " + e.getMessage());
        }
    }

    private boolean isPublicEndpoint(String path) {
        List<String> publicPaths = List.of(
                "/api/auth/register",
                "/api/auth/login",
                "/api/auth/validate",
                "/actuator",
                "/error"
        );
        
        return publicPaths.stream().anyMatch(path::startsWith);
    }

    private boolean isValidToken(String token) {
        try {
            return !jwtUtil.extractExpiration(token).before(new java.util.Date());
        } catch (Exception e) {
            return false;
        }
    }

    private String extractUserIdFromToken(String token) {
        try {
            // This is a simplified approach - in a real scenario, you might want to store user ID in the token
            return jwtUtil.extractUsername(token); // Using username as ID for now
        } catch (Exception e) {
            return "unknown";
        }
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");
        
        String body = "{\"error\":\"" + message + "\",\"status\":401}";
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @Override
    public int getOrder() {
        return -1; // High priority
    }
}
