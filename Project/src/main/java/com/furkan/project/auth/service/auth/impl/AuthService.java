package com.furkan.project.auth.service.auth.impl;


import com.furkan.project.auth.dto.request.LoginRequest;
import com.furkan.project.auth.dto.response.JwtResponse;
import com.furkan.project.auth.dto.response.UserResponse;
import com.furkan.project.auth.entity.ERole;
import com.furkan.project.auth.entity.Role;
import com.furkan.project.auth.entity.User;
import com.furkan.project.auth.exception.InvalidCredentialsException;
import com.furkan.project.auth.exception.UserAlreadyExistsException;
import com.furkan.project.auth.repository.RoleRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;



    @Override
    public UserResponse registerUser(String username, String email, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Kullanıcı adı kullanımda.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email adresi kullanımda.");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        user.getRoles().add(userRole);
        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
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
