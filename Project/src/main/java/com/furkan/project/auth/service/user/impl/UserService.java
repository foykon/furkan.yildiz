package com.furkan.project.auth.service.user.impl;

import com.furkan.project.auth.dto.request.UserFilterRequest;
import com.furkan.project.auth.dto.request.UserRequest;
import com.furkan.project.auth.dto.response.UserResponse;
import com.furkan.project.auth.entity.ERole;
import com.furkan.project.auth.entity.Role;
import com.furkan.project.auth.entity.User;
import com.furkan.project.auth.exception.UserNotFoundException;
import com.furkan.project.auth.filter.UserSpecifications;
import com.furkan.project.auth.repository.RoleRepository;
import com.furkan.project.auth.repository.UserRepository;
import com.furkan.project.auth.service.user.IUserService;
import com.furkan.project.auth.validation.UserValidator;
import com.furkan.project.common.result.*;
import com.furkan.project.common.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserValidator userValidator;
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public DataResult<UserResponse> createUser(UserRequest userRequest) {
        userValidator.validateUserUniqueness(userRequest);

        User user = requestToUser(userRequest);

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException(messageService.get("role.not-found")));
        user.getRoles().add(userRole);

        userRepository.save(user);

        return new SuccessDataResult<>(userToUserResponse(user), messageService.get("user.created"));

    }

    @Override
    public DataResult<UserResponse> updateUser(long id, UserRequest request) {
        userValidator.validateUserUpdate(id, request);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(messageService.get("user.notfound")));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return new SuccessDataResult<>(userToUserResponse(user), messageService.get("user.updated"));
    }

    @Override
    public Result deleteUser(long id) {
        userValidator.validateUserExists(id);

        userRepository.deleteById(id);

        return new SuccessResult(messageService.get("user.deleted"));
    }

    @Override
    public DataResult<UserResponse> getUserById(long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(messageService.get("user.notfound")));

        return new SuccessDataResult<>(userToUserResponse(user), messageService.get("user.found"));
    }

    @Override
    public DataResult<List<UserResponse>> getAllUsers(UserFilterRequest userFilterRequest) {
        Specification<User> spec = (root, query, cb) -> cb.conjunction(); // her zaman true
        UserRequest userReq = userFilterRequest.getUserRequest();
        if (userReq != null) {
            if (userReq.getUsername() != null && !userReq.getUsername().isBlank()) {
                spec = spec.and(UserSpecifications.hasUsername(userReq.getUsername()));
            }
            if (userReq.getEmail() != null && !userReq.getEmail().isBlank()) {
                spec = spec.and(UserSpecifications.hasEmail(userReq.getEmail()));
            }
        }
        String sortBy = (userFilterRequest.getSortBy() != null && !userFilterRequest.getSortBy().isBlank())
                ? userFilterRequest.getSortBy()
                : "id";

        String sortDirection = (userFilterRequest.getSortDirection() != null && !userFilterRequest.getSortDirection().isBlank())
                ? userFilterRequest.getSortDirection()
                : "ASC";

        int page = (userFilterRequest.getPage() != null && userFilterRequest.getPage() >= 0)
                ? userFilterRequest.getPage()
                : 0;

        int size = (userFilterRequest.getSize() != null && userFilterRequest.getSize() > 0)
                ? userFilterRequest.getSize()
                : 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));

        Page<User> resultPage = userRepository.findAll(spec, pageable);

        List<UserResponse> responses = resultPage.getContent().stream()
                .map(this::userToUserResponse)
                .collect(Collectors.toList());

        return new PagedDataResult<>(
                responses,
                resultPage.getTotalElements(),
                resultPage.getTotalPages(),
                resultPage.getNumber(),
                resultPage.getSize(),
                messageService.get("user.listed")
        );
    }



    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void assignDefaultRole(User user) {
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException(messageService.get("role.not-found")));
        user.getRoles().add(userRole);
    }

    private User requestToUser(UserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .roles(new HashSet<>())
                .build();
    }

    private UserResponse userToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isEnabled(user.isEnabled())
                .build();
    }

}
