package com.example.July4SefLearning.repository.impl;

import com.example.July4SefLearning.entity.User;
import com.example.July4SefLearning.repository.IUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository implements IUserRepository {
    private List<User> users;

    public UserRepository(List<User> users) {
        this.users = users;
    }

    @Override
    public void add(User user) {
        users.add(user);
    }

    @Override
    public List<User> getAll() {
        return users;
    }
}
