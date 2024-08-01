<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MANAGER login</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <div class="container">
        <h1>WELCOME MANAGER</h1>
        <br>
        <h1>MANAGER LOGIN</h1>
        <form action="AdminHome" method="post">
            <label>Username: <input type="text" id="username" name="username"></label>
            <label>Password: <input type="password" id="password" name="password"></label>
            <input type="submit" value="login">
        </form>
        <form action="Emp.jsp" method="post">
            <input type="submit" value="Back">
        </form>
    </div>
</body>
</html>
