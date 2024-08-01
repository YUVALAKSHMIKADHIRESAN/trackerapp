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


@WebServlet("/AdminHome")
public class AdminHome extends HttpServlet {
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

            PreparedStatement ps = conn.prepareStatement("SELECT uname FROM loginm WHERE uname = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

          HttpSession session = request.getSession();
          session.setAttribute("username", username);

            if (rs.next()) {
                RequestDispatcher rd = request.getRequestDispatcher("AdminHome.jsp");
                rd.forward(request, response);
            } else {
                out.println("<font color='red' size='18'>Login Failed<br>");
                out.println("<a href='manager.jsp'>Try again</a>");
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