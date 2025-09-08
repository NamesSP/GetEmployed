package com.example.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service", url = "http://localhost:8082")
public interface CompanyServiceClient {

    @GetMapping("/api/companies/{id}")
    String getCompanyById(@PathVariable("id") Long id);
}
