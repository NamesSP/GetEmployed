package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Entity
@Table(name = "skills")
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;

    @Column(nullable = false, length = 100, unique = true)
    @NotBlank(message = "Skill name cannot be blank")
    private String skillName;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    
}
