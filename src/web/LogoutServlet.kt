package web

import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException

@WebServlet(name = "web.LogoutServlet", urlPatterns = arrayOf("/logout.do"))
class LogoutServlet : HttpServlet() {
  @Throws(ServletException::class, IOException::class)
  override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
    val session = request.getSession(false)
    if (session == null) {
      response.sendRedirect("/index.html")
      return
    }
    session.removeAttribute("username")
    session.invalidate()
    response.sendRedirect("/index.html")
  }

  @Throws(ServletException::class, IOException::class)
  override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
    doPost(request, response)
  }
}
