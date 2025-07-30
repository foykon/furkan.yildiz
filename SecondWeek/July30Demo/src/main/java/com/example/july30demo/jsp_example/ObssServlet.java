package com.example.july30demo.jsp_example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/go-servlet")
public class ObssServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Go to OBSS</title>
                    <style>
                        body {
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                            margin: 0;
                        }
                        a {
                            color: red;
                            text-decoration: none;
                            font-size: 20px;
                        }
                        a:hover {
                            color: blue;
                        }
                    </style>
                </head>
                <body>
                    <p><b><a href='https://obss.tech' target='_blank'>Go to OBSS from servlet</a></b></p>
                </body>
                </html>
                """);
    }
}
