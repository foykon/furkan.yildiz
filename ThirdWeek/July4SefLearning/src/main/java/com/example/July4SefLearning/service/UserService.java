package com.example.July4SefLearning.service;

import com.example.July4SefLearning.dto.UserDTO;
import java.util.List;

public interface UserService {
    UserDTO getById(int id);
    List<UserDTO> getAll();
    void add(UserDTO userDTO);
}
