package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "user_profiles")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // Reference to auth-service user (foreign key at DB level,
    // but not mapped as relation here since it's a different service)
    @Column(nullable = true)
    private Long authId;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "FirstName cannot be blank")
    private String firstName;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "LastName cannot be blank")
    private String lastName;

    // ---- Relationships ----
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserSkills> userSkills;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Experience> experiences;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Applications> applications;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Recruiters recruiter;

    // ---- Derived fields (not stored in DB) ----
    @Transient
    private String username; // fetched from auth-service

    @Transient
    private String role; // fetched from auth-service

}
