package com.furkan.project.auth.service.impl;

import com.furkan.project.auth.entity.RefreshToken;
import com.furkan.project.auth.repository.RefreshTokenRepository;
import com.furkan.project.auth.service.RefreshTokenService;
import com.furkan.project.auth.util.TokenUtils;
import com.furkan.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

// RefreshTokenServiceImpl.java
@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository repo;
    private final UserRepository userRepo;
    private final Clock clock = Clock.systemUTC();
    @Override
    public String upsertForUser(Long userId, Instant expiresAt) {
        repo.deleteByUserId(userId);
        String raw = TokenUtils.randomToken();
        var rt = RefreshToken.builder()
                .user(userRepo.getReferenceById(userId))
                .tokenHash(TokenUtils.sha256Hex(raw))
                .expiresAt(expiresAt)
                .build();
        repo.save(rt);
        return raw;
    }

    @Override
    @Transactional(readOnly = true)
    public Long validateAndGetUserId(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            throw new IllegalArgumentException("refresh.missing");
        }
        var now = Instant.now(clock);
        var rt = repo.findByTokenHash(TokenUtils.sha256Hex(rawToken))
                .orElseThrow(() -> new IllegalArgumentException("refresh.invalid"));
        if (now.isAfter(rt.getExpiresAt())) {
            throw new IllegalArgumentException("refresh.expired");
        }
        return rt.getUser().getId();
    }

    @Override
    public void deleteForUser(Long userId) {
        repo.deleteByUserId(userId);
    }

    @Override
    public void deleteByRawToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) return;
        repo.findByTokenHash(TokenUtils.sha256Hex(rawToken))
                .ifPresent(rt -> repo.deleteById(rt.getId()));
    }
}

