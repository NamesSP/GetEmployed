package com.example.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ApiErrorTest {

    @Test
    void testDefaultConstructor() {
        ApiError error = new ApiError();

        // timestamp should be initialized automatically
        assertNotNull(error.getTimestamp());

        // other fields should have default values
        assertEquals(0, error.getStatus());
        assertNull(error.getError());
        assertNull(error.getMessage());
        assertNull(error.getPath());
    }

    @Test
    void testParameterizedConstructor() {
        int status = 404;
        String errorMsg = "Not Found";
        String message = "The requested resource was not found";
        String path = "/api/test";

        ApiError error = new ApiError(status, errorMsg, message, path);

        assertNotNull(error.getTimestamp());
        assertEquals(status, error.getStatus());
        assertEquals(errorMsg, error.getError());
        assertEquals(message, error.getMessage());
        assertEquals(path, error.getPath());
    }

    @Test
    void testSettersAndGetters() {
        ApiError error = new ApiError();

        int status = 500;
        String errorMsg = "Internal Server Error";
        String message = "Something went wrong";
        String path = "/api/test";

        error.setStatus(status);
        error.setError(errorMsg);
        error.setMessage(message);
        error.setPath(path);

        assertEquals(status, error.getStatus());
        assertEquals(errorMsg, error.getError());
        assertEquals(message, error.getMessage());
        assertEquals(path, error.getPath());
    }

    @Test
    void testTimestampImmutability() {
        ApiError error = new ApiError();
        Instant firstTimestamp = error.getTimestamp();

        // Ensure timestamp remains constant after other setters
        error.setStatus(400);
        error.setError("Bad Request");
        error.setMessage("Invalid input");
        error.setPath("/api/input");

        assertEquals(firstTimestamp, error.getTimestamp());
    }

    @Test
    void testTimestampIsRecent() {
        ApiError error = new ApiError();
        Instant now = Instant.now();

        // Ensure timestamp is very recent (within 1 second)
        assertTrue(!error.getTimestamp().isBefore(now.minusSeconds(1)) &&
                !error.getTimestamp().isAfter(now.plusSeconds(1)));
    }
}
