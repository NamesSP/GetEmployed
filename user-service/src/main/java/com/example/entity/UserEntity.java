package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "user_profiles")
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // Reference to auth-service user (foreign key at DB level,
    // but not mapped as relation here since it's a different service)
    @Column(nullable = true, unique = true)
    private Long authId;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "FirstName cannot be blank")
    private String firstName;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "LastName cannot be blank")
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // ---- Denormalized fields from auth-service for convenience ----
    @Column(nullable = true, length = 100)
    private String username; // fetched from auth-service

    @Column(nullable = true, length = 50)
    private String role; // fetched from auth-service

}
