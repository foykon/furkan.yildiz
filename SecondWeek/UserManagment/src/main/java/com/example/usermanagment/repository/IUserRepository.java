package com.example.usermanagment.repository;

import com.example.usermanagment.model.User;

import java.util.List;

public interface IUserRepository {

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(int id);

    List<User> getAll() throws ClassNotFoundException;

    User getById(int id);

}
