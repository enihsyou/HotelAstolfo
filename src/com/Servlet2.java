package com;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@WebServlet(name = "Servlet2", urlPatterns = "/search.do")
public class Servlet2 extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    final String student_department = request.getParameter("student_department");
    if (student_department == null)
      return;
    try {
      DriverManager.registerDriver(new com.mysql.jdbc.Driver());
      Properties properties = new Properties();
      properties.setProperty("user", "root");
      properties.setProperty("password", "enihsyou");
      final Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", properties);final PreparedStatement statement = conn.prepareStatement("SELECT * FROM test.student WHERE department=?");
      statement.setString(1, student_department);
      final ResultSet query = statement.executeQuery();
      List<Student> list = new ArrayList<>();
      while (query.next()) {
        final int id = query.getInt("id");
        final String name = query.getString("student_name");
        final int gender = query.getInt("gender");
        final int age = query.getInt("age");
        list.add(new Student(id, name, gender, age, student_department));
      }
      request.setAttribute("list", list);
      request.getRequestDispatcher("/WEB-INF/search.jsp").forward(request, response);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}

