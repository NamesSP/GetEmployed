package com.example.util;

import org.springframework.web.server.ServerWebExchange;

public class UserContextUtil {

    public static String getCurrentUsername(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("X-User-Name");
    }

    public static String getCurrentUserRole(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("X-User-Role");
    }

    public static String getCurrentUserId(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst("X-User-Id");
    }

    public static boolean isAdmin(ServerWebExchange exchange) {
        String role = getCurrentUserRole(exchange);
        return "ROLE_ADMIN".equals(role);
    }

    public static boolean isRecruiter(ServerWebExchange exchange) {
        String role = getCurrentUserRole(exchange);
        return "ROLE_RECRUITER".equals(role) || "ROLE_ADMIN".equals(role);
    }

    public static boolean isSeeker(ServerWebExchange exchange) {
        String role = getCurrentUserRole(exchange);
        return "ROLE_SEEKER".equals(role) || "ROLE_ADMIN".equals(role);
    }
}
