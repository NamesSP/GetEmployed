package com.example.entity;

import jakarta.persistence.*;
import lombok.*;



@Data
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long companyId;

    @Column(nullable = false, unique = true)
    private String companyName;

    @Column(nullable = false)
    private String location;
}
