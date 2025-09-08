package com.example.entity;

import com.example.Validation.Adult;
import com.example.Validation.UniqueEmail;
import com.example.Validation.UniqueUserName;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Table(name="User")
@Data
public class UserEntity {

    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long userid;
//
//    @NotBlank(message = "Username cannot be blank")
//    @Column(nullable = false, unique = true)
//    @UniqueUserName
//    private String userName;
//
//    @NotBlank(message = "FirstName cannot be blank")
//    @Column(nullable = false)
//    private String firstName;
//
//    @NotBlank(message = "LastName cannot be blank")
//    @Column(nullable = false)
//    private String lastName;
//
//    @Email(message = "Email should be Valid")
//    @NotBlank(message = "Email is required")
//    @Column(nullable = false, unique = true)
//    @UniqueEmail
//    private String email;
//
//    @NotNull(message = "Date Of Birth is required")
//    @Adult
//    private LocalDate dob;
//
//    @Column(nullable = false)
//    private int age;
//
//    @NotNull(message = "Role must be specified")
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;
//
//    @Column(nullable = false)
//    private LocalDateTime createdAt;
//    @Column(nullable = false)
//    private LocalDateTime updatedAt;
//
//    public enum Role{
//        SEEKER,
//        RECRUITER
//    }
//
//    @PrePersist
//    protected void onCreate(){
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//        this.age= Period.between(dob,LocalDate.now()).getYears();
//    }
//
//    @PreUpdate
//    protected void onUpdate(){
//        this.updatedAt = LocalDateTime.now();
//    }
    //---------------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // Reference to auth-service user (foreign key at DB level,
    // but not mapped as relation here since it's a different service)
    @Column(nullable = false)
    private Long authId;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "FirstName cannot be blank")
    private String firstName;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "LastName cannot be blank")
    private String lastName;

    @Column(nullable = false)
    @NotNull(message = "Date Of Birth is required")
    private LocalDate dob;

    @Column(nullable = false)
    private String role; // pulled from auth-service when needed

    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    @Column(nullable = false)
    private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();

    // ---- Derived fields (not stored in DB) ----
    @Transient
    private Integer age;

    @Transient
    private String username; // fetched from auth-service

    @PostLoad
    @PostPersist
    @PostUpdate
    public void calculateAge() {
        if (this.dob != null) {
            this.age = Period.between(this.dob, LocalDate.now()).getYears();
        }
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
