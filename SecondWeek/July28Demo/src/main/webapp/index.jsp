<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %></h1>
<br/>
<a href="hello">Hello Servlet</a>
<a href="number">Show Numbers</a>
<a onclick="sendPut()">Generate number</a>
</body>
</html>
<script>
  function sendPut() {
    fetch('number', {
      method: 'POST'
    }).then(response => response.text())
            .then(data => alert(data));
  }
</script>