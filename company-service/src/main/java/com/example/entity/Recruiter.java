package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;


@Data
@Entity
@Table(name="recruiter")
public class Recruiter {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long recruiterId;

    private String recruiterName;

    private String email;

    @ManyToOne
    @JoinColumn(name = "companyId", referencedColumnName = "companyId", nullable = false)
    private Company company;

}
