package com.example.firstservlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@WebServlet(name = "numberServlet", value = "/number")
public class NumberServlet extends HttpServlet {

    private final Random random = new Random();
    private final List<Integer> nums = new ArrayList<>();

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        super.service(req, res);
        System.out.println( "Entered Service Method!" + req.getMethod());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int num = random.nextInt(100);
        nums.add(num);
        resp.getWriter().println("Generated num: " + num);

    }



    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int oldNum = nums.getLast();
        int newNum = random.nextInt(100);

        nums.set(nums.indexOf(oldNum), newNum);

        resp.getWriter().println("Deleted num: " + oldNum );
        resp.getWriter().println("Generated new num: " + newNum);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(nums);

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int deletedNum = nums.getLast();
        nums.removeLast();
        resp.getWriter().println("Deleted num: " + deletedNum);

    }
    /*
    public void returner(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("text/html");

        // Hello
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");

    }
    */

}
