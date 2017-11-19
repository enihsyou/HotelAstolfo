package web;

import dbo.NoteList;
import model.Note;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@WebServlet(name = "web.ListServlet", urlPatterns = {"/list", "/list.do"})
public class ListServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (request.getSession(false) == null)
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
    final String input_textarea = request.getParameter("textarea");
    if (!input_textarea.isEmpty()) {
      ArrayList<Note> list = NoteList.getInstance().getNotes();
      String username = "Anonymous";
      for (final Cookie cookie : request.getCookies()) {
        if ("username".equals(cookie.getName()))
          username = cookie.getValue();
      }
      final Note note = new Note(input_textarea, new User(username), LocalDateTime.now());
      list.add(note);
    }
    response.sendRedirect("/list");
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (request.getSession(false) == null) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    ArrayList<Note> list = NoteList.getInstance().getNotes();

    request.setAttribute("note_list", list);
    request.getRequestDispatcher("list.jsp").include(request, response);
  }
}
