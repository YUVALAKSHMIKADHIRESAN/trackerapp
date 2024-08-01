package com.emp.add;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.DAO;
import com.google.gson.Gson;

@WebServlet("/MonthlyProgressServlet")
public class MonthlyProgressServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    DAO dao = new DAO();
    Connection conn = null;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String month = request.getParameter("month");

        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        Connection conn = null;

        try {
            conn = DAO.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                "SELECT DAY(date) as day, SUM(time) as total_time " +
                "FROM task WHERE id = ? AND MONTH(date) = MONTH(?) AND YEAR(date) = YEAR(?) " +
                "GROUP BY DAY(date) ORDER BY DAY(date)");
            ps.setString(1, id);
            ps.setString(2, month + "-01");
            ps.setString(3, month + "-01");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                labels.add(String.valueOf(rs.getInt("day")));
                values.add(rs.getInt("total_time"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
            return;
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(new BarChartData(labels, values));

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    private static class BarChartData {
        List<String> labels;
        List<Integer> values;

        BarChartData(List<String> labels, List<Integer> values) {
            this.labels = labels;
            this.values = values;
        }
    }
}