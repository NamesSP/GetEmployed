package com.example.controller;


import com.example.UserServiceApplication;
import com.example.client.AuthServiceClient;

import com.example.dto.UserDto;

import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;


import java.util.List;



import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@SuppressWarnings("deprecation")
@SpringBootTest(classes = UserServiceApplication.class)
class UserControllerIntegratedTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private AuthServiceClient authServiceClient;



    @MockBean
    private UserService userService; // mock the service

    private UserDto sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new UserDto();
        sampleUser.setId(1L);
        sampleUser.setAuthId(101L);
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Doe");
        sampleUser.setEmail("john@example.com");
        sampleUser.setUsername("johndoe");
        sampleUser.setRoles("USER");
    }



    @Test
    void getUserByAuthId_found() {
        when(userService.getUserById(101L)).thenReturn(sampleUser);

        ResponseEntity<UserDto> response = userController.getUserByAuthId(101L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("john@example.com");

        verify(userService).getUserById(101L);
    }

    @Test
    void getUserByAuthId_notFound() {
        when(userService.getUserById(101L)).thenReturn(null);

        ResponseEntity<UserDto> response = userController.getUserByAuthId(101L);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNull();

        verify(userService).getUserById(101L);
    }

    // ---------- getAllUsers tests -------------

    @Test
    void getAllUsers_returnsList() {
        when(userService.getAllUsers()).thenReturn(List.of(sampleUser));

        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getUsername()).isEqualTo("johndoe");

        verify(userService).getAllUsers();
    }

    @Test
    void getAllUsers_returnsEmptyList() {
        when(userService.getAllUsers()).thenReturn(List.of());

        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEmpty();

        verify(userService).getAllUsers();
    }

    // ---------- getUserByEmail tests -------------

    @Test
    void getUserByEmail_found() {
        when(userService.getUserByEmail("john@example.com")).thenReturn(sampleUser);

        ResponseEntity<UserDto> response = userController.getUserByEmail("john@example.com");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("johndoe");

        verify(userService).getUserByEmail("john@example.com");
    }

    @Test
    void getUserByEmail_notFound() {
        when(userService.getUserByEmail("unknown@example.com")).thenReturn(null);

        ResponseEntity<UserDto> response = userController.getUserByEmail("unknown@example.com");

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNull();

        verify(userService).getUserByEmail("unknown@example.com");
    }




}


