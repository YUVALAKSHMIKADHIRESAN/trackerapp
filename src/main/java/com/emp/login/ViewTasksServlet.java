package com.emp.login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.DAO;
import com.emp.add.Task;

@WebServlet("/ViewTasksServlet")
public class ViewTasksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("username");
        String selectedDate = request.getParameter("selectedDate");

        List<Task> tasks = new ArrayList<>();

        try {
            Connection conn = DAO.getConnection();
            String query = "SELECT * FROM task WHERE name = ? AND date = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, selectedDate);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getString("id"));
                task.setName(rs.getString("name"));
                task.setDate(rs.getString("date"));
                task.setStart(rs.getString("start"));
                task.setEnd(rs.getString("end"));
                task.setTime(rs.getString("time"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setRole(rs.getString("role"));
                tasks.add(task);
            }

            request.setAttribute("tasks", tasks);
            RequestDispatcher rd = request.getRequestDispatcher("viewTasks.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving tasks", e);
        }
    }
}