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
<p>Login: <%= ((org.example.dto.PersonDto)request.getAttribute("person")).getLogin() %></p>


<h2>Update Person</h2>
<form action="${pageContext.request.contextPath}/person/update" method="post">
    <input type="hidden" name="id" value="${person.id}">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" value="${person.name}"><br>
    <label for="login">Login:</label>
    <input type="text" id="login" name="login" value="${person.login}"><br>
    <label for="role">Role:</label>
    <input type="text" id="role" name="role" value="<%= ((org.example.dto.PersonDto)request.getAttribute("person")).getRole().getId() %>"><br>
    <button type="submit">Update</button>
</form>

<h2>Delete Person</h2>
<form action="${pageContext.request.contextPath}/person/delete" method="post">
    <input type="hidden" name="id" value="<%= ((org.example.dto.PersonDto)request.getAttribute("person")).getId() %>">
    <button type="submit">Delete</button>
</form>



<% } else { %>
<h1>No Person Found</h1>
<% } %>
</body>
</html>
