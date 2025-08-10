package com.furkan.project.user.util;
import com.furkan.project.user.dto.request.UserFilterRequest;
import com.furkan.project.user.dto.request.UserCreateRequest;
import com.furkan.project.user.dto.request.UserUpdateRequest;

public final class InputNormalizer {
    private InputNormalizer() {}

    public static void normalize(UserCreateRequest r) {
        if (r == null) return;
        if (r.getUsername() != null) r.setUsername(r.getUsername().trim());
        if (r.getEmail() != null) r.setEmail(r.getEmail().trim().toLowerCase());
    }

    public static void normalize(UserUpdateRequest r) {
        if (r == null) return;
        if (r.getUsername() != null) r.setUsername(r.getUsername().trim());
        if (r.getEmail() != null) r.setEmail(r.getEmail().trim().toLowerCase());
    }

    public static UserFilterRequest normalize(UserFilterRequest r) {
        if (r == null) return null;
        if (r.getUsername() != null) r.setUsername(r.getUsername().trim());
        if (r.getEmail() != null) r.setEmail(r.getEmail().trim().toLowerCase());
        if (r.getSortBy() != null) r.setSortBy(r.getSortBy().trim());
        if (r.getSortDirection() != null) r.setSortDirection(r.getSortDirection().trim());
        return r;
    }
}