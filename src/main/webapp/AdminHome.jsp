<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Home</title>
<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
<h1>Admin Home</h1>
<%
String u_name = (String)session.getAttribute("username");
out.print("Hi Admin "+u_name);
%>
<form action="FetchEmp" method="post">
    <input type="submit" value="List of Employees">
</form>
<form action="manager.jsp" method="post">
    <input type="submit" value="Logout" class="back-button">
</form>
</div>
</body>
</html>
