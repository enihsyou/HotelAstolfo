package dbo;
import model.Note;
import model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class NoteList {
  private static NoteList ourInstance = new NoteList();
  private final ArrayList<Note> notes = new ArrayList<>();
  private static final String JDBC_DB_URL = "jdbc:sqlite:D:/IntelliJProjects/JavaWeb/javaweb.db";

  public static NoteList getInstance() {
    return ourInstance;
  }

  private NoteList() {
    populateNotes();
  }

  private void populateNotes() {
    notes.add(new Note("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor", new User("user1"),
        LocalDateTime.now()));
    notes.add(new Note("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor", new User("user2"),
        LocalDateTime.now()));
    notes.add(new Note("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor", new User("user2"),
        LocalDateTime.now()));
    notes.add(new Note("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem ipsum dolor", new User("user3"),
        LocalDateTime.now()));
  }

  public void addNote(Note note) {
    notes.add(note);
    // Connection connection = null;
    // try {
    //   // create a database connection
    //   connection = DriverManager.getConnection(JDBC_DB_URL);
    //   Statement statement = connection.createStatement();
    //   statement.setQueryTimeout(10);  // set timeout to 30 sec.
    //   PreparedStatement preparedStatement =
    //       connection.prepareStatement("INSERT INTO note_app (author, note_body, create_time) VALUES (?, ? , ?)");
    //   preparedStatement.setString(1, note.getAuther().toString());
    //   preparedStatement.setString(2, note.getBody());
    //   preparedStatement.setString(3, note.getCreate_time().toString());
    //   preparedStatement.execute();
    //   connection.commit();
    //   ResultSet rs = statement.executeQuery("SELECT * FROM note_app");
    //   notes.clear();
    //   while (rs.next()) {
    //     // read the result set
    //     notes.add(new Note(rs.getString(1), new User(rs.getString(2)), LocalDateTime.parse(rs.getString(3))));
    //   }
    // } catch (SQLException e) {
    //   // if the error message is "out of memory",
    //   // it probably means no database file is found
    //   System.err.println(e.getMessage());
    // } finally {
    //   try {
    //     if (connection != null)
    //       connection.close();
    //   } catch (SQLException e) {
    //     // connection close failed.
    //     e.printStackTrace();
    //   }
    // }
  }

  public void addNote(String body, User author, LocalDateTime create_time) {
    addNote(new Note(body, author, create_time));
  }

  public ArrayList<Note> getNotes() {return notes;}
}
