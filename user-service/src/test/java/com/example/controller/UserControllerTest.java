package com.example.controller;


import com.example.UserServiceApplication;
import com.example.client.AuthServiceClient;
import com.example.dto.AuthUserInfoDto;
import com.example.dto.UserDto;
import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;



import java.util.Optional;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@SuppressWarnings("deprecation")
@SpringBootTest(classes = UserServiceApplication.class)
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private AuthServiceClient authServiceClient;



    @Autowired
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
        UserDto userDto = new UserDto();
        userDto.setFirstName("Jane");

        ResponseEntity<UserDto> response = userController.createUser(userDto);

        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
    @Test
    void testCreateUser_Failure_AlreadyExists() {
        userRepository.deleteAll();

        String email = "john@example.com";
        Long authId1 = 101L;

        // Insert first user
        userRepository.save(new UserEntity(null, authId1, "John", "Doe", email, "johndoe", "SEEKER"));

        // Attempt to create another user with the same email
        UserDto userDto = new UserDto();
        userDto.setAuthId(202L);
        userDto.setFirstName("Jane");
        userDto.setLastName("Smith");

        ResponseEntity<UserDto> response = userController.createUser(userDto);

        assertEquals(409, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
