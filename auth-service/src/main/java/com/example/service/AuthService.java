package com.example.service;

import com.example.dto.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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
        // Simple authentication without Spring Security
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        UserTokenPayload payload = new UserTokenPayload(user.getUsername(), user.getRole());
        String token = jwtUtil.generateToken(payload);

        return new AuthResponse(token, user.getUsername(), user.getRole().name(), "Login successful");
    }

    public AuthUserInfoDto getUserInfoByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AuthUserInfoDto dto = new AuthUserInfoDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());
        return dto;
    }
}
