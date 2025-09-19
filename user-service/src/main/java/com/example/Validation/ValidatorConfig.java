package com.example.Validation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

@Configuration
public class ValidatorConfig {
    @Bean
    public LocalValidatorFactoryBean validator() {

        return new LocalValidatorFactoryBean();
    }






}
