package com.example.firstservlet.TelephoneNumber;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "userServlet", value = "/users") // path
public class UserServlet extends HttpServlet {
    private IUserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        userRepository = new UserRepository();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String idParam = req.getParameter("id");

        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            User user = userRepository.getById(id);

            if (user != null) {
                resp.getWriter().println(user.toString());
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Kullanıcı bulunamadı\"}");
            }

        } else {
            List<User> users = null;
            users = userRepository.getAll();
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                resp.getWriter().println(u.toString());
            }

        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String phone = req.getParameter("phone");


        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setPhoneNumber(phone);

        userRepository.updateUser(user);

        resp.setContentType("application/json");
        resp.getWriter().write("{\"message\": \"Kullanıcı güncellendi.\"}" + user.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String phone = req.getParameter("phone");
        int id = Integer.parseInt(req.getParameter("id"));

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setPhoneNumber(phone);

        userRepository.addUser(user);

        resp.setContentType("application/json");
        resp.getWriter().write("{\"message\": \"Kullanıcı eklendi.\"}" + user.toString());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        userRepository.deleteUser(id);

        resp.setContentType("application/json");
        resp.getWriter().write("{\"message\": \"Kullanıcı silindi.\"}");
    }


}
