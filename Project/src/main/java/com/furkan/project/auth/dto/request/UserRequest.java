package com.furkan.project.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserRequest {

    @NotBlank(message = "Username cannot be empty!")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters!")
    private String username;

    @NotBlank(message = "Password cannot be empty!")
    @Size(min = 6, max = 40, message = "Username must be between 6 and 40 characters")
    private String password;

    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Email must be valid!")
    private String email;

    private Set<RoleRequest> roles;

}
