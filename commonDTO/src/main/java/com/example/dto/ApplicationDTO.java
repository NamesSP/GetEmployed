package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDTO {
    private Long applicationId;
    private Integer statusId;
    private String statusName;
    private Long userId;     // add this
    private Long jobId;
}
