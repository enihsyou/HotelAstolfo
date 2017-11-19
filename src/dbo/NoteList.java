package dbo;

import model.Note;
import model.User;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class NoteList {
  private static NoteList ourInstance = new NoteList();
  private final ArrayList<Note> notes = new ArrayList<>();
  private static final String JDBC_DB_URL = "jdbc:sqlite:D:/IntelliJProjects/JavaWeb/javaweb.db";
  private final SQLiteDataSource dataSource;

  public static NoteList getInstance() {
    return ourInstance;
  }

  private NoteList() {
    try {
      DriverManager.registerDriver(new org.sqlite.JDBC());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    dataSource = new SQLiteDataSource();
    populateNotes();
  }

  private void populateNotes() {
    dataSource.setUrl(JDBC_DB_URL);
    try (Connection connection = dataSource.getConnection()) {
      System.out.println("connected");

      Statement s = connection.createStatement();
      final ResultSet rs = s.executeQuery("SELECT * FROM note_app;");
      while (rs.next()) {
        notes.add(new Note(rs.getString(3), new User(rs.getString(2)), LocalDateTime.parse(rs.getString(4))));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void addNote(Note note) {
    getInstance().notes.add(note);
    getInstance().addNote(note.getBody(), note.getAuthor(), note.getCreateTime());
  }

  private void addNote(String body, User author, LocalDateTime createTime) {
    try (Connection connection = dataSource.getConnection()) {
      System.out.println("connected");

      final PreparedStatement statement =
          connection.prepareStatement("INSERT INTO note_app(author, note_body, create_time) VALUES (?,?,?)");
      statement.setString(1, author.getUsername());
      statement.setString(2, body);
      statement.setString(3, createTime.toString());
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static ArrayList<Note> getNotes() {return getInstance().notes;}
}
