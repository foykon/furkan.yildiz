<%--
  Created by IntelliJ IDEA.
  User: foyko
  Date: 31.07.2025
  Time: 15:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.july31projectcontactmanagment.entities.Contact" %>
<%@ page import="com.example.july31projectcontactmanagment.entities.User" %>

<%
  Contact contact = (Contact) request.getAttribute("contact");
  boolean isUpdate = contact != null && contact.getId() > 0;
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect("login");
    return;
  }
%>

<html>
<head>
  <title><%= isUpdate ? "Update Contact" : "Create New Contact" %></title>
</head>
<body>

<h2><%= isUpdate ? "Update Contact" : "Create New Contact" %></h2>

<form action="contact" method="post">
  <% if (isUpdate) { %>
  <input type="hidden" name="id" value="<%= contact.getId() %>"/>
  <% } %>

  <input type="hidden" name="userid" value="<%= user.getId() %>"/>

  <label for="contactname">Contact Name:</label><br>
  <input type="text" id="contactname" name="contactname" value="<%= isUpdate ? contact.getContactname() : "" %>" required/><br><br>

  <label for="contactnumber">Contact Number:</label><br>
  <input type="text" id="contactnumber" name="contactnumber" value="<%= isUpdate ? contact.getContactnumber() : "" %>" required/><br><br>

  <input type="submit" value="<%= isUpdate ? "Update" : "Create" %>"/>
</form>

<a href="contact">Back to Contact List</a>

</body>
</html>