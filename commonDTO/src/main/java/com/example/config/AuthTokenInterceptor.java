package com.example.config;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@ConditionalOnProperty(name = "auth.interceptor.enabled", havingValue = "true")
public class AuthTokenInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    public void init() {
        System.out.println("🔍 AuthTokenInterceptor initialized — this should NOT happen if disabled");
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        System.out.println("🔑 Incoming token: " + authHeader);

        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(authHeader.replace("Bearer ", ""));
            System.out.println("✅ Token validation successful");
        } catch (Exception e) {
            System.out.println("❌ Token validation failed: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;


    }
}
