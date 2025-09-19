package com.example.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;


@Data
public class JobDto {

    private Long id;
    private String title;
    private String description;
    private Long companyId;
    private CompanyDto company;
    private LocalDate postedOn;
    private List<Long> skillIds;
    private Long recruiterId;

    private Integer requiredYearsExperience;
    private Integer openings;
    private JobStatus status;

}