package com.example.service;

import com.example.entity.UserEntity;
import com.example.exceptions.DuplicateFieldException;
import com.example.repository.UserRepository;
import jakarta.validation.constraints.Max;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    //Create
    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }
    //get AlL
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
    //getByID
    public Optional<UserEntity> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
    //Update userEntity
    public UserEntity updateUser(UserEntity user) {
        return userRepository.save(user);
    }
    //DeleteById
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
