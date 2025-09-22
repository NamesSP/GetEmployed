package com.example.controller;

import com.example.dto.UserDto;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        //modified to catch 2 different types of err -prym cr
        try {
            return ResponseEntity.ok(userService.createUser(userDto));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build(); // 409 Conflict
        }
    }

    @GetMapping("/{authId}")
    public ResponseEntity<UserDto> getUserByAuthId(@PathVariable Long authId) {
        UserDto userDto = userService.getUserById(authId);
        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto userDto = userService.getUserByEmail(email);
        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }
}