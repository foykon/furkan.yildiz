package com.furkan.project.auth.service.token;

import com.furkan.project.auth.entity.RefreshToken;
import com.furkan.project.auth.entity.User;

import java.util.Optional;

public interface RefreshTokenService {
    String createRefreshToken(String username);
    void verifyExpiration(String token);
    User getUserFromToken(String token);
    void deleteByUser(User user);
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
}
