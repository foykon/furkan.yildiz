package com.furkan.project.auth.service.auth.impl;

import com.furkan.project.auth.dto.request.LoginRequest;
import com.furkan.project.auth.dto.request.RegisterRequest;
import com.furkan.project.auth.dto.request.UserRequest;
import com.furkan.project.auth.dto.response.JwtResponse;
import com.furkan.project.auth.dto.response.RegisterResponse;
import com.furkan.project.auth.dto.response.UserResponse;
import com.furkan.project.auth.entity.RefreshToken;
import com.furkan.project.auth.entity.User;
import com.furkan.project.auth.exception.InvalidCredentialsException;
import com.furkan.project.auth.repository.UserRepository;
import com.furkan.project.auth.security.JwtTokenProvider;
import com.furkan.project.auth.service.auth.IAuthService;
import com.furkan.project.auth.service.token.RefreshTokenService;
import com.furkan.project.auth.service.token.RefreshTokenServiceImpl;
import com.furkan.project.auth.service.user.impl.UserService;
import com.furkan.project.common.result.DataResult;
import com.furkan.project.common.result.Result;
import com.furkan.project.common.result.SuccessDataResult;
import com.furkan.project.common.result.SuccessResult;
import com.furkan.project.common.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final MessageService messageService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;


    @Override
    public DataResult<RegisterResponse> registerUser(RegisterRequest registerRequest) {
        UserRequest userRequest = mapToUserRequest(registerRequest);
        UserResponse userResponse = userService.createUser(userRequest).getData();
        RegisterResponse response = mapToRegisterResponse(userResponse);
        return new SuccessDataResult<>(response, messageService.get("user.created"));
    }

    @Override
    public DataResult<JwtResponse> login(LoginRequest request) {
        authenticate(request.getUsername(), request.getPassword());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException(messageService.get("user.notfound")));

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());

        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        JwtResponse response = buildJwtResponse(user, accessToken, refreshToken);

        return new SuccessDataResult<>(response, messageService.get("login.success"));
    }


    @Override
    public DataResult<JwtResponse> refreshToken(String refreshToken) {
        RefreshToken token = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidCredentialsException(messageService.get("token.invalid")));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidCredentialsException(messageService.get("token.expired"));
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(token.getUser().getUsername());

        JwtResponse response = JwtResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(token.getToken())
                .tokenType("Bearer")
                .username(token.getUser().getUsername())
                .email(token.getUser().getEmail())
                .build();

        return new SuccessDataResult<>(response, messageService.get("token.refreshed"));
    }

    @Override
    public Result logout(String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
        return new SuccessResult("Logout successful, token invalidated.");
    }

    private void authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new InvalidCredentialsException(messageService.get("login.invalid"));
        }
    }

    private UserRequest mapToUserRequest(RegisterRequest request) {
        return UserRequest.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .build();
    }

    private RegisterResponse mapToRegisterResponse(UserResponse user) {
        return RegisterResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isEnabled(user.getIsEnabled())
                .roles(user.getRoles())
                .build();
    }

    private JwtResponse buildJwtResponse(User user, String accessToken, String refreshToken) {
        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
