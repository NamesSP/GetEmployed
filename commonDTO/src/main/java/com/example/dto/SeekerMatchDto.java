package com.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class SeekerMatchDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String role;
    private List<Long> matchingSkillIds;
    private long totalExperienceYears;
}
