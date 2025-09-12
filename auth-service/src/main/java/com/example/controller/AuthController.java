package com.example.controller;

import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.dto.RegisterResponse;
import com.example.entity.User;
import com.example.service.AuthService;
import com.example.dto.AuthUserInfoDto;

import lombok.RequiredArgsConstructor;
import com.example.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.dto.Role;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        try {
            if (request.getRole() == null) {
                request.setRole(Role.SEEKER);
            }
            RegisterResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new RegisterResponse(null, null, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(null, null, null, e.getMessage()));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            boolean isValid = authService.validateToken(token);
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

//    @GetMapping("/user-info/{username}")
//    public ResponseEntity<AuthUserInfoDto> getUserInfo(@PathVariable String username) {
//        try {
//            AuthUserInfoDto dto = authService.getUserInfoByUsername(username);
//            return ResponseEntity.ok();
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping("/users/{id}/exists")
    public ResponseEntity<Boolean> userExists(@PathVariable Long id) {
        return ResponseEntity.ok(userRepository.existsById(id));
    }

    @GetMapping(value = "/users/info/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthUserInfoDto> getUserInfoById(@PathVariable("id") Long id) {
        User u = userRepository.findById(id).get();
        AuthUserInfoDto ob= new AuthUserInfoDto();
        ob.setId(u.getId());
        ob.setEmail(u.getUsername());
        ob.setRole(u.getRole().name());
        return new ResponseEntity<>(ob,HttpStatus.OK);
//        return userRepository.findById(id)
//                .map(u -> ResponseEntity.ok()
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(new AuthUserInfoDto(u.getId(), u.getUsername(), u.getRole().name())))
//                .orElse(ResponseEntity.notFound().build());
    }


}
