package com.manager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.DAO;

@WebServlet("/FetchEmp")
public class FetchEmp extends HttpServlet {
    private static final long serialVersionUID = 1L;
    DAO dao = new DAO();
    Connection conn = null;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        List<String> employeeNames = new ArrayList<>();

        try {
            conn = DAO.getConnection();

            PreparedStatement ps = conn.prepareStatement("SELECT uname FROM login");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String uname = rs.getString("uname");
                if (uname != null && !uname.trim().isEmpty()) {
                    employeeNames.add(uname);
                }
            }

            request.setAttribute("employeeNames", employeeNames);

            RequestDispatcher rd = request.getRequestDispatcher("listEmployees.jsp");
            rd.forward(request, response);
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
}