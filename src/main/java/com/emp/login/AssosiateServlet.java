package com.emp.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.DAO;

@WebServlet("/AssosiateServlet")
public class AssosiateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    DAO dao = new DAO();
    Connection conn = null;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            conn = DAO.getConnection();

            PreparedStatement ps = conn.prepareStatement("SELECT uname, uid, role FROM login WHERE uname = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            HttpSession session = request.getSession();

            if (rs.next()) {
                String uid = rs.getString("uid");
                String role = rs.getString("role");
                session.setAttribute("username", username);
                session.setAttribute("uid", uid);
                session.setAttribute("role", role);

                RequestDispatcher rd = request.getRequestDispatcher("assosiateHome.jsp");
                rd.forward(request, response);
            } else {
                out.println("<h1>Login Failed</h1><br>");
                out.println("<a href='Assosiate.jsp'>Try again</a>");
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
}