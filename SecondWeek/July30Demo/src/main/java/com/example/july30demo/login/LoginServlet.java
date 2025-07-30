package com.example.july30demo.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Random;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final String NAME = "furkan";
    private final String PASSWORD = "pass123";
    private Random random;
    private LoginService loginService;

    @Override
    public void init() throws ServletException {
        super.init();
         random = new Random();
         loginService = new LoginService();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username =req.getParameter("username");
        String password = req.getParameter("password");

        if(username.equals(NAME) && password.equals(PASSWORD)){
            int randomNumber = random.nextInt(1000);
            loginService.login(username, String.valueOf(randomNumber));
            Cookie cookie = new Cookie("randomNumber", String.valueOf(randomNumber));
            cookie.setMaxAge(3600);
            resp.addCookie(cookie);
            resp.sendRedirect("welcome");
        }
        else resp.sendRedirect("login");

    }
}
