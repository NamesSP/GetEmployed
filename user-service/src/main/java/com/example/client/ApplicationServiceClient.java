package com.example.client;

import com.example.dto.ApplicationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "application-service")
public interface ApplicationServiceClient {
    @PostMapping("/applications")
    ApplicationDTO createApplication(@RequestBody ApplicationDTO application);
}
