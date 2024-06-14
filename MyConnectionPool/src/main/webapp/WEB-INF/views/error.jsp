<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error Page</title>
</head>
<body>
<div class="error-container">
    <h1>Error</h1>
    <p><%= request.getAttribute("error") != null ? request.getAttribute("error") : "An unknown error occurred." %>
    </p>
</div>
</body>
</html>
