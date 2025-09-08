package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Data
@Table(name = "experience")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "user id is required")
    private Long userId;

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "Company name is required")
    private String company;

    @NotNull(message = "Start date is required")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    @PastOrPresent(message = "End date cannot be in the future")
    private LocalDate endDate;

    @Min(value = 0, message = "Duration cannot be negative")
    private long duration;
    private boolean currentlyWorking;

    @Column(length = 2000)
    private String description;

    @PrePersist
    @PreUpdate
    public void calcDuration() {
        if (currentlyWorking) {
            // If still working, calculate duration till today
            this.duration = Period.between(startDate, LocalDate.now()).getYears();
        } else {
            if (endDate == null) {
                throw new IllegalArgumentException("End date is required if not currently working");
            }
            this.duration = Period.between(startDate, endDate).getYears();
        }
    }

}
