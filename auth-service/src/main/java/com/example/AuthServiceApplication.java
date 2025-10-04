package com.example;

import com.example.dto.Role;
import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (!userRepository.existsByRole(Role.ADMIN)) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setEmail("admin@getemployed.local");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole(Role.ADMIN);
				userRepository.save(admin);
			}
		};
	}

}
