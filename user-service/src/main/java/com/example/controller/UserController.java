package com.example.controller;

import com.example.entity.UserEntity;
import com.example.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserEntity> createUser(@Valid @RequestBody UserEntity user) {
        UserEntity savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/get/allUsers")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/set/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @Valid @RequestBody UserEntity user) {
        return userService.getUserById(id).map(existingUser -> {
            user.setUserId(id);  // keep same id for update
            UserEntity updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        }).orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.getUserById(id).map(existingUser -> {
            userService.deleteUser(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

}
