package web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "web.LoginServlet", urlPatterns = "/login.do")
public class LoginServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    final String input_username = request.getParameter("username");
    // final String input_password = request.getParameter("password");
    // final String input_auto_login = request.getParameter("auto_login");

    if (input_username != null /*&& input_password != null*/) {
      final HttpSession session = request.getSession();
      // if (input_auto_login != null) {
      //   final Cookie login_username_cookie = new Cookie("username", input_username);
      //   login_username_cookie.setMaxAge(30 * 60);
      //   response.addCookie(login_username_cookie);
      //   session.setAttribute("username", input_username);
      // }
      final Cookie login_username_cookie = new Cookie("username", input_username);
      response.addCookie(login_username_cookie);
      session.setAttribute("username", input_username);
      response.sendRedirect("/list");
    }
  }
}
