package com.example.july29demo.OriginAndTarget;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/origin")
public class OriginServlet extends HttpServlet {
    ServletContext servletContext;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Request comes to OriginServlet");

        req.setAttribute("atrb1",1);
        req.setAttribute("atrb2",2);
        req.setAttribute("atrb3",3);

        servletContext = getServletContext();
        servletContext.setAttribute("atrb1",4);
        servletContext.setAttribute("atrb2",5);
        servletContext.setAttribute("atrb3",6);

        req.getRequestDispatcher("/target").forward(req,resp);

        //resp.sendRedirect(req.getContextPath()+"/target");

    }

}
