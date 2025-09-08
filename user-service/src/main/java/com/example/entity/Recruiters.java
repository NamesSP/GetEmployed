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

    // ---- Relationships ----
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Companies company;

    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Jobs> jobs;
}
