package dbo

import model.Note
import model.User
import org.sqlite.SQLiteDataSource

import java.sql.*
import java.time.LocalDateTime
import java.util.ArrayList

class NoteList private constructor() {
  private val notes = ArrayList<Note>()
  private val dataSource: SQLiteDataSource

  init {
    try {
      DriverManager.registerDriver(org.sqlite.JDBC())
    } catch (e: SQLException) {
      e.printStackTrace()
    }

    dataSource = SQLiteDataSource()
    populateNotes()
  }

  private fun populateNotes() {
    dataSource.url = JDBC_DB_URL
    try {
      dataSource.connection.use { connection ->

        val s = connection.createStatement()
        val rs = s.executeQuery("SELECT * FROM note_app;")
        while (rs.next()) {
          notes.add(Note(rs.getString(3), User(rs.getString(2)), LocalDateTime.parse(rs.getString(4))))
        }
      }
    } catch (e: SQLException) {
      e.printStackTrace()
    }

  }

  private fun addNote(body: String, author: User, createTime: LocalDateTime) {
    try {
      dataSource.connection.use { connection ->

        val statement = connection.prepareStatement("INSERT INTO note_app(author, note_body, create_time) VALUES (?,?,?)")
        statement.setString(1, author.username)
        statement.setString(2, body)
        statement.setString(3, createTime.toString())
        statement.execute()
      }
    } catch (e: SQLException) {
      e.printStackTrace()
    }

  }

  companion object {
    val instance = NoteList()
    private val JDBC_DB_URL = "jdbc:sqlite:D:/IntelliJProjects/JavaWeb/javaweb.db"

    fun addNote(note: Note) {
      instance.notes.add(note)
      instance.addNote(note.body, note.author, note.createTime)
    }

    fun getNotes(): ArrayList<Note> {
      return instance.notes
    }
  }
}
