package com.example.exceptions;

public class DuplicateFieldException extends RuntimeException {
    private final String fieldName;

    public DuplicateFieldException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
