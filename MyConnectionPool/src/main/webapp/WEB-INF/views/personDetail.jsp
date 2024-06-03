<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Person Details</title>
</head>
<body>
<% if (request.getAttribute("person") != null) { %>
<h1>Person Details</h1>
<p>ID: <%= ((org.example.dto.PersonDto)request.getAttribute("person")).getId() %></p>
<p>Name: <%= ((org.example.dto.PersonDto)request.getAttribute("person")).getName() %></p>
<p>Name: <%= ((org.example.dto.PersonDto)request.getAttribute("person")).getLogin() %></p>
<% } else { %>
<h1>No Person Found</h1>
<% } %>
</body>
</html>
