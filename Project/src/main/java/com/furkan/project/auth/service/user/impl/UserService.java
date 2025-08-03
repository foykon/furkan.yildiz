package com.furkan.project.auth.service.user.impl;

import com.furkan.project.auth.dto.response.UserResponse;
import com.furkan.project.auth.entity.ERole;
import com.furkan.project.auth.entity.Role;
import com.furkan.project.auth.entity.User;
import com.furkan.project.auth.exception.UserAlreadyExistsException;
import com.furkan.project.auth.repository.RoleRepository;
import com.furkan.project.auth.repository.UserRepository;
import com.furkan.project.auth.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
