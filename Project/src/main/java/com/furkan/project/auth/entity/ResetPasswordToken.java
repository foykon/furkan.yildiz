package com.furkan.project.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "auth_reset_tokens",
        indexes = {
                @Index(name="ix_reset_user", columnList="user_id"),
                @Index(name="ix_reset_expires", columnList="expires_at"),
                @Index(name="ix_reset_token_hash", columnList="token_hash", unique = true)
        })
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ResetPasswordToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable=false)
    private Long userId;

    @Column(name="token_hash", length=64, nullable=false, unique=true)
    private String tokenHash;

    @Column(name="expires_at", nullable=false)
    private Instant expiresAt;

    @Column(name="used_at")
    private Instant usedAt;

    @Column(name="ip", length = 64)
    private String ip;

    @Column(name="user_agent", length = 256)
    private String userAgent;

    public boolean isExpired() { return Instant.now().isAfter(expiresAt); }
    public boolean isUsed()    { return usedAt != null; }
}
