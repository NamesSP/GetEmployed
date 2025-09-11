package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "recruiters")
@Data
public class Recruiters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiter_id")
    private Long recruiterId;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

}
