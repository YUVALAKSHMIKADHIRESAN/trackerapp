<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee List</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
    <h1>Select an Employee</h1>
    <form action="EmployeeDetailsServlet" method="post">
        <%
            List<String> employeeNames = (List<String>) request.getAttribute("employeeNames");
            if (employeeNames != null && !employeeNames.isEmpty()) {
                for (String employee : employeeNames) {
        %>
            <input type="radio" name="employeeName" value="<%= employee %>"> <%= employee %><br>
        <%
                }
            } else {
        %>
            <p>No employees found.</p>
        <%
            }
        %>
        <input type="submit" value="Show Details">
    </form>
    <form action="AdminHome.jsp" method="post">
        <input type="submit" value="Back" class="back-button">
    </form>
    </div>
</body>
</html>
