package com;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

@WebServlet("/index.do")
public class Servlet extends javax.servlet.http.HttpServlet {
  @Resource(lookup = "jdbc/db")
  private DataSource dataSource;

  protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
      throws ServletException, IOException {
    final String student_id = request.getParameter("student_id");
    final String student_name = request.getParameter("student_name");
    final String student_gender = request.getParameter("student_gender");
    final String student_age = request.getParameter("student_age");
    final String student_department = request.getParameter("student_department");
    if (student_age == null ||
        student_id == null ||
        student_name == null ||
        student_department == null ||
        student_gender == null)
      return;
    try {
      DriverManager.registerDriver(new com.mysql.jdbc.Driver());
      Properties properties = new Properties();
      properties.setProperty("user", "root");
      properties.setProperty("password", "enihsyou");
      final Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", properties);
      final PreparedStatement statement = conn.prepareStatement(
          "INSERT INTO test.student (id, student_name, gender, age, department) VALUES (?, ?, ?, ?, ?);");
      statement.setInt(1, Integer.parseInt(student_id));
      statement.setString(2, student_name);
      statement.setInt(3, Integer.parseInt(student_gender));
      statement.setInt(4, Integer.parseInt(student_age));
      statement.setString(5, student_department);
      final int i = statement.executeUpdate();
      if (i == Statement.EXECUTE_FAILED)
        response.sendRedirect("/index.jsp");
      response.sendRedirect("/all");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
