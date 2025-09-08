package com.example.client;

import com.example.dto.AuthUserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", path = "/api/auth")
public interface AuthServiceClient {

    @GetMapping("/user-info/{username}")
    AuthUserInfoDto getUserInfoByUsername(@PathVariable("username") String username);
}


