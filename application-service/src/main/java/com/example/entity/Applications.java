package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Data
public class Applications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // @Column(nullable = false)
    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedOn = LocalDateTime.now();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();


    public enum ApplicationStatus {
        APPLIED, HIRED, REJECTED
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
