
package com.example.client;

import com.example.dto.CompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service", path = "/companies")
public interface CompanyClient {

    @GetMapping("/{id}")
    CompanyDto getCompanyById(@PathVariable("id") Long id);
}
