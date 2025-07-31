package com.example.july31projectcontactmanagment.service.user;

import com.example.july31projectcontactmanagment.entities.User;

import java.util.List;

public interface IUserService {
    User getUserById(int id);
    List<User> getAllUsers();
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    User getUserByUsername(String username);
}
