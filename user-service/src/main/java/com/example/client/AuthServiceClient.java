package com.example.client;


import com.example.dto.AuthUserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", path = "/api/auth")
public interface AuthServiceClient {
    @GetMapping(value = "/user-info/{username}", produces = "application/json")
    AuthUserInfoDto getUserInfoByUsername(@PathVariable("username") String username);

    @GetMapping(value = "/users/{id}/exists", produces = "application/json")
    Boolean userExists(@PathVariable("id") Long id);

//    @GetMapping(value = "/users/{id}/info", produces = "application/json")
//    AuthUserInfoDto getUserInfoById(@PathVariable("id") Long id);

    @GetMapping(value = "/users/info/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    AuthUserInfoDto getUserInfoById(@PathVariable("id") Long id);

}


