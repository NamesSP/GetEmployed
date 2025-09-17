package com.example.Validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AdultValidatorTest {

    private AdultValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AdultValidator();
    }

    @Test
    void shouldReturnFalseWhenDobIsNull() {
        boolean result = validator.isValid(null, null);
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueWhenAgeIs18() {
        LocalDate dob = LocalDate.now().minusYears(18);
        boolean result = validator.isValid(dob, null);
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueWhenAgeIsGreaterThan18() {
        LocalDate dob = LocalDate.now().minusYears(25);
        boolean result = validator.isValid(dob, null);
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenAgeIsLessThan18() {
        LocalDate dob = LocalDate.now().minusYears(17).plusDays(1);
        boolean result = validator.isValid(dob, null);
        assertThat(result).isFalse();
    }
}
