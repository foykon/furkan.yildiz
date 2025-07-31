package com.example.july31projectcontactmanagment.service.login;

import com.example.july31projectcontactmanagment.entities.User;
import com.example.july31projectcontactmanagment.service.user.IUserService;

public class LoginService implements ILoginService {

    private final IUserService userService;

    public LoginService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public User login(String username, String password) {
        User user = userService.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}