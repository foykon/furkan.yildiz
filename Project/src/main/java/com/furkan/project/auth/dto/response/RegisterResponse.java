package com.furkan.project.auth.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RegisterResponse {
    private Long id;
    private String username;
    private String email;
    private Boolean isEnabled;
    private Set<RoleResponse> roles;
}
