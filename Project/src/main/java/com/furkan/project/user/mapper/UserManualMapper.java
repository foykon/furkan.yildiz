package com.furkan.project.user.mapper;

import com.furkan.project.user.dto.request.UserCreateRequest;
import com.furkan.project.user.dto.*;
import com.furkan.project.user.dto.request.UserUpdateRequest;
import com.furkan.project.user.dto.response.RoleResponse;
import com.furkan.project.user.dto.response.UserResponse;
import com.furkan.project.user.entity.Role;
import com.furkan.project.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Component
public class UserManualMapper {

    public User toEntity(UserCreateRequest req) {
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setEnabled(true);
        user.setLocked(false);
        return user;
    }

    public void patch(User user, UserUpdateRequest req) {
        if (req.getUsername() != null && !req.getUsername().isBlank()) {
            user.setUsername(req.getUsername());
        }
        if (req.getEmail() != null && !req.getEmail().isBlank()) {
            user.setEmail(req.getEmail());
        }
        if (req.getEnabled() != null) user.setEnabled(req.getEnabled());
        if (req.getLocked() != null) user.setLocked(req.getLocked());
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .locked(user.isLocked())
                .roles(user.getRoles().stream()
                        .map(this::toRoleResponse)
                        .collect(Collectors.toCollection(LinkedHashSet::new)))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private RoleResponse toRoleResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }
}