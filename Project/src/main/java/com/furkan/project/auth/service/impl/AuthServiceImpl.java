package com.furkan.project.auth.service.impl;

import com.furkan.project.auth.dto.request.LoginRequest;
import com.furkan.project.auth.dto.request.SignUpRequest;
import com.furkan.project.auth.dto.response.AuthResponse;
import com.furkan.project.auth.jwt.JwtTokenProvider;
import com.furkan.project.auth.service.AuthService;
import com.furkan.project.auth.service.RefreshCookieService;
import com.furkan.project.auth.service.RefreshTokenService;
import com.furkan.project.common.logging.LogExecution;
import com.furkan.project.user.entity.ERole;
import com.furkan.project.user.entity.Role;
import com.furkan.project.user.entity.User;
import com.furkan.project.user.repository.RoleRepository;
import com.furkan.project.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashSet;

@Service
@RequiredArgsConstructor
@Transactional
@LogExecution

public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwt;
    private final UserRepository userRepository;
private final RoleRepository roleRepository;
    private final RefreshTokenService refreshTokens;
    private final RefreshCookieService refreshCookies;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.refresh.expiration-sec:1209600}") // 14 gün
    private long refreshExpSec;

    @Override
    public AuthResponse login(LoginRequest req, HttpServletResponse res) {
        authenticate(req.getUsername(), req.getPassword());
        var user = loadUser(req.getUsername());

        var rawRefresh = refreshTokens.upsertForUser(user.getId(),
                Instant.now().plusSeconds(refreshExpSec));
        refreshCookies.set(res, rawRefresh, Duration.ofSeconds(refreshExpSec));

        var roles = rolesOf(user);
        String access = jwt.generateToken(user.getId(), user.getUsername(), roles);

        return AuthResponse.builder()
                .token(access)
                .expiresIn(jwt.getExpirationSec())
                .user(null)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse refresh(HttpServletRequest req, HttpServletResponse res) {
        String raw = refreshCookies.read(req);
        Long userId = refreshTokens.validateAndGetUserId(raw);

        var user = userRepository.findWithRolesByUsername(
                userRepository.findById(userId).orElseThrow().getUsername()
        ).orElseThrow();

        String access = jwt.generateToken(userId, user.getUsername(), rolesOf(user));

        refreshCookies.set(res, raw, Duration.ofSeconds(remainingTtl(userId)));

        return AuthResponse.builder()
                .token(access)
                .expiresIn(jwt.getExpirationSec())
                .user(null)
                .build();
    }

    @Override
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        String raw = refreshCookies.read(req);
        refreshTokens.deleteByRawToken(raw);
        refreshCookies.clear(res);
    }

    @Override
    public void signUp(SignUpRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new IllegalArgumentException("auth.signup.username.taken");
        }
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("auth.signup.email.taken");
        }

        var user = new User();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setEnabled(false);
        user.setLocked(false);
        user.setDeleted(false);

        var roleUser = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("role.ROLE_USER.missing"));
        var roles = new LinkedHashSet<Role>();
        roles.add(roleUser);
        user.setRoles(roles);

        userRepository.save(user);
        // TODO: email verification
    }

    /* ------- helpers ------- */
    private void authenticate(String u, String p) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(u, p));
    }
    private com.furkan.project.user.entity.User loadUser(String username) {
        return userRepository.findWithRolesByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("auth.user.notfound"));
    }
    private java.util.Set<String> rolesOf(com.furkan.project.user.entity.User u) {
        return u.getRoles().stream().map(r -> r.getName().name()).collect(java.util.stream.Collectors.toSet());
    }
    private long remainingTtl(Long userId) {
        return refreshExpSec;
    }
}
