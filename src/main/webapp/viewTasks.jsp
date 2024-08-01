<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.emp.add.Task" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>View Tasks</title>
<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
<%
    HttpSession sess = request.getSession(false);
    if (sess == null || sess.getAttribute("username") == null) {
        response.sendRedirect("Associate.jsp");
        return;
    }

    String username = (String) sess.getAttribute("username");
%>

<h1>Tasks for <%= username %></h1>

<form action="ViewTasksServlet" method="post">
    <label>Select Date: <input type="date" id="selectedDate" name="selectedDate" required></label><br>
    <input type="submit" value="View Tasks">
</form>

<%
    @SuppressWarnings("unchecked")
    List<Task> tasks = (List<Task>) request.getAttribute("tasks");
    if (tasks == null || tasks.isEmpty()) {
        out.println("<p>No tasks available for the selected date.</p>");
    } else {
        for (Task task : tasks) {
%>
    <!-- Task modification and deletion forms -->
    <form action="ModifyTaskServlet" method="post" style="margin-bottom: 20px;">
        <input type="hidden" name="id" value="<%= task.getId() %>">
        <input type="hidden" name="date" value="<%= task.getDate() %>">
        <input type="hidden" name="start" value="<%= task.getStart() %>">
        <input type="hidden" name="time" value="<%= task.getTime() %>">
        <div>
            <label>ID:</label> <%= task.getId() %><br>
            <label>Name:</label> <input type="text" name="name" value="<%= task.getName() %>" required><br>
            <label>Date:</label> <input type="date" name="date" value="<%= task.getDate() %>" required><br>
            <label>Start:</label> <input type="time" name="start" value="<%= task.getStart() %>" required><br>
            <label>End:</label> <input type="time" name="end" value="<%= task.getEnd() %>" required><br>
            <label>Time:</label> <input type="text" name="time" value="<%= task.getTime() %>" required><br>
            <label>Title:</label> <input type="text" name="title" value="<%= task.getTitle() %>" required><br>
            <label>Description:</label> <input type="text" name="description" value="<%= task.getDescription() %>" required><br>
            <label>Role:</label> <input type="text" name="role" value="<%= task.getRole() %>" required><br>
            <input type="submit" value="Update Task">
        </div>
    </form>

    <form action="DeleteTaskServlet" method="post" style="margin-bottom: 20px;">
        <input type="hidden" name="id" value="<%= task.getId() %>">
        <input type="submit" value="Delete Task">
    </form>
<%
        }
    }
%>

<form action="assosiateHome.jsp" method="post">
    <input type="submit" value="Back" class="back-button">
</form>
</div>
</body>
</html>
