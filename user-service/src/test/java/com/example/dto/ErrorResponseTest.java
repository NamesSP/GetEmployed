package com.example.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testNoArgsConstructor() {
        ErrorResponse response = new ErrorResponse();

        // Fields should have default values
        assertNull(response.getMessage());
        assertEquals(0L, response.getTimestamp());
    }

    @Test
    void testAllArgsConstructor() {
        String message = "Error occurred";
        long timestamp = 123456789L;

        ErrorResponse response = new ErrorResponse(message, timestamp);

        assertEquals(message, response.getMessage());
        assertEquals(timestamp, response.getTimestamp());
    }

    @Test
    void testSingleArgConstructorSetsTimestamp() {
        String message = "Single argument error";

        long beforeCreation = System.currentTimeMillis();
        ErrorResponse response = new ErrorResponse(message);
        long afterCreation = System.currentTimeMillis();

        assertEquals(message, response.getMessage());

        // Timestamp should be set automatically and within the creation time window
        assertTrue(response.getTimestamp() >= beforeCreation && response.getTimestamp() <= afterCreation);
    }

    @Test
    void testSettersAndGetters() {
        ErrorResponse response = new ErrorResponse();

        String message = "Updated message";
        long timestamp = 987654321L;

        response.setMessage(message);
        response.setTimestamp(timestamp);

        assertEquals(message, response.getMessage());
        assertEquals(timestamp, response.getTimestamp());
    }

    @Test
    void testTimestampImmutabilityForCustomConstructor() {
        String message = "Check timestamp";
        ErrorResponse response = new ErrorResponse(message);

        long firstTimestamp = response.getTimestamp();

        // Update message shouldn't affect timestamp
        response.setMessage("Changed message");

        assertEquals(firstTimestamp, response.getTimestamp());
    }
}
