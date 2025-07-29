package com.example.july29demo.Login;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {
    HashMap<String, String> users = new HashMap<>();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        users.put(username, password);


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Map<String, String> userMap = new HashMap<>();
        userMap.put("furkan", "1234");
        userMap.put("admin", "adminpass");

        if (username == null || password == null || !userMap.containsKey(username) || !userMap.get(username).equals(password)) {

            System.out.println("Redirect to page /error");
            resp.sendRedirect("/error");
            return;
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("/welcome");
        System.out.println("Redirect to page /welcome");
        dispatcher.forward(req, resp);
    }
}
