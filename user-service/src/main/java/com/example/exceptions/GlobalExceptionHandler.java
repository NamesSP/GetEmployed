package com.example.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> f1(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        if ("role".equals(ex.getName())) {
            errors.put("role", "Role must be one of [SEEKER, RECRUITER]");
        } else {
            errors.put(ex.getName(), "Invalid value");
        }
        return ResponseEntity.badRequest().body(errors);
    }

    // Handle database unique constraint violations globally
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
//        Map<String, String> errors = new HashMap<>();
//        Throwable cause = ex.getRootCause();
//        if (cause != null && cause.getMessage() != null) {
//            String message = cause.getMessage();
//            if (message.contains("user_name")) {  // Adjust these checks based on your DB constraint names
//                errors.put("userName", "user_name already taken");
//            } else if (message.contains("email")) {
//                errors.put("email", "email already registered");
//            } else {
//                errors.put("database", "Data integrity violation");
//            }
//        } else {
//            errors.put("database", "Data integrity violation");
//        }
//        return ResponseEntity.badRequest().body(errors);
//    }

    // Handle your custom exceptions, e.g., DuplicateFieldException
//    @ExceptionHandler(DuplicateFieldException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<Map<String, String>> handleDuplicateField(DuplicateFieldException ex) {
//        Map<String, String> errors = new HashMap<>();
//        errors.put(ex.getFieldName(), ex.getMessage());
//        return ResponseEntity.badRequest().body(errors);
//    }

    // A generic fallback error handler
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleAllUncaughtException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        Throwable root = getRootCause(ex);

        if (root instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) root;
            String targetType = ife.getTargetType().getSimpleName();
            if ("Role".equals(targetType)) {
                errors.put("role", "Role must be one of [SEEKER, RECRUITER]");
            } else {
                errors.put("error", "Invalid value for " + ife.getPathReference());
            }
        } else {
            errors.put("error", "Malformed JSON request");
        }
        return ResponseEntity.badRequest().body(errors);
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable cause;
        Throwable result = throwable;
        while ((cause = result.getCause()) != null && result != cause) {
            result = cause;
        }
        return result;
    }
}
