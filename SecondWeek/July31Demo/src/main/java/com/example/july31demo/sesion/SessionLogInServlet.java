package com.example.july31demo.sesion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class SessionLogInServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if ("furkan".equals(username) && "1234".equals(password)) {
            HttpSession session = req.getSession(true);
            System.out.println("New login. ID: " + session.getId());
            session.setAttribute("username", username);
            resp.sendRedirect("welcome.jsp");
        } else {
            req.setAttribute("error", "Invalid credentials!");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }


    }
}
