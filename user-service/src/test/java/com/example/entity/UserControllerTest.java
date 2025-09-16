package com.example.entity;

import com.example.UserServiceApplication;
import com.example.client.AuthServiceClient;
import com.example.controller.UserController;
import com.example.dto.AuthUserInfoDto;
import com.example.dto.UserDto;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

    @SpringBootTest(classes = UserServiceApplication.class)
    class UserControllerTest {

        @Autowired
        private UserController userController;

        @Autowired
        private UserRepository userRepository;

        @MockBean
        private AuthServiceClient authServiceClient;

        @Test
        void testCreateUser_Success() {
            try {
                // given
                Long testAuthId = 1001L;

                UserDto userDto = new UserDto();
                userDto.setAuthId(testAuthId);
                userDto.setFirstName("John");
                userDto.setLastName("Doe");

                // mock auth-service behavior
                when(authServiceClient.userExists(testAuthId)).thenReturn(true);

                AuthUserInfoDto authInfo = new AuthUserInfoDto();
                authInfo.setEmail("john@example.com");
                authInfo.setUsername("johndoe");
                authInfo.setRole("SEEKER");
                when(authServiceClient.getUserInfoById(testAuthId)).thenReturn(authInfo);

                // make sure DB is clean
                userRepository.deleteAll();

                // when
                ResponseEntity<UserDto> response = userController.createUser(userDto);

                // then (controller response)
                assertNotNull(response);
                assertEquals(200, response.getStatusCodeValue());
                assertNotNull(response.getBody());
                assertEquals("johndoe", response.getBody().getUsername());
                assertEquals("john@example.com", response.getBody().getEmail());

                // and also verify entity persisted in DB
                Optional<UserEntity> saved = userRepository.findByAuthId(testAuthId);
                assertTrue(saved.isPresent());
                assertEquals("John", saved.get().getFirstName());
                assertEquals("Doe", saved.get().getLastName());

            } catch (Exception e) {
                fail("Exception occurred in success test: " + e.getMessage());
            }
        }

        @Test
        void testCreateUser_Failure_MissingAuthId() {
            try {
                UserDto userDto = new UserDto();
                userDto.setFirstName("Jane");

                // when
                ResponseEntity<UserDto> response = userController.createUser(userDto);

                // ❌ should never reach here because service throws exception
                fail("Expected failure due to missing authId but got response: " + response);

            } catch (IllegalArgumentException e) {
                assertEquals("authId is required", e.getMessage());
            } catch (Exception e) {
                fail("Unexpected exception: " + e.getMessage());
            }
        }


    }

