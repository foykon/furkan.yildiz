package com.example.july31projectcontactmanagment.controller;

import com.example.july31projectcontactmanagment.entities.Contact;
import com.example.july31projectcontactmanagment.entities.User;
import com.example.july31projectcontactmanagment.repository.contact.ContactRepository;
import com.example.july31projectcontactmanagment.repository.contact.IContactRepository;
import com.example.july31projectcontactmanagment.service.contact.ContactService;
import com.example.july31projectcontactmanagment.service.contact.IContactService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(value = "/contact")
public class ContactController extends HttpServlet {

    private IContactService contactService;
    private IContactRepository contactRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        contactRepository = new ContactRepository();
        contactService = new ContactService(contactRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        String search = req.getParameter("search");
        if (action == null) action = "";

        switch (action) {
            case "create":
                req.getRequestDispatcher("/WEB-INF/contact_edit.jsp").forward(req, resp);
                break;
            case "update":
                String idStr = req.getParameter("id");
                if (idStr != null) {
                    int id = Integer.parseInt(idStr);
                    Contact contact = contactService.getContactById(id);
                    req.setAttribute("contact", contact);
                    req.getRequestDispatcher("/WEB-INF/contact_edit.jsp").forward(req, resp);
                } else {
                    resp.sendRedirect(req.getContextPath() + "/contact");
                }
                break;
            case "delete":
                String delIdStr = req.getParameter("id");
                if (delIdStr != null) {
                    int delId = Integer.parseInt(delIdStr);
                    contactService.deleteContact(delId);
                }
                resp.sendRedirect(req.getContextPath() + "/contact");
                break;
            default:
                List<Contact> contacts;
                if (search != null && !search.trim().isEmpty()) {
                    contacts = contactService.searchByName(search, user.getId());
                } else {
                    contacts = contactService.getContactsByUserId(user.getId());
                }
                req.setAttribute("contacts", contacts);
                req.getRequestDispatcher("/WEB-INF/contact.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        String contactname = req.getParameter("contactname");
        String contactnumber = req.getParameter("contactnumber");

        Contact contact = new Contact();
        contact.setUserid(user.getId());
        contact.setContactname(contactname);
        contact.setContactnumber(contactnumber);

        if (idStr == null || idStr.isEmpty()) {
            contactService.addContact(contact);
        } else {
            contact.setId(Integer.parseInt(idStr));
            contactService.updateContact(contact);
        }

        resp.sendRedirect(req.getContextPath() + "/contact");
    }
}

