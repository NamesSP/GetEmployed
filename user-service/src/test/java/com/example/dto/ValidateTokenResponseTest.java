package com.example.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidateTokenResponseTest {

    @Test
    void testGettersAndSetters() {
        ValidateTokenResponse response = new ValidateTokenResponse();

        // Set values
        response.setValid(true);
        response.setUsername("testuser");
        response.setRole("ADMIN");
        response.setMessage("Token is valid");

        // Assert values
        assertTrue(response.isValid());
        assertEquals("testuser", response.getUsername());
        assertEquals("ADMIN", response.getRole());
        assertEquals("Token is valid", response.getMessage());
    }

    @Test
    void testDefaultValues() {
        ValidateTokenResponse response = new ValidateTokenResponse();

        // Default values should be false/null
        assertFalse(response.isValid());
        assertNull(response.getUsername());
        assertNull(response.getRole());
        assertNull(response.getMessage());
    }
}
