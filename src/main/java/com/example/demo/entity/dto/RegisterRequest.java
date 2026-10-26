package com.example.demo.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "username can't be empty")
    private String username;

    @NotBlank(message = "email wajib diisi")
    @Email(message = "format email salah")
    private String email;

    @NotBlank(message = "password can't be empty")
    private String password;
}
