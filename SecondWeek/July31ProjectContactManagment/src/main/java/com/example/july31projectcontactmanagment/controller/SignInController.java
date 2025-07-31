package com.example.july31projectcontactmanagment.controller;

import com.example.july31projectcontactmanagment.entities.User;
import com.example.july31projectcontactmanagment.repository.user.IUserRepository;
import com.example.july31projectcontactmanagment.repository.user.UserRepository;
import com.example.july31projectcontactmanagment.service.signin.ISignInService;
import com.example.july31projectcontactmanagment.service.signin.SignInService;
import com.example.july31projectcontactmanagment.service.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/signin")
public class SignInController extends HttpServlet {

    private IUserRepository userRepository;
    private UserService userService;
    private ISignInService signInService;

    @Override
    public void init() throws ServletException {
        userRepository = new UserRepository();
        userService = new UserService(userRepository);
        signInService = new SignInService(userService);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/signin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "Kullanıcı adı ve şifre boş olamaz.");
            req.getRequestDispatcher("/WEB-INF/signin.jsp").forward(req, resp);
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        String error = signInService.signIn(newUser);

        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("/WEB-INF/signin.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
