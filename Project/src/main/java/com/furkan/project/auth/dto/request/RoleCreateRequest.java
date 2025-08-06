package com.furkan.project.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleCreateRequest {

    @NotBlank(message = "Name cannot be empty!")
    private String name;
}
