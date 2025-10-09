package com.example.client;

import com.example.dto.JobDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "job-service")
public interface JobserviceClient {
    @GetMapping("/jobs/{id}")
    JobDto getJobById(@PathVariable("id") Long id);
}
