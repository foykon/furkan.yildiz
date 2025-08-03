package com.furkan.project.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Username boş olamaz")
    @Size(min = 3, max = 20, message = "Username 3 ile 20 karakter arasında olmalı")
    private String username;

    @NotBlank(message = "Password boş olamaz")
    @Size(min = 6, max = 40, message = "Password 6 ile 40 karakter arasında olmalı")
    private String password;
}