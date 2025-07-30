<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>JSP - Hello World</title>
</head>
<body>

<!-- Static -->
<%@ include file="header.jsp" %>

<hr/>

<!-- Dynamic Include -->
<jsp:include page="message.jsp" />

<hr/>

<!-- Servlet
-->
<%
  RequestDispatcher dispatcher = request.getRequestDispatcher("time");
  dispatcher.include(request, response);
%>

</body>
</html>