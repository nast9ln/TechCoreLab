<%@ page import="org.example.enums.RoleEnum" %><%--
  Created by IntelliJ IDEA.
  User: Настя
  Date: 6/6/2024
  Time: 12:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Person</title>
</head>
<body>
<h1>Create Person</h1>
<form action="${pageContext.request.contextPath}/person/create" method="post">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" required><br>
    <label for="login">Login:</label>
    <input type="text" id="login" name="login" required><br>
    <label for="roleId">Role ID:</label>
    <input type="number" id="roleId" name="roleId"><br>
    <label for="roleName">Role Name:</label>
    <select id="roleName" name="roleName">
        <option value="">Select Role</option>
        <% for (RoleEnum role : (RoleEnum[]) request.getAttribute("roles")) { %>
        <option value="<%= role.name() %>"><%= role.name() %></option>
        <% } %>
    </select><br>
    <button type="submit">Create</button>
</form>
</body>
</html>
