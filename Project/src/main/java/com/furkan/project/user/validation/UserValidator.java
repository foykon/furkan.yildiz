package com.furkan.project.user.validation;


import com.furkan.project.user.dto.request.UserCreateRequest;
import com.furkan.project.user.dto.request.UserUpdateRequest;
import com.furkan.project.user.entity.User;
import com.furkan.project.user.exception.DuplicateResourceException;
import com.furkan.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateCreate(UserCreateRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
    }

    public void validateUpdate(User current, UserUpdateRequest req) {
        if (req.getUsername() != null && !req.getUsername().isBlank()) {
            String newUsername = req.getUsername();
            if (!newUsername.equals(current.getUsername()) &&
                    userRepository.existsByUsername(newUsername)) {
                throw new DuplicateResourceException("Username already exists");
            }
        }
        if (req.getEmail() != null && !req.getEmail().isBlank()) {
            String newEmail = req.getEmail();
            if (!newEmail.equals(current.getEmail()) &&
                    userRepository.existsByEmail(newEmail)) {
                throw new DuplicateResourceException("Email already exists");
            }
        }
    }
}