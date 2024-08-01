package com.emp.add;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.dao.DAO;
import com.google.gson.Gson;

@WebServlet("/WeeklyProgressServlet")
public class WeeklyProgressServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String weekInput = request.getParameter("week"); 

        String[] weekParts = weekInput.split("-W");
        int year = Integer.parseInt(weekParts[0]);
        int week = Integer.parseInt(weekParts[1]);
        
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate startOfWeek = LocalDate.of(year, 1, 1).with(weekFields.weekOfYear(), week).with(weekFields.dayOfWeek(), 1); 
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        DateTimeFormatter sqlDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateString = startOfWeek.format(sqlDateFormatter);
        String endDateString = endOfWeek.format(sqlDateFormatter);
        
        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        Connection conn = null;

        try {
            conn = DAO.getConnection();

            PreparedStatement ps = conn.prepareStatement("SELECT date, SUM(time) as total_time FROM task WHERE id = ? AND date BETWEEN ? AND ? GROUP BY date");
            ps.setString(1, id);
            ps.setString(2, startDateString);
            ps.setString(3, endDateString);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                labels.add(rs.getDate("date").toString());
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