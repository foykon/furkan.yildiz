package com.furkan.project.user.api.impl;

import com.furkan.project.user.api.UserApiService;
import com.furkan.project.user.entity.User;
import com.furkan.project.user.exception.UserNotFoundException;
import com.furkan.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserApiServiceImpl implements UserApiService {

    private final UserRepository userRepository;

    @Override
    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public Long findIdByUsername(String username) {
        return userRepository.findByUsername(username).map(User::getId).orElse(null);
    }

    @Override
    public String getUserNameById(Long id) {
        String username = userRepository.findById(id).get().getUsername();
        if(username == null) {
            throw new UserNotFoundException("user.notFound");
        }
        return username;
    }
}
