package com.example.july31projectcontactmanagment.service.login;

import com.example.july31projectcontactmanagment.entities.User;

public interface ILoginService {
    User login(String username, String password);
}
