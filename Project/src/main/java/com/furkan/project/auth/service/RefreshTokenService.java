package com.furkan.project.auth.service;

import java.time.Instant;

public interface RefreshTokenService {
    String upsertForUser(Long userId, Instant expiresAt);

    Long validateAndGetUserId(String rawToken);

    void deleteForUser(Long userId);

    void deleteByRawToken(String rawToken);
}