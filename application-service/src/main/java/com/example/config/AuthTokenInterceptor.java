package com.example.config;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthTokenInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret}")
    private String secretKey;

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
