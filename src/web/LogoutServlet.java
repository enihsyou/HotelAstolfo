package web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "web.LogoutServlet", urlPatterns = "/logout.do")
public class LogoutServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    final HttpSession session = request.getSession(false);
    if (session == null) {
      response.sendRedirect("/index.html");
      return;
    }
    session.removeAttribute("username");
    session.invalidate();
    response.sendRedirect("/index.html");
  }

  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }
}
