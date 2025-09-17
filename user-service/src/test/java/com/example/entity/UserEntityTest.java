package com.example.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserEntityTest {

    @Test
    void testGettersAndSetters() {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setAuthId(100L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        user.setRole("USER");

        assertEquals(1L, user.getUserId());
        assertEquals(100L, user.getAuthId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("johndoe", user.getUsername());
        assertEquals("USER", user.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        UserEntity u1 = new UserEntity();
        u1.setUserId(1L);
        u1.setAuthId(200L);
        u1.setFirstName("Alice");
        u1.setLastName("Smith");
        u1.setEmail("alice.smith@example.com");
        u1.setUsername("alicesmith");
        u1.setRole("ADMIN");

        UserEntity u2 = new UserEntity();
        u2.setUserId(1L);
        u2.setAuthId(200L);
        u2.setFirstName("Alice");
        u2.setLastName("Smith");
        u2.setEmail("alice.smith@example.com");
        u2.setUsername("alicesmith");
        u2.setRole("ADMIN");

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testToString() {
        UserEntity user = new UserEntity();
        user.setUserId(10L);
        user.setAuthId(300L);
        user.setFirstName("Bob");
        user.setLastName("Johnson");
        user.setEmail("bob.johnson@example.com");
        user.setUsername("bobjohnson");
        user.setRole("USER");

        String str = user.toString();
        assertTrue(str.contains("10"));
        assertTrue(str.contains("300"));
        assertTrue(str.contains("Bob"));
        assertTrue(str.contains("Johnson"));
        assertTrue(str.contains("bob.johnson@example.com"));
        assertTrue(str.contains("bobjohnson"));
        assertTrue(str.contains("USER"));
    }

    @Test
    void testObjectCreation() {
        UserEntity user = new UserEntity();
        assertNotNull(user);
    }
}
