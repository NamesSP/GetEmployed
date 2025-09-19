
package com.example.service;

import com.example.client.AuthServiceClient;
import com.example.dto.UserDto;
import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import com.example.dto.AuthUserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthServiceClient authServiceClient;

    public UserDto createUser(UserDto userDto) {
        if (userDto.getAuthId() == null) {
            throw new IllegalArgumentException("authId is required");
        }

        // Check if authId already exists in user_profiles table
        Optional<UserEntity> existingUser = userRepository.findByAuthId(userDto.getAuthId());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("User profile already exists for authId: " + userDto.getAuthId());
        }

        // Check if authId exists in auth-service users table
        Boolean userExists = authServiceClient.userExists(userDto.getAuthId());
        if (userExists == null || !userExists) {
            throw new IllegalStateException(
                    "Invalid authId: " + userDto.getAuthId() + ". User does not exist in auth-service.");
        }

        // Fetch user info from auth-service
        AuthUserInfoDto authInfo = authServiceClient.getUserInfoById(userDto.getAuthId());
        if (authInfo == null) {
            throw new IllegalStateException(
                    "Failed to fetch user information from auth-service for authId: " + userDto.getAuthId());
        }

        UserEntity newUser = new UserEntity();
        newUser.setAuthId(userDto.getAuthId());
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());

        // Populate from auth-service
        newUser.setEmail(authInfo.getEmail());
        newUser.setUsername(authInfo.getUsername());
        newUser.setRole(authInfo.getRole());

        userRepository.save(newUser);
        return toDto(newUser);
    }

    public UserDto getUserById(Long id) {
        Optional<UserEntity> user = userRepository.findByAuthId(id);
        return user.map(this::toDto).orElse(null);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public UserDto getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toDto).orElse(null);
    }

    private UserDto toDto(UserEntity user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getUserId());
        userDto.setAuthId(user.getAuthId()); // Add this
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRole());
        return userDto;
    }

}
