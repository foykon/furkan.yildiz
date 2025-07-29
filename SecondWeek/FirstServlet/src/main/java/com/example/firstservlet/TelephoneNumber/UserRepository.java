package com.example.firstservlet.TelephoneNumber;

import java.util.ArrayList;
import java.util.List;


public class UserRepository implements IUserRepository {
    private static List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    @Override
    public User addUser(User user) {
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getPhoneNumber().equals(user.getPhoneNumber())) {
                users.set(i, user);
                return user;
            }
        }
        return null;
    }

    @Override
    public User deleteUser(int id) {
        if (id >= 0 && id < users.size()) {
            return users.remove(id);
        }
        return null;
    }

    @Override
    public User getById(int id) {
        if (id >= 0 && id < users.size()) {
            return users.get(id);
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users);
    }
}


