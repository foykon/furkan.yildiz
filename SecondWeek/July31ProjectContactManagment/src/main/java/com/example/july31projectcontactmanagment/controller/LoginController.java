package com.example.july31projectcontactmanagment.controller;

import com.example.july31projectcontactmanagment.entities.User;
import com.example.july31projectcontactmanagment.repository.user.IUserRepository;
import com.example.july31projectcontactmanagment.repository.user.UserRepository;
import com.example.july31projectcontactmanagment.service.login.ILoginService;
import com.example.july31projectcontactmanagment.service.login.LoginService;
import com.example.july31projectcontactmanagment.service.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private ILoginService loginService;
    private UserService userService;
    private IUserRepository userRepository;

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        super.service(req, res);
        System.out.println( "Entered Service Method!" + req.getMethod());

    }

    @Override
    public void init() throws ServletException {
        super.init();
        userRepository = new UserRepository();
        userService = new UserService(userRepository);
        loginService = new LoginService(userService);
    }

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp) throws jakarta.servlet.ServletException, java.io.IOException {
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = loginService.login(username, password);

        if (user != null) {
            req.getSession().setAttribute("user", user);
            System.out.println("Kullanıcı adı ve şifre doğru" + user.getUsername());
            resp.sendRedirect(req.getContextPath() + "/contact");
        } else {
            System.out.println("Kullanıcı adı veya şifre yanlış");
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
        }
    }
}
