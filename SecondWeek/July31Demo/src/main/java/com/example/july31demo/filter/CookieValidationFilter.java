package com.example.july31demo.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter("/*")
public class CookieValidationFilter extends HttpFilter {
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cookies required");
            return;
        }

        boolean hasTest1 = false;
        boolean hasTest2 = false;

        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if ("test1".equals(name)) {
                hasTest1 = true;
            } else if ("test2".equals(name)) {
                hasTest2 = true;
            } else if ("JSESSIONID".equals(name)) {
                // izin veriyoruz
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unexpected cookie: " + name);
                return;
            }
        }

        if (hasTest1 && hasTest2) {
            chain.doFilter(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required cookies");
        }
    }
}
