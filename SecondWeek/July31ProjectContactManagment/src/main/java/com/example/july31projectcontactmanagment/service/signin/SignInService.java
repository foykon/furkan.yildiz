package com.example.july31projectcontactmanagment.service.signin;

import com.example.july31projectcontactmanagment.entities.User;
import com.example.july31projectcontactmanagment.service.user.IUserService;

public class SignInService implements ISignInService {

    private final IUserService userService;

    public SignInService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public String signIn(User user) {
        if (userService.getUserByUsername(user.getUsername()) != null) {
            return "Kullanıcı adı zaten alınmış.";
        }

        userService.addUser(user);
        return null;
    }
}