package com.example.util; // Gateway's util package

import com.example.security.UserDetailsServiceImpl; // Gateway-local
import io.jsonwebtoken.Claims; // If needed for extractAllClaims; from jjwt
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

public class JwtReactiveFilter implements WebFilter {

    private final JwtUtil jwtUtil; // From common module
    private final UserDetailsServiceImpl userDetailsService; // Gateway-local

    public JwtReactiveFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtUtil.extractUsername(token);

            if (username != null) {
                // Check if context is already set and filter only if it's NOT set
                return ReactiveSecurityContextHolder.getContext()
                        .defaultIfEmpty(new SecurityContextImpl(null)) // Provide an empty context if no context is
                                                                       // found
                        .filter(ctx -> ctx.getAuthentication() == null) // Filter for only an unauthenticated context
                        .flatMap(ctx -> {
                            // Load user details synchronously
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                            if (jwtUtil.validateToken(token, userDetails)) {
                                Claims claims = jwtUtil.extractAllClaims(token); // From common
                                String role = claims.get("role", String.class);
                                System.out.println("Gateway Reactive: Role from token for "
                                        + exchange.getRequest().getPath() + ": " + role);

                                List<SimpleGrantedAuthority> authorities;
                                if ("ROLE_ADMIN".equals(role)) {
                                    authorities = List.of(
                                            new SimpleGrantedAuthority("ROLE_ADMIN"),
                                            new SimpleGrantedAuthority("ROLE_RECRUITER"),
                                            new SimpleGrantedAuthority("ROLE_SEEKER"));
                                } else {
                                    authorities = List.of(new SimpleGrantedAuthority(role));
                                }

                                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                        userDetails, null, authorities);
                                SecurityContext context = new SecurityContextImpl(authToken);

                                // Correct way to run the filter chain within the new SecurityContext
                                return chain.filter(exchange)
                                        .contextWrite(
                                                ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
                            }

                            // Token invalid but username existed: continue the chain without security
                            // context
                            return chain.filter(exchange);
                        })
                        // If no context was present, or it was already authenticated (filtered out by
                        // .filter())
                        .switchIfEmpty(chain.filter(exchange));
            }

            // Username was null: continue the chain without security context
            return chain.filter(exchange);

        } catch (Exception e) {
            System.err.println(
                    "Gateway Reactive JWT failed for " + exchange.getRequest().getPath() + ": " + e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
            return exchange.getResponse().writeWith(Mono.just(
                    exchange.getResponse().bufferFactory()
                            .wrap("{\"error\": \"Invalid or expired token\"}".getBytes())));
        }
    }
}