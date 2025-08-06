package com.furkan.project.auth.validation;

import com.furkan.project.auth.dto.request.UserRequest;
import com.furkan.project.auth.entity.User;
import com.furkan.project.auth.exception.UserAlreadyExistsException;
import com.furkan.project.auth.exception.UserNotFoundException;
import com.furkan.project.auth.repository.UserRepository;
import com.furkan.project.common.service.MessageService;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    private final UserRepository userRepository;
    private final MessageService messageService;

    public UserValidator(UserRepository userRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    public void validateUserExists(long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(messageService.get("user.notfound"));
        }
    }


    public void validateUserUniqueness(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(messageService.get("username.already-exists"));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(messageService.get("email.already-exists"));
        }
    }

    public void validateUserUpdate(long userId, UserRequest request) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(messageService.get("user.notfound")));

        if (!existingUser.getUsername().equals(request.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(messageService.get("username.already-exists"));
        }

        if (!existingUser.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(messageService.get("email.already-exists"));
        }

    }


}
