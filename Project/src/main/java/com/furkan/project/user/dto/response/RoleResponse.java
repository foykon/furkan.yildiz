package com.furkan.project.user.dto.response;

import com.furkan.project.user.entity.ERole;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {
    private Long id;
    private ERole name;
    private String description;
}
