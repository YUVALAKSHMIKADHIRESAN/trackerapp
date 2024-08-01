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

@WebServlet("/DeleteTaskServlet")
public class DeleteTaskServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String date = request.getParameter("date");
        String start = request.getParameter("start");
        String title = request.getParameter("title");

        if (id == null || date == null || start == null || title == null ||
            id.trim().isEmpty() || date.trim().isEmpty() || start.trim().isEmpty() || title.trim().isEmpty()) {
            throw new ServletException("Missing parameters for task deletion");
        }

        try {
            Connection conn = DAO.getConnection();
            String query = "DELETE FROM task WHERE id=? AND date=? AND start=? AND title=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, date);
            ps.setString(3, start);
            ps.setString(4, title);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new ServletException("No task found with the specified criteria");
            }
            response.sendRedirect("viewTasks.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error deleting task", e);
        }
    }
}