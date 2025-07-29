package com.example.july29demo.OriginAndTarget;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet( value = "/target")
public class TargetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Request comes to TargetServlet");

        resp.getWriter().println(req.getAttribute("atrb1"));
        resp.getWriter().println(req.getAttribute("atrb2"));
        resp.getWriter().println(req.getAttribute("atrb3"));

        resp.getWriter().println(getServletContext().getAttribute("atrb1"));
        resp.getWriter().println(getServletContext().getAttribute("atrb2"));
        resp.getWriter().println(getServletContext().getAttribute("atrb3"));

    }
}
