package com.example.service;

//import org.example.dto.AuthResponse;
//import org.example.dto.AuthUserInfoDto;
//import org.example.dto.LoginRequest;
//import org.example.dto.RegisterRequest;
//import org.example.dto.RegisterResponse;
import com.example.dto.AuthResponse;
import org.example.dto.AuthUserInfoDto;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.dto.RegisterResponse;

import com.example.dto.Role;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
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

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponse(token, user.getUsername(), user.getRole().name(), "Login successful");
    }

    public boolean validateToken(String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return jwtUtil.validateToken(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }

    public AuthUserInfoDto getUserInfoByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new AuthUserInfoDto(user.getId(), user.getUsername(), user.getRole().name());
    }
}
