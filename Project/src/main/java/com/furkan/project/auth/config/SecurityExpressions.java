package com.furkan.project.auth.config;

import com.furkan.project.user.api.UserApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("sec")
@RequiredArgsConstructor
public class SecurityExpressions {
    private final UserApiService userApi;

    public boolean isSelfOrAdmin(Long targetUserId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return false;

        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (admin) return true;

        Long me = userApi.findIdByUsername(authentication.getName());
        return Objects.equals(me, targetUserId);
    }
}

