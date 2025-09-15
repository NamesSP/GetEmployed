package com.example.config;

import com.example.client.AuthServiceClient;
import com.example.dto.ValidateTokenResponse;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class AuthTokenInterceptor implements HandlerInterceptor {

    private final AuthServiceClient authServiceClient;
    @Value("${jwt.secret}")
    private String secretKey;

    public AuthTokenInterceptor(@Lazy AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        System.out.println("🔑 Incoming token: " + authHeader);

            try {
               Jwts.parser()
                        .setSigningKey(secretKey) // must match auth-service
                        .parseClaimsJws(authHeader.replace("Bearer ", ""));
                System.out.println("✅ Token validation successful");
            } catch (Exception e) {
                System.out.println("❌ Token validation failed: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            return true;
        }
//
//        if (authHeader == null || authHeader.isBlank()) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Missing Authorization header");
//            return false;
//        }
//
//        try {
//            var result = authServiceClient.validate(authHeader);
//            if (!result.isValid()) { // adjust depending on your ValidateTokenResponse
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Invalid or expired token");
//                return false;
//            }
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Token validation failed");
//            return false;
//        }
//
//        return true;
    }

