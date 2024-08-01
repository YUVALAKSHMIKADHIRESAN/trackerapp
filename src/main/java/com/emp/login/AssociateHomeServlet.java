package com.emp.login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.emp.add.AddTask;

@WebServlet("/AssociateHomeServlet")
public class AssociateHomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String date = request.getParameter("date");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        String time = request.getParameter("time");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String role = request.getParameter("role");

        AddTask addTask = new AddTask();
        boolean result = addTask.addTask(id, name, date, start, end, time, title, description, role);

        if (result) {
            response.sendRedirect("assosiateHome.jsp"); 
        } else {
            response.sendRedirect("error.jsp"); 
        }
    }
}