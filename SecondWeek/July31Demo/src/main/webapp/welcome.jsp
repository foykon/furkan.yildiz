
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  if (session == null || session.getAttribute("username") == null) {
    response.sendRedirect("login.jsp");
    return;
  }
%>
<html>
<head><title>Welcome</title></head>
<body>
<h1>Welcome, <%= session.getAttribute("username") %>!</h1>
<h3>Session ID: <%= session.getId() %></h3>
<form action="logout" method="post">
  <input type="submit" value="Logout"/>
</form>
</body>
</html>

