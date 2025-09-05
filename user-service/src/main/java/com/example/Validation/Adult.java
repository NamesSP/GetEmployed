package com.example.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = AdultValidator.class)
public @interface Adult {
    String message() default "User must be atleast 18 years old";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
}
