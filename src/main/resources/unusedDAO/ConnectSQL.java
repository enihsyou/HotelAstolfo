/*
 * 
 * 连接mysql数据库
 * for istudy
 * lang:java
 * 
 */



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectSQL {

	// CREATE DATABASE istudy DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
	private static final String URL = "jdbc:mysql://localhost:3306/istudy?user=root&password=286956679hjy&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false";

	/**
	 * 创建连接mysql的对象
	 * @return Connection
	 */
	public Connection MySQLConnection() {
		// 加载驱动
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("未能成功加载驱动程序，请检查是否导入驱动程序！");
			e.printStackTrace();
		}
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL);
		} catch (SQLException e) {
			System.out.println("获取数据库连接失败！");
			e.printStackTrace();
		}
		return conn;
	}
	/**
	 * 在学生账号被创建的同时为其创建记事本
	 * @param s_id
	 */
	private void Insert(String s_id) {
		Connection connection=MySQLConnection();
		String sql="insert into notes(s_id,note) values(?,'null')";
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, s_id);
			p.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * insert into student,并且创建笔记本。
	 * @param student
	 * @return true if success 
	 */
	public boolean Insert(Student student) {
		Connection connection=MySQLConnection();
		String sql="insert into student (s_id,s_mail,s_name,s_passwd) values (?,?,?,?)";  
		int re=-1;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, student.getS_id());
			p.setString(2, student.getS_mail());
			p.setString(3, student.getS_Name());
			p.setString(4, student.getS_passwd());
			re=p.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		Insert(student.getS_id()); //创建记事本
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(re==1) return true;
		else return false;
	}
	/**
	 * insert into teacher
	 * @param teacher
	 * @return true if success
	 */
	public boolean Insert(Teacher teacher) {
		Connection connection=MySQLConnection();
		String sql="insert into teacher (t_id,t_mail,t_name,t_passwd) values (?,?,?,?)";  
		int re=-1;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, teacher.getT_id());
			p.setString(2, teacher.getT_mail());
			p.setString(3, teacher.getT_Name());
			p.setString(4, teacher.getT_passwd());
			re=p.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(re==1) return true;
		else return false;
	}
	/**
	 * insert into take_course
	 * @param take_course
	 * @return true if success
	 */
	public boolean Insert(TakeCourse take_course) {
		Connection connection=MySQLConnection();
		String sql="insert into take_course (s_id,t_id,u_grade,m_grade,f_grade) values (?,?,?,?,?)";  
		int re=-1;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, take_course.getS_id());
			p.setString(2, take_course.getT_id());
			p.setDouble(3, take_course.getU_grade());
			p.setDouble(4, take_course.getM_grade());
			p.setDouble(5, take_course.getF_score());
			re=p.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(re==1) return true;
		else return false;
	}
	/**
	 * insert into question
	 * @param question
	 * @return true if success
	 */
	public boolean Insert(Question question) {
		Connection connection=MySQLConnection();
		String sql="insert into question (content) values (?)";  
		int re=-1;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, question.getContent());
			re=p.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(re==1) return true;
		else return false;
	}
	/**
	 * insert into papers
	 * @param papers
	 * @return true if success
	 */
	public boolean Insert(Papers papers) {
		Connection connection=MySQLConnection();
		String sql="insert into papers (p_id,year,month,day) values (?,?,?,?)"; 
		int re=-1;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, papers.getP_id());
			p.setInt(2, papers.getYear());
			p.setInt(3, papers.getMonth());
			p.setInt(4, papers.getDay());
			re=p.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(re==1) return true;
		else return false;
	}
	/**
	 * 显示选课关系
	 * 按学号查询选课
	 * @param s_id
	 * @return ResultSet对象 ,有三个属性:s_id,s_name,t_name
	 */
	public ResultSet getSCBySID(String s_id) {
		Connection connection=MySQLConnection();
		String sql="select s_id, s_name ,t_name from student natural join take_course natural join teacher where s_id= ?";  
		ResultSet rs=null;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, s_id);
			rs=p.executeQuery();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * 显示选课关系
	 * 按教师查询选课，可以用来显示该老师班级下的所有学生
	 * @param name
	 * @return ResultSet对象 ,有三个属性:s_id,s_name,t_name
	 */
	public ResultSet getSCByTeacherName(String t_name) {
		Connection connection=MySQLConnection();
		String sql="select s_id, s_name ,t_name from student natural join take_course natural join teacher where t_name= ?";  
		ResultSet rs=null;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, t_name);
			rs=p.executeQuery();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * 显示选课关系
	 * 按学生姓名查询选课
	 * @param name
	 * @return ResultSet对象 ,有三个属性:s_id,s_name,t_name
	 */
	public ResultSet getSCByStudentName(String s_name) {
		Connection connection=MySQLConnection();
		String sql="select s_id, s_name ,t_name from student natural join take_course natural join teacher where s_name= ?";  
		ResultSet rs=null;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, s_name);
			rs=p.executeQuery();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * 获取学生对象的笔记内容
	 * @param student
	 * @return String 对象，值是该学生对象的笔记
	 */
	public String getNoteByStudent(Student student) {
		Connection connection=MySQLConnection();
		String sql="select note from notes where s_id= ?";  
		ResultSet rs=null;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, student.getS_id());
			rs=p.executeQuery();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		String note=null;
		try {
			while (rs.next()) {
				note=rs.getString("note");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return note;
		
	}
	/**
	 * 根据学生id，获取学生的笔记内容
	 * @param s_id
	 * @return String 对象，值是该学号学生的笔记。
	 */
	public String getNoteByS_id(String s_id) {
		Connection connection=MySQLConnection();
		String sql="select note from notes where s_id= ?";  
		ResultSet rs=null;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, s_id);
			rs=p.executeQuery();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		String note=null;
		try {
			while (rs.next()) {
				note=rs.getString("note");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return note;
		
	}
	/** 
	 * 更新学生账号密码
	 * @param s_id 学生账号id
	 * @param s_passwd  学生新密码
	 * @return true if success
	 */
	public boolean updateStudentPasswd(String s_id,String s_passwd) {
		Connection connection=MySQLConnection();
		String sql="update student set s_passwd=? where s_id= ?";
		int re=-1;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, s_passwd);
			p.setString(2, s_id);
			re=p.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		if(re==1) return true;
		else return false;
	}
	/**
	 * 依据学号更新该学生的笔记本.
	 * @param s_id
	 * @param note
	 * @return true if success
	 */
	public boolean updateNoteByS_id(String s_id,String note) {
		// TODO Auto-generated method stub
		Connection connection=MySQLConnection();
		String sql="update notes set note=? where s_id= ?";
		int re=-1;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, note);
			p.setString(2, s_id);
			re=p.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		if(re==1) return true;
		else return false;
	}
	/**
	 * 依据学生对象，更新该学生的记事本。
	 * @param student
	 * @param note
	 * @return
	 */
	public boolean updateNoteByStudent(Student student,String note) {
		// TODO Auto-generated method stub
		Connection connection=MySQLConnection();
		String sql="update notes set note=? where s_id= ?";
		int re=-1;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, note);
			p.setString(2, student.getS_id());
			re=p.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		if(re==1) return true;
		else return false;
	}
	/**
	 * 跟新老师账号信息
	 * @param t_id 老师账号id
	 * @param t_passwd 老师新密码
	 * @return
	 */
	public boolean updateTeacherPasswd(String t_id,String t_passwd) {
		Connection connection=MySQLConnection();
		String sql="update teacher set t_passwd=? where t_id= ?";
		int re=-1;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, t_passwd);
			p.setString(2, t_id);
			re=p.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		if(re==1) return true;
		else return false;
	}
	/**
	 * 删除学生账号
	 * @param s_id
	 * @return true if success
	 */
	public boolean delStudentAccount(String s_id)
	{
		Connection connection=MySQLConnection();
		String sql="delete from student where s_id=?";  
		int re=-1;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, s_id);
			re=p.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(re==1) return true;
		else return false;
	}
	/**
	 * 查询学生所选的所有课的成绩。
	 * @param s_id
	 * @return ResultSet 包含以下变量t_id t_name u_grade m_grade t_grade
	 */
	public ResultSet getMyScore(String s_id) {
		Connection connection=MySQLConnection();
		String sql="select t_id,t_name, u_grade ,m_grade,f_grade from student natural join take_course natural join teacher where s_id= ?";  
		ResultSet rs=null;
		try {
			PreparedStatement p=connection.prepareStatement(sql);
			p.setString(1, s_id);
			rs=p.executeQuery();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
}

/*
create table paper_XXXX
(index int primary key,
q_id varchar(20) references question(q_id));
*/