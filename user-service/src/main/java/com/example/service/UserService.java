
package com.example.service;

import com.example.dto.UserDto;
import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        UserEntity userEntity = toEntity(userDto);
        userEntity = userRepository.save(userEntity);
        return toDto(userEntity);
    }

    public UserDto getUserById(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
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
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRole());
        return userDto;
    }

    private UserEntity toEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setRole(userDto.getRoles());
        return userEntity;
    }
}
