package com.example.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienceDto {
    private Long id;
    private Long userId;
    private String title;
    private String company;
    private LocalDate startDate;
    private LocalDate endDate;
    private long duration;
    private boolean currentlyWorking;
    private String description;
}
