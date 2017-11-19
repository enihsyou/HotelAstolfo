package web

import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.*
import java.io.IOException

@WebServlet(name = "web.LoginServlet", urlPatterns = arrayOf("/login.do"))
class LoginServlet : HttpServlet() {
  @Throws(ServletException::class, IOException::class)
  override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
    val inputUsername: String = request.getParameter("username") // 用户名
    val inputRemember: String? = request.getParameter("remember")
    val isRemember: Boolean = !inputRemember.isNullOrBlank() // 是否自动登陆
    println("inputUsername = ${inputUsername}")
    val session = request.session
    val cookieUsername = Cookie("username", inputUsername)
    response.addCookie(cookieUsername)
    session.setAttribute("username", inputUsername)
    if (isRemember)
      response.addCookie(Cookie("remember", "1"))

    response.sendRedirect("/list")
  }
}

