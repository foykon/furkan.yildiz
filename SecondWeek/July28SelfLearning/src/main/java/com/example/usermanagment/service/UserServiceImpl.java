package com.example.usermanagment.service;

import com.example.usermanagment.model.User;
import com.example.usermanagment.repository.IUserRepository;
import com.example.usermanagment.repository.UserRepository;


import java.util.List;

public class UserServiceImpl implements UserService {
    private final IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        if (user.getName() == null || user.getSurname() == null) {
            throw new IllegalArgumentException("Kullanıcı adı ve e-posta boş olamaz");
        }
        userRepository.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        if (user.getId() <= 0) {
            throw new IllegalArgumentException("Geçersiz kullanıcı ID");
        }
        User existingUser = userRepository.getById(user.getId());
        if (existingUser == null) {
            throw new IllegalStateException("Güncellenmek istenen kullanıcı bulunamadı.");
        }
        userRepository.updateUser(user);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteUser(id);
    }

    @Override
    public List<User> getAll() throws ClassNotFoundException {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }
}
