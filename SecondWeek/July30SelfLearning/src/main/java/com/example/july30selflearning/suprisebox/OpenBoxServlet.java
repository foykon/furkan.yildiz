package com.example.july30selflearning.suprisebox;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Random;

@WebServlet(value = "/open-box")
public class OpenBoxServlet extends HttpServlet {
    private Random rand;
    private OpenBoxService openBoxService;



    @Override
    public void init() throws ServletException {
        super.init();
        rand = new Random();
        openBoxService = OpenBoxService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getCookies() != null){
            for (Cookie cookie : req.getCookies()) {
                if (cookie.getName().equals("is-opened")) {
                    resp.sendRedirect("already.jsp");
                    return;
                }
                }
        }
        req.getRequestDispatcher("open-box.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name =req.getParameter("name");

        if(openBoxService.isOpened(name)){
            resp.sendRedirect("already.jsp");
            return;
        }

        Rewards reward = Rewards.values()[rand.nextInt(Rewards.values().length)];
        openBoxService.put(name, reward.toString());
        Cookie cookie = new Cookie("is-opened", "true");
        cookie.setMaxAge(3600);
        resp.addCookie(cookie);

        req.setAttribute("reward", reward.toString());
        req.getRequestDispatcher("reward.jsp").forward(req, resp);



    }
}
