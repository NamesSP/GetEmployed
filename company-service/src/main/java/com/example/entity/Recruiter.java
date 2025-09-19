package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "recruiter")
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recruiterId;

    private String recruiterName;

    private String email;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private Company company;
}
