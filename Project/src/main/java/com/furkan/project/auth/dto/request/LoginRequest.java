package com.furkan.project.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Username cannot be empty!")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters!")
    private String username;

    @NotBlank(message = "Password cannot be empty!")
    @Size(min = 6, max = 40, message = "Username must be between 6 and 40 characters")
    private String password;
}