package com.example.july29demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@WebServlet("/exercise")
public class JSPExerciseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = "Furkan";
        int age = 23;

        Random rand = new Random();
        List<Integer> randomNumbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            randomNumbers.add(rand.nextInt(100));
        }

        // scope a göndeiroyuz
        request.setAttribute("name", name);
        request.setAttribute("age", age);
        request.setAttribute("randomNumbers", randomNumbers);

        // jspyi forward et
        request.getRequestDispatcher("/exercise.jsp").forward(request, response);
    }
}
