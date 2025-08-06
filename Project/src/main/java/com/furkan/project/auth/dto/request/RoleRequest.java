package com.furkan.project.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RoleRequest {
    @NotBlank(message = "Name cannot be empty!")
    private String name;
}
