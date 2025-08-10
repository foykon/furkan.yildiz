package com.furkan.project.user.dto.request;

import com.furkan.project.user.entity.ERole;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @Builder.Default
    private Set<ERole> roles = Set.of(ERole.ROLE_USER);
}
