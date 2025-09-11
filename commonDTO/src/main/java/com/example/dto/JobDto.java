
package com.example.dto;

import lombok.Data;

@Data
public class JobDto {

    private Long id;
    private String title;
    private String description;
    private Long companyId;
    private CompanyDto company;
}
