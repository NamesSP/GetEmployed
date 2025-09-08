package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    private Long recruiterId;   // FK → Users.user_id
    private Long companyId;     // FK → Companies.company_id
    private String position;

    private int requiredYearsExperience;
    private int openings;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private LocalDateTime postedOn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relationship with JobSkill
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<JobSkill> jobSkills = new HashSet<>();




}
