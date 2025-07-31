<%--
  Created by IntelliJ IDEA.
  User: foyko
  Date: 31.07.2025
  Time: 15:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.july31projectcontactmanagment.entities.Contact" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.july31projectcontactmanagment.entities.User" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login");
        return;
    }

    List<Contact> contacts = (List<Contact>) request.getAttribute("contacts");
%>

<html>
<head>
    <title>Contact List</title>
</head>
<body>

<!-- Üst kısım: Hoşgeldin + Logout + Arama -->
<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
    <h2>Hoşgeldin, <%= user.getUsername() %>!</h2>

    <!-- Arama formu -->
    <form action="contact" method="get" style="margin: 0;">
        <input type="text" name="search" placeholder="İsimle ara" value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>" />
        <input type="submit" value="Ara" />
    </form>

    <!-- Logout linki -->
    <a href="logout" style="color: red; text-decoration: none; font-weight: bold;">Çıkış Yap</a>
</div>

<!-- Yeni kişi oluşturma butonu -->
<a href="contact?action=create" style="display: inline-block; margin-bottom: 15px;">Create New Contact</a>

<!-- Kişiler tablosu -->
<table border="1" cellpadding="5" cellspacing="0">
    <thead>
    <tr>
        <th>Contact Name</th>
        <th>Contact Number</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <%
        if (contacts != null && !contacts.isEmpty()) {
            for (Contact c : contacts) {
    %>
    <tr>
        <td><%= c.getContactname() %></td>
        <td><%= c.getContactnumber() %></td>
        <td>
            <a href="contact?action=update&id=<%= c.getId() %>">Update</a>
            |
            <a href="contact?action=delete&id=<%= c.getId() %>" onclick="return confirm('Are you sure?');">Delete</a>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="3">No contacts found.</td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

</body>
</html>
