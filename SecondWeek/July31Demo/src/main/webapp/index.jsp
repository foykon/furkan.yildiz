<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %></h1>
<br/>
  <form action="login" method="post">
    <label>User name</label>
    <input type="text" placeholder="username" name="username">
    <label>User password</label>
    <input type="password" placeholder="password" name="password">
    <input type="submit">
  </form>
</body>
</html>