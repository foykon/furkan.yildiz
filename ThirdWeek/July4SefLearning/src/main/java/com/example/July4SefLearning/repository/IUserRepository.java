package com.example.July4SefLearning.repository;


import com.example.July4SefLearning.entity.User;

import java.util.List;

public interface IUserRepository {

    void add(User user);

    List<User> getAll();
}
