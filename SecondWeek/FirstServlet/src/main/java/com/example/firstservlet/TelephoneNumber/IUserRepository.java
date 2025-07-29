package com.example.firstservlet.TelephoneNumber;

import java.util.List;

public interface IUserRepository {
    User addUser(User user);

    User updateUser(User user);

    User deleteUser(int id);

    User getById(int id);

    List<User> getAll();


}
