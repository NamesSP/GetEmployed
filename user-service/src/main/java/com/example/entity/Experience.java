package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "experience")
@Data
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experienceId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Company name cannot be blank")
    private String companyName;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Position cannot be blank")
    private String position;

    @Column(nullable = false)
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    // ---- Relationships ----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;
}
