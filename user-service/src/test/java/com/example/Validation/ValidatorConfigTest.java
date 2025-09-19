package com.example.Validation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
@ContextConfiguration(classes = ValidatorConfig.class)
class ValidatorConfigTest {

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    void validatorBeanShouldBeLoaded() {
        assertThat(validator).isNotNull();
        assertThat(validator.getValidator()).isNotNull();
    }
}
