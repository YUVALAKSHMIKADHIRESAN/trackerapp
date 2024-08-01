package com.emp.login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.DAO;

@WebServlet("/ModifyTaskServlet")
public class ModifyTaskServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String date = request.getParameter("date");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        String time = request.getParameter("time");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String role = request.getParameter("role");

        if (id == null || date == null || start == null || time == null ||
            id.trim().isEmpty() || date.trim().isEmpty() || start.trim().isEmpty() || time.trim().isEmpty()) {
            throw new ServletException("Missing parameters for task modification");
        }

        try {
            Connection conn = DAO.getConnection();
            String query = "UPDATE task SET name=?, end=?, time=?, title=?, description=?, role=? " +
                           "WHERE id=? AND date=? AND start=? AND time=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, request.getParameter("name"));
            ps.setString(2, end);
            ps.setString(3, time);
            ps.setString(4, title);
            ps.setString(5, description);
            ps.setString(6, role);
            ps.setString(7, id);
            ps.setString(8, date);
            ps.setString(9, start);
            ps.setString(10, time);

            ps.executeUpdate();
            response.sendRedirect("viewTasks.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error modifying task", e);
        }
    }
}