package com.example.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleMethodArgumentNotValid() {
        // Dummy bean with a username field
        class UserDto {
            private String username;
            public String getUsername() { return username; }
            public void setUsername(String username) { this.username = username; }
        }

        BindException bindException = new BindException(new UserDto(), "userDto");
        bindException.rejectValue("username", "NotBlank", "must not be blank");

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindException);

        var response = handler.f1(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("username", "must not be blank");
    }


    @Test
    void testHandleTypeMismatch_roleField() {
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException("INVALID", String.class, "role", null, null);

        var response = handler.handleTypeMismatch(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody())
                .containsEntry("role", "Role must be one of [SEEKER, RECRUITER]");
    }

    @Test
    void testHandleTypeMismatch_otherField() {
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException("123", Integer.class, "age", null, null);

        var response = handler.handleTypeMismatch(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("age", "Invalid value");
    }

    @Test
    void testHandleDuplicateField() {
        DuplicateFieldException ex =
                new DuplicateFieldException("email", "email already registered");

        var response = handler.handleDuplicateField(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(409);
        assertThat(response.getBody()).containsEntry("email", "email already registered");
    }

    @Test
    void testHandleAllUncaughtException() {
        Exception ex = new RuntimeException("Something went wrong");

        var response = handler.handleAllUncaughtException(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(500);
        assertThat(response.getBody().get("error"))
                .isEqualTo("An unexpected error occurred: Something went wrong");
    }

    @Test
    void testHandleHttpMessageNotReadable_invalidFormat_role() {
        InvalidFormatException invalidFormat =
                new InvalidFormatException(null, "Invalid role", "INVALID", Role.class);

        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("Bad JSON", invalidFormat, null);

        var response = handler.handleHttpMessageNotReadable(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody())
                .containsEntry("role", "Role must be one of [SEEKER, RECRUITER]");
    }

    @Test
    void testHandleHttpMessageNotReadable_malformedJson() {
        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("Malformed JSON");

        var response = handler.handleHttpMessageNotReadable(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("error", "Malformed JSON request");
    }

    // ✅ Dummy Role class just for testing (no need in main code)
    static class Role {}
}
