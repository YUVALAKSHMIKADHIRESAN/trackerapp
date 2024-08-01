package com.manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.DAO;
import com.google.gson.Gson;

@WebServlet("/EmployeeDetailsServlet")
public class EmployeeDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    DAO dao = new DAO();
    Connection conn = null;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String employeeName = request.getParameter("employeeName");
        String dailyDate = request.getParameter("dailyDate");
        String weeklyDate = request.getParameter("weeklyDate");
        String monthlyDate = request.getParameter("monthlyDate");

        try {
            conn = DAO.getConnection();

            PreparedStatement ps = conn.prepareStatement("SELECT uname FROM login WHERE uname = ?");
            ps.setString(1, employeeName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                request.setAttribute("employeeName", rs.getString("uname"));

                Map<String, Integer> dailyData = new HashMap<>();
                Map<String, Integer> weeklyData = new HashMap<>();
                Map<String, Integer> monthlyData = new HashMap<>();

                if (dailyDate != null) {
                    dailyData = fetchTaskData("SELECT title, SUM(time) as total_time FROM task WHERE name = ? AND date = ? GROUP BY title", employeeName, dailyDate);
                } else if (weeklyDate != null) {
                    LocalDate startOfWeek = parseWeek(weeklyDate);
                    LocalDate endOfWeek = startOfWeek.plusDays(6);
                    weeklyData = fetchDateData("SELECT date, SUM(time) as total_time FROM task WHERE name = ? AND date BETWEEN ? AND ? GROUP BY date", employeeName, startOfWeek.toString(), endOfWeek.toString());
                } else if (monthlyDate != null) {
                    LocalDate startOfMonth = LocalDate.parse(monthlyDate + "-01");
                    LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
                    monthlyData = fetchDateData("SELECT date, SUM(time) as total_time FROM task WHERE name = ? AND date BETWEEN ? AND ? GROUP BY date", employeeName, startOfMonth.toString(), endOfMonth.toString());
                }

                Gson gson = new Gson();
                String dailyDataJson = gson.toJson(dailyData);
                String weeklyDataJson = gson.toJson(weeklyData);
                String monthlyDataJson = gson.toJson(monthlyData);

                request.setAttribute("dailyData", dailyDataJson);
                request.setAttribute("weeklyData", weeklyDataJson);
                request.setAttribute("monthlyData", monthlyDataJson);

                RequestDispatcher rd = request.getRequestDispatcher("employeeDetails.jsp");
                rd.forward(request, response);
            } else {
                PrintWriter out = response.getWriter();
                out.println("<h1>No details found for the selected employee.</h1>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("SQL error.", e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private LocalDate parseWeek(String weekInput) {
        String[] weekParts = weekInput.split("-W");
        int year = Integer.parseInt(weekParts[0]);
        int week = Integer.parseInt(weekParts[1]);

        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate startOfWeek = LocalDate.of(year, 1, 1).with(weekFields.weekOfYear(), week).with(weekFields.dayOfWeek(), 1); 
        return startOfWeek;
    }

    private Map<String, Integer> fetchTaskData(String query, String employeeName, String date) throws SQLException {
        Map<String, Integer> data = new HashMap<>();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, employeeName);
        ps.setString(2, date);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            data.put(rs.getString("title"), rs.getInt("total_time"));
        }

        return data;
    }

    private Map<String, Integer> fetchDateData(String query, String employeeName, String startDate, String endDate) throws SQLException {
        Map<String, Integer> data = new HashMap<>();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, employeeName);
        ps.setString(2, startDate);
        ps.setString(3, endDate);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            data.put(rs.getString("date"), rs.getInt("total_time"));
        }

        return data;
    }
}