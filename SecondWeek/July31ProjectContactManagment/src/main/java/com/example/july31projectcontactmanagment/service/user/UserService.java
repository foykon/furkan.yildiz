package com.example.july31projectcontactmanagment.service.user;

import com.example.july31projectcontactmanagment.entities.User;
import com.example.july31projectcontactmanagment.repository.user.IUserRepository;
import com.example.july31projectcontactmanagment.repository.user.UserRepository;

import java.util.List;

public class UserService implements IUserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getById( id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    public void addUser(User user) {
        userRepository.add(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.update(user);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.delete( id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}