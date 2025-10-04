package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableFeignClients
public class ExperienceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExperienceServiceApplication.class, args);
	}

}
