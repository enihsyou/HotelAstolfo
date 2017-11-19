package web

import dbo.NoteList
import model.Note
import model.User

import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import java.time.LocalDateTime
import java.util.ArrayList

@WebServlet(name = "web.ListServlet", urlPatterns = arrayOf("/list", "/list.do"))
class ListServlet : HttpServlet() {
  @Throws(ServletException::class, IOException::class)
  override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
    if (request.getSession(false) == null)
      response.sendError(HttpServletResponse.SC_FORBIDDEN)
    val inputTextBody = request.getParameter("textarea")
    if (!inputTextBody.isEmpty()) {
      val noteList = NoteList.getNotes()
      val username: String = request.session.getAttribute("username") as String
      val note = Note(inputTextBody, User(username), LocalDateTime.now())
      NoteList.addNote(note)
    }
    response.sendRedirect("/list")
  }

  @Throws(ServletException::class, IOException::class)
  override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
    if (request.session.isNew) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN)
      return
    }
    val list = NoteList.getNotes()

    request.setAttribute("note_list", list)
    request.getRequestDispatcher("/WEB-INF/list.jsp").forward(request, response)
  }
}
