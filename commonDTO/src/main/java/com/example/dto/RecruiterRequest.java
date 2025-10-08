package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterRequest {
    private Long recruiterId;
    private String recruiterName;
    private String email;
    private Long companyId;
}
