package com.furkan.project.auth.service.auth.impl;


import com.furkan.project.auth.dto.request.LoginRequest;
import com.furkan.project.auth.dto.response.JwtResponse;
import com.furkan.project.auth.dto.response.UserResponse;
import com.furkan.project.auth.entity.User;
import com.furkan.project.auth.exception.InvalidCredentialsException;
import com.furkan.project.auth.repository.UserRepository;

import com.furkan.project.auth.security.JwtTokenProvider;
import com.furkan.project.auth.service.auth.IAuthService;
import com.furkan.project.auth.service.user.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;


    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Kullanıcı adı veya şifre hatalı.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Kullanıcı bulunamadı."));

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override
    public JwtResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidCredentialsException("Geçersiz refresh token.");
        }

        String username = jwtTokenProvider.getUsernameFromJwt(refreshToken);

        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Kullanıcı bulunamadı."));

        String newAccessToken = jwtTokenProvider.generateAccessToken(username);

        return new JwtResponse(
                newAccessToken,
                refreshToken,
                "Bearer",
                user.getUsername(),
                user.getEmail()
        );
    }
}
