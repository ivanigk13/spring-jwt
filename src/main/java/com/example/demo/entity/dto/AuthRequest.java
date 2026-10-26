package com.example.demo.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank(message = "username can't be empty")
    private String username;

    @NotBlank(message = "password can't be empty")
    private String password;
}
