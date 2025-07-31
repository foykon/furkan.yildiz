<%--
  Created by IntelliJ IDEA.
  User: foyko
  Date: 31.07.2025
  Time: 16:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <title>Sign In</title>
</head>
<body>

<h2>Kayıt Ol</h2>

<% String error = (String) request.getAttribute("error"); %>
<% if (error != null) { %>
<p style="color:red;"><%= error %></p>
<% } %>

<form action="signin" method="post">
  <label>Kullanıcı Adı:</label>
  <input type="text" name="username" required><br><br>
  <label>Şifre:</label>
  <input type="password" name="password" required><br><br>
  <input type="submit" value="Kayıt Ol">
</form>

<a href="login">Zaten hesabınız var mı? Giriş Yap</a>

</body>
</html>

