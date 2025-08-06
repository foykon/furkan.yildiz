package com.furkan.project.auth.service.auth.impl;


import com.furkan.project.auth.dto.request.LoginRequest;
import com.furkan.project.auth.dto.request.RegisterRequest;
import com.furkan.project.auth.dto.request.UserRequest;
import com.furkan.project.auth.dto.response.JwtResponse;
import com.furkan.project.auth.dto.response.RegisterResponse;
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
import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.SuccessDataResult;
import com.furkan.project.common.service.MessageService;
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
    private final MessageService messageService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public DataResult<RegisterResponse> registerUser(RegisterRequest registerRequest) {
        UserRequest userRequest = UserRequest.builder()
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .email(registerRequest.getEmail())
                .build();

        UserResponse userResponse = userService.createUser(userRequest).getData();

        RegisterResponse registerResponse = RegisterResponse.builder()
                .id(userResponse.getId())
                .username(userResponse.getUsername())
                .email(userResponse.getEmail())
                .isEnabled(userResponse.getIsEnabled())
                .roles(userResponse.getRoles())
                .build();

        return new SuccessDataResult<>(registerResponse, messageService.get("user.created"));

    }
    @Override
    public DataResult<JwtResponse> login(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException(messageService.get("login.invalid"));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException(messageService.get("user.notfound")));

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        return new SuccessDataResult<>(JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .build(), messageService.get("login.success"));

    }

    @Override
    public DataResult<JwtResponse> refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidCredentialsException(messageService.get("token.invalid"));
        }

        String username = jwtTokenProvider.getUsernameFromJwt(refreshToken);

        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException(messageService.get("user.notfound")));

        String newAccessToken = jwtTokenProvider.generateAccessToken(username);

        return new SuccessDataResult<>(new JwtResponse(
                newAccessToken,
                refreshToken,
                "Bearer",
                user.getUsername(),
                user.getEmail()
        ), messageService.get("token.refreshed"));
    }
}
