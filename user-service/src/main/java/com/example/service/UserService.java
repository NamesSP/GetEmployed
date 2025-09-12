
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

        // Fetch user info from auth-service
        AuthUserInfoDto authInfo = authServiceClient.getUserInfoById(userDto.getAuthId());
        if (authInfo == null) {
            throw new IllegalStateException("No user found in auth-service with authId: " + userDto.getAuthId());
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
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRole());
        return userDto;
    }

    private UserEntity toEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setAuthId(userDto.getAuthId()); // Add this
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setRole(userDto.getRoles());
        return userEntity;
    }
}
