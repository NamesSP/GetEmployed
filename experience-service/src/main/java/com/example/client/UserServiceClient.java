package com.example.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", path = "/api/auth")
public interface UserServiceClient {

    @GetMapping(value="/users/{id}/exists",produces = "application/json")
    Boolean userExists(@PathVariable("id") Long userId);
}
