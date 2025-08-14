package com.furkan.project.auth.repository;

import com.furkan.project.auth.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    Optional<ResetPasswordToken> findByTokenHash(String tokenHash);
    long countByUserIdAndUsedAtIsNullAndExpiresAtAfter(Long userId, java.time.Instant now);
}