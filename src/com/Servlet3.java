package com;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

@WebServlet(name = "Servlet3", urlPatterns = "/delete.do")
public class Servlet3 extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    {
      final String student_id = request.getParameter("student_id");
      if (student_id == null)
        return;
      try {
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "enihsyou");
        final Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", properties);
        final PreparedStatement statement = conn.prepareStatement("DELETE * FROM test.student WHERE id = ?");
        statement.setInt(1, Integer.parseInt(student_id));
        statement.execute();
        response.sendRedirect("/all");
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
