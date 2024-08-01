<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Associate Login</title>
<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
<h1>Welcome Associate</h1>
<h1>Associate Login</h1>
<form action="AssosiateServlet" method="post">
    <label>Username: <input type="text" id="username" name="username"></label>
    <label>Password: <input type="password" id="password" name="password"></label>
    <input type="submit" value="Login">
</form>
<form action="Emp.jsp" method="post">
    <input type="submit" value="Back" class="back-button">
</form>
</div>
</body>
</html>
