package com.example.july30demo.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {
    private LoginService loginService;
    @Override
    public void init() throws ServletException {
        super.init();
        loginService = new LoginService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        String randomValue = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("randomNumber".equals(cookie.getName())) {
                    randomValue = cookie.getValue();
                    break;
                }
            }
        }

        if (randomValue != null && loginService.isLogIn("furkan", randomValue)) {
            req.getRequestDispatcher("/welcome.jsp").forward(req, resp);
        }else{
            resp.sendRedirect("/login");

        }

    }
}
