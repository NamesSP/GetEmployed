package com.example.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long authId;
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String roles;
}
