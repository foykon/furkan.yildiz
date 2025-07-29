<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: foyko
<%--  Date: 29.07.2025--%>
  Time: 16:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Forwarded Random Numbers</title>
</head>
<body>
<h2>Kullanıcı Bilgisi</h2>
<p>İsim: ${name}</p>
<p>Yaş: ${age}</p>

<h2>Rastgele Sayı</h2><ul>
    <%
        List<Integer> numbers = (List<Integer>) request.getAttribute("randomNumbers");
        if (numbers != null) {
            for (Integer num : numbers) {
    %>
    <li><%= num %></li>
    <%
        }
    } else {
    %>
    <li>Liste boşş .</li>
    <%
        }
    %>
</ul>
</body>
</html>
