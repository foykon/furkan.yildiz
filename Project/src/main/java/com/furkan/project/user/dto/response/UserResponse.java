package com.furkan.project.user.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private boolean locked;
    private Set<RoleResponse> roles;

    private Instant createdAt;
    private Instant updatedAt;
}
