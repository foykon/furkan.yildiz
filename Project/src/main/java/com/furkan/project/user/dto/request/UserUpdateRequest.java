package com.furkan.project.user.dto.request;

import com.furkan.project.user.entity.ERole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @Email
    @Size(max = 255)
    private String email;

    @Size(min = 6, max = 100)
    private String password;

    private Boolean enabled;
    private Boolean locked;

    private Set<ERole> roles;
}
