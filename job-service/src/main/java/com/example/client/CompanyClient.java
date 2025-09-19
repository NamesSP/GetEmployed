
package com.example.client;

import com.example.dto.CompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service", url = "http://localhost:8082")
public interface CompanyClient {

    @GetMapping("/companies/{id}")
    CompanyDto getCompanyById(@PathVariable("id") Long id);
}
