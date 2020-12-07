<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>JSP Redirect Example</title>
</head>
<body>
    <div align="center">
        <br/>
        <h2>${message}</h3>
        <br/>
        <h3>This page will be closed automatically in 5 seconds</h2>
    </div>
    <% response.setHeader("Refresh", "5;url=index.html"); %>
</body>
</html>  