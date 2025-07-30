package com.example.july30demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet("/counter")
public class CounterServlet extends HttpServlet {
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int updated = counter.incrementAndGet(); // Thread-safe
        response.setContentType("text/plain");
        response.getWriter().println("Counter: " + updated);
    }
}
