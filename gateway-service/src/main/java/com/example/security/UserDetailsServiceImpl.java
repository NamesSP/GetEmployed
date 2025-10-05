package com.example.security; // Gateway-specific package

import com.example.dto.AuthUserInfoDto; // Shared DTO (from common/auth)
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RestTemplate restTemplate; // Bean in SecurityConfig

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Call auth-service via direct URL (bypasses gateway for internal call; use
            // service discovery if needed)
            String authServiceUrl = "http://localhost:8082"; // Adjust to auth-service port (e.g., 8080)
            String endpoint = UriComponentsBuilder
                    .fromHttpUrl(authServiceUrl + "/api/auth/users/info/username/{username}")
                    .buildAndExpand(username).toUriString();

            AuthUserInfoDto userInfo = restTemplate.getForObject(endpoint, AuthUserInfoDto.class);
            if (userInfo == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            // Build UserDetails with role from DTO (password not needed for JWT)
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userInfo.getUsername())
                    .password("") // Dummy; JWT doesn't use it
                    .authorities(Collections.singletonList(
                            new SimpleGrantedAuthority(userInfo.getRole()) // e.g., "ROLE_ADMIN"
                    ))
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Failed to load user: " + username, e);
        }
    }
}