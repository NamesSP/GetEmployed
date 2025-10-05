package com.example.service;

import com.example.dto.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.util.JwtUtil; // ✅ From common module
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // ✅ Injected from common (via @Component in JwtUtil if needed)
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {
        if (request.getRole() == Role.ADMIN) {
            throw new RuntimeException("Admin registration is not allowed");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.SEEKER);

        userRepository.save(user);

        return new RegisterResponse(user.getUsername(), user.getRole().name(), "User registered successfully");
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = (User) authentication.getPrincipal(); // Safe cast (from UserDetailsService in auth-service)
        UserTokenPayload payload = new UserTokenPayload(user.getUsername(), user.getRole());
        String token = jwtUtil.generateToken(payload);

        return new AuthResponse(token, user.getUsername(), user.getRole().name(), "Login successful");
    }

    public boolean validateToken(String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // ✅ FIXED: Wrap User in UserDetails (authorities from role)
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword()) // Encoded; used for validation if needed
                    .authorities(Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                    .build();

            return jwtUtil.validateToken(token, userDetails); // Now passes proper UserDetails
        } catch (Exception e) {
            return false;
        }
    }

    public AuthUserInfoDto getUserInfoByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AuthUserInfoDto dto = new AuthUserInfoDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name()); // No "ROLE_" prefix here; add in controller if needed for DTO
        return dto;
    }
}