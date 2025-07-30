<%--
  Created by IntelliJ IDEA.
  User: foyko
  Date: 30.07.2025
  Time: 19:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

        <h1>Congratulations!</h1>
        <h2><%=
            request.getAttribute("reward")
        %></h2>
</body>
</html>
