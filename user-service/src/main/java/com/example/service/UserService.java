package com.example.service;

import com.example.client.AuthServiceClient;
import com.example.entity.UserEntity;
import com.example.exceptions.DuplicateFieldException;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthServiceClient authServiceClient;

    // Create
    public UserEntity createUser(UserEntity user) {
        if (user.getAuthId() == null || Boolean.FALSE.equals(authServiceClient.userExists(user.getAuthId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Auth user does not exist for authId " + user.getAuthId());
        }
        // If profile already exists for this authId, reject as duplicate
        var existing = userRepository.findByAuthId(user.getAuthId());
        if (existing.isPresent()) {
            throw new DuplicateFieldException("authId",
                    "User profile already exists for authId " + user.getAuthId());
        }
        try {
            var info = authServiceClient.getUserInfoById(user.getAuthId());
            if (info != null) {
                user.setUsername(info.getUsername());
                user.setRole(info.getRole());
            }
        } catch (Exception ignored) {
        }
        return userRepository.save(user);
    }

    // get AlL
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // getByID
    public Optional<UserEntity> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    // Update userEntity
    public UserEntity updateUser(UserEntity user) {
        if (user.getAuthId() != null) {
            if (Boolean.FALSE.equals(authServiceClient.userExists(user.getAuthId()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Auth user does not exist for authId " + user.getAuthId());
            }
            userRepository.findByAuthId(user.getAuthId()).ifPresent(existing -> {
                if (!existing.getUserId().equals(user.getUserId())) {
                    throw new DuplicateFieldException("authId",
                            "User profile already exists for authId " + user.getAuthId());
                }
            });
            try {
                var info = authServiceClient.getUserInfoById(user.getAuthId());
                if (info != null) {
                    user.setUsername(info.getUsername());
                    user.setRole(info.getRole());
                }
            } catch (Exception ignored) {
            }
        }
        return userRepository.save(user);
    }

    // DeleteById
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }
}
