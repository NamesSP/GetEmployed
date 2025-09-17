package com.example.service;

import com.example.client.AuthServiceClient;
import com.example.dto.AuthUserInfoDto;
import com.example.dto.UserDto;
import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthServiceClient authServiceClient;

    @InjectMocks
    private UserService userService;

    private UserDto sampleDto;
    private AuthUserInfoDto authInfo;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleDto = new UserDto();
        sampleDto.setAuthId(101L);
        sampleDto.setFirstName("John");
        sampleDto.setLastName("Doe");

        authInfo = new AuthUserInfoDto();
        authInfo.setEmail("john@example.com");
        authInfo.setUsername("johndoe");
        authInfo.setRole("USER");

        userEntity = new UserEntity();
        userEntity.setUserId(1L);
        userEntity.setAuthId(101L);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john@example.com");
        userEntity.setUsername("johndoe");
        userEntity.setRole("USER");
    }

    @Test
    void createUser_successful() {
        when(userRepository.findByAuthId(101L)).thenReturn(Optional.empty());
        when(authServiceClient.userExists(101L)).thenReturn(true);
        when(authServiceClient.getUserInfoById(101L)).thenReturn(authInfo);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto result = userService.createUser(sampleDto);

        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        assertEquals("johndoe", result.getUsername());
        assertEquals("USER", result.getRoles());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void createUser_missingAuthId_throwsException() {
        sampleDto.setAuthId(null);
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(sampleDto));
        verifyNoInteractions(authServiceClient);
    }

    @Test
    void createUser_existingUser_throwsException() {
        when(userRepository.findByAuthId(101L)).thenReturn(Optional.of(userEntity));
        assertThrows(IllegalStateException.class, () -> userService.createUser(sampleDto));
        verify(authServiceClient, never()).userExists(anyLong());
    }

    @Test
    void createUser_userDoesNotExistInAuthService_throwsException() {
        when(userRepository.findByAuthId(101L)).thenReturn(Optional.empty());
        when(authServiceClient.userExists(101L)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> userService.createUser(sampleDto));
    }

    @Test
    void createUser_nullAuthServiceResponse_throwsException() {
        when(userRepository.findByAuthId(101L)).thenReturn(Optional.empty());
        when(authServiceClient.userExists(101L)).thenReturn(true);
        when(authServiceClient.getUserInfoById(101L)).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> userService.createUser(sampleDto));
    }

    @Test
    void getUserById_found() {
        when(userRepository.findByAuthId(101L)).thenReturn(Optional.of(userEntity));

        UserDto result = userService.getUserById(101L);

        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
    }

    @Test
    void getUserById_notFound_returnsNull() {
        when(userRepository.findByAuthId(101L)).thenReturn(Optional.empty());

        UserDto result = userService.getUserById(101L);

        assertNull(result);
    }

    @Test
    void getAllUsers_returnsList() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));

        List<UserDto> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("johndoe", users.get(0).getUsername());
    }

    @Test
    void getUserByEmail_found() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(userEntity));

        UserDto result = userService.getUserByEmail("john@example.com");

        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
    }

    @Test
    void getUserByEmail_notFound_returnsNull() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        UserDto result = userService.getUserByEmail("john@example.com");

        assertNull(result);
    }
}
