package com.example.usermanagment.service;

import com.example.usermanagment.model.User;

import java.util.List;

public interface UserService {

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(int id);

    List<User> getAll() throws ClassNotFoundException;

    User getById(int id);
}
