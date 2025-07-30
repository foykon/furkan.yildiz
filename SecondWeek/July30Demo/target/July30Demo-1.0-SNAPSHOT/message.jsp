<%--
  Created by IntelliJ IDEA.
  User: foyko
  Date: 30.07.2025
  Time: 10:38
  To change this template use File | Settings | File Templates.
--%>
<%
    java.util.Calendar calendar = java.util.Calendar.getInstance();
    String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    String today = days[calendar.get(java.util.Calendar.DAY_OF_WEEK) - 1];
%>
<p>Hello, today is <strong><%= today %></strong>!</p>
<p>This is a dynamic included message.</p>
