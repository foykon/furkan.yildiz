package com.furkan.project.auth.service.impl;

import com.furkan.project.auth.dto.request.ForgotPasswordRequest;
import com.furkan.project.auth.dto.request.ResetPasswordRequest;
import com.furkan.project.auth.entity.ResetPasswordToken;
import com.furkan.project.auth.events.ResetPasswordEmailEvent;
import com.furkan.project.auth.repository.ResetPasswordTokenRepository;
import com.furkan.project.common.result.Result;
import com.furkan.project.common.result.SuccessResult;
import com.furkan.project.common.service.MessageService;
import com.furkan.project.user.entity.User;
import com.furkan.project.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordResetServiceImpl implements com.furkan.project.auth.service.PasswordResetService {

    private final UserRepository users;
    private final ResetPasswordTokenRepository tokens;
    private final MessageService msg;
    private final PasswordEncoder encoder;
    private final ApplicationEventPublisher events;

    @Value("${app.security.reset.token-ttl-minutes:30}")
    private int ttlMinutes;

    @Value("${app.security.reset.base-url:http://localhost:5173/reset-password}")
    private String resetBaseUrl;

    @Value("${app.security.reset.max-active-per-user:3}")
    private int maxActivePerUser;

    private static final SecureRandom RNG = new SecureRandom();

    @Override
    public Result forgot(ForgotPasswordRequest req, HttpServletRequest http) {
        Optional<User> opt = users.findByEmail(req.getEmail());

        opt.ifPresent(user -> {
            long active = tokens.countByUserIdAndUsedAtIsNullAndExpiresAtAfter(user.getId(), Instant.now());
            if (active >= maxActivePerUser) return;

            String token = generateToken();
            String tokenHash = hash(token);
            Instant exp = Instant.now().plusSeconds(ttlMinutes * 60L);

            tokens.save(
                    ResetPasswordToken.builder()
                            .userId(user.getId())
                            .tokenHash(tokenHash)
                            .expiresAt(exp)
                            .ip(clientIp(http))
                            .userAgent(http.getHeader("User-Agent"))
                            .build()
            );

            String link = resetBaseUrl + "?token=" + token;
            events.publishEvent(new ResetPasswordEmailEvent(
                    user.getId(), user.getEmail(), user.getUsername(), link
            ));
        });

        return new SuccessResult(msg.get("auth.reset.forgot.accepted"));
    }

    @Override
    public Result reset(ResetPasswordRequest req) {
        String tokenHash = hash(req.getToken());

        ResetPasswordToken t = tokens.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException(msg.get("auth.reset.token.invalid")));

        if (t.isUsed())    throw new IllegalStateException(msg.get("auth.reset.token.used"));
        if (t.isExpired()) throw new IllegalStateException(msg.get("auth.reset.token.expired"));

        User u = users.findById(t.getUserId())
                .orElseThrow(() -> new IllegalStateException(msg.get("auth.reset.user.not_found")));

        u.setPassword(encoder.encode(req.getNewPassword()));
        users.save(u);

        t.setUsedAt(Instant.now());
        tokens.save(t);

        return new SuccessResult(msg.get("auth.reset.password.changed"));
    }



    private static String generateToken() {
        byte[] buf = new byte[32];
        RNG.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }

    private static String hash(String token) {
        try {
            var md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(token.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("hash_failed", e);
        }
    }

    private static String clientIp(HttpServletRequest req) {
        String h = req.getHeader("X-Forwarded-For");
        if (h != null && !h.isBlank()) return h.split(",")[0].trim();
        return req.getRemoteAddr();
    }
}
