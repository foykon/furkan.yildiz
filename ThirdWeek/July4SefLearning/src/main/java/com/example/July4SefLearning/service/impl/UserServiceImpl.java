package com.example.July4SefLearning.service.impl;

import com.example.July4SefLearning.dto.UserDTO;
import com.example.July4SefLearning.entity.User;
import com.example.July4SefLearning.repository.IUserRepository;
import com.example.July4SefLearning.repository.impl.UserRepository;
import com.example.July4SefLearning.service.UserService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.ILoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final IUserRepository userRepository;

    @Override
    public UserDTO getById(int id) {
        log.info("finding user: " + id);
        return userRepository.getAll()
                .stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .map(user -> UserDTO.builder()
                        .name(user.getName())
                        .id(user.getId())
                        .build())
                .orElse(null);
    }

    @Override
    public List<UserDTO> getAll() {
        log.info("All users gathering");

        return userRepository.getAll()
                .stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void add(UserDTO userDTO) {

        log.info("Adding user: " + userDTO.toString());
        if(userDTO.getName() == null || userDTO.getName().isEmpty()) return;

        userRepository.add(User.builder()
                        .id(userDTO.getId())
                        .name(userDTO.getName())
                        .build());
    }
}
