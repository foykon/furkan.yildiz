package com.example.usermanagment.controller;

import com.example.usermanagment.model.User;
import com.example.usermanagment.repository.IUserRepository;
import com.example.usermanagment.repository.UserRepository;
import com.example.usermanagment.service.UserService;
import com.example.usermanagment.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "userServlet", value = "/users")
public class UserServlet extends HttpServlet {
    private UserService userService;
    private IUserRepository repository;

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        super.service(req, res);
        System.out.println( "Entered Service Method!" + req.getMethod());

    }

    @Override
    public void init() throws ServletException {
        super.init();
        repository = new UserRepository();
        userService = new UserServiceImpl(repository);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String idParam = req.getParameter("id");

        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            User user = userService.getById(id);

            if (user != null) {
                resp.getWriter().write(
                        String.format("{\"id\": %d, \"name\": \"%s\", \"surname\": \"%s\", \"age\": %d}",
                                user.getId(), user.getName(), user.getSurname(), user.getAge()));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Kullanıcı bulunamadı\"}");
            }

        } else {
            List<User> users = null;
            try {
                users = userService.getAll();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                json.append(String.format("{\"id\": %d, \"name\": \"%s\", \"surname\": \"%s\", \"age\": %d}",
                        u.getId(), u.getName(), u.getSurname(), u.getAge()));
                if (i != users.size() - 1) json.append(",");
            }
            json.append("]");
            resp.getWriter().write(json.toString());
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        int age = Integer.parseInt(req.getParameter("age"));

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setAge(age);

        userService.updateUser(user);

        resp.setContentType("application/json");
        resp.getWriter().write("{\"message\": \"Kullanıcı güncellendi.\"}");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        int age = Integer.parseInt(req.getParameter("age"));
        int id = Integer.parseInt(req.getParameter("id"));

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setAge(age);

        userService.addUser(user);

        resp.setContentType("application/json");
        resp.getWriter().write("{\"message\": \"Kullanıcı eklendi.\"}");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        userService.deleteUser(id);

        resp.setContentType("application/json");
        resp.getWriter().write("{\"message\": \"Kullanıcı silindi.\"}");
    }
}
