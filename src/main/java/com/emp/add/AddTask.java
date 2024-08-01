package com.emp.add;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dao.DAO;

public class AddTask {
    public boolean addTask(String id, String name, String date, String start, String end, String time, String title, String description, String role) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean status = false;
        
        try {
            DAO dao = new DAO();
            conn = DAO.getConnection();
            String checkQuery = "SELECT COUNT(*) FROM task WHERE id = ? AND date = ? AND start = ?";
            ps = conn.prepareStatement(checkQuery);
            ps.setString(1, id);
            ps.setString(2, date);
            ps.setString(3, start);
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.err.println("Task already exists for the same date and time.");
                return false;
            }
            String totalHoursQuery = "SELECT SUM(time) FROM task WHERE id = ? AND date = ?";
            ps = conn.prepareStatement(totalHoursQuery);
            ps.setString(1, id);
            ps.setString(2, date);
            rs = ps.executeQuery();
            double totalHours = 0;
            if (rs.next()) {
                totalHours = rs.getDouble(1);
            }
            
            double newTaskHours = Double.parseDouble(time);
            if (totalHours + newTaskHours > 8) {
                System.err.println("Total hours for the day cannot exceed 8 hours.");
                return false;
            }
            String insertQuery = "INSERT INTO task (id, name, date, start, end, time, title, description, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(insertQuery);
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, date);
            ps.setString(4, start);
            ps.setString(5, end);
            ps.setString(6, time);
            ps.setString(7, title);
            ps.setString(8, description);
            ps.setString(9, role);
            int res = ps.executeUpdate();
            status = res > 0;
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return status;
    }
}