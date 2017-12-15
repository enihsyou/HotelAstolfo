package com.enihsyou.astolfo.hotel.mybatis

import com.enihsyou.astolfo.hotel.domain.*
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper

interface InitNotes{
  @Insert("insert into notes(s_id,note) values(#{student_uid},'null')")
  fun initNoteByStudentID(student_uid:Int):Notes
}

interface InsertNotes{
  @Insert("insert into notes(s_id,note) values(#{student_uid},#{note})")
  fun initNoteByStudentID(student_uid: Int,note:String): Notes
}

interface InsertStudent {
  @Insert("insert into student (student_uid,student_email,student_name,student_passwd) values (#{student_uid},#{student_email},#{student_name},#{student_passwd})")
  fun insertStudent(student_uid:Int,student_email:String,student_name:String,student_passwd:String):Student
}

interface InsertTeacher{
  @Insert("insert into teacher (teacher_uid,teacher_email,teacher_name,teacher_passwd) values (#{teacher_uid},#{teacher_email},#{teacher_name},#{teacher_passwd})")
  fun insertTeacher(teacher_uid:Int, teacher_email:Int, teacher_name:String, teacher_passwd:String):Teacher
}



interface InsertTakeCourse{
  @Insert("insert into take_course (student_uid,teacher_uid,usual_behave_grade,master_test_grade,final_term_grade) values (#{student_uid},#{teacher_uid},#{usual_behave_grade},#{master_test_grade},#{final_term_grade})")
  fun insertTakeCourse(student_uid:Int, teacher_uid:Int, usual_behave_grade:Double, master_test_grade:Double, final_term_grade:Double):TakeCourse
}

interface InsertQuestion {
  @Insert("insert into question (content,answer) values (#{content},#{answer})")
  fun insertQuestion(content:String,answer:String):Question
}

//注意这里的的String 要改成DATETIME
interface InsertPaper{
  @Insert("insert into paper (paper_id) values (#{paper_id})")
  fun insertPaper(paper_id:Int, create_time:String, last_modification:String):Paper
}

interface InsertQuestionPaper{
  @Insert("insert into question__paper (question_id,paper_id,ind) values (#{question_id},#{paper_id},#{ind})")
  fun insertQuestionPaper(question_id:Int, paper_id:Int, ind:Int):QuestionPaper
}



//interface UserMapper {
//  @Select("SELECT * FROM student WHERE student_name = #{username}")
//  fun selectByUsername(username: String): User
//}

interface NotesMapper{
  @Select ("Select * FROM notes WHERE note =#{student_uid}")
  fun selectByQuestionID(student_uid:Int):Notes
}

interface  StudentMapper{
  @Select ("SELECT * FROM student WHERE student_uid= #{student_uid}")
  fun selectByStudentID(student_uid:Int): Student
}

interface TeacherMapper{
  @Select ("SELECT * FROM teacher WHERE teacher_uid= #{teacher_uid}")
  fun selectByTeacherID(teacher_uid:Int):Teacher
}

interface PaperMapper{
  @Select ("select * from paper where paper_id=#{paper_id}")
  fun selectByPaperID(paper_id: Int):Paper
}

interface QuestionMapper{
  @Select ("select * from question where question_id=#{question_id}")
  fun selectByQuestionID(question_id: Int):Question
}

interface QuestionPaperMapper{
  @Select ("select * from question__paper where question_id=#{question_id} and paper_id=#{paper_id}")
  fun selectByQuestionAndPaperID(question_id: Int,paper_id: Int):QuestionPaper
}


interface TakeCourseMapper{
  @Select ("select * from take_course where student_uid=#{student_uid} and teacher_uid=#{teacher_uid}")
  fun selectByStudentAndTeacherID(student_uid: Int,teacher_uid: Int):TakeCourse
}



UPDATE student SET student_passwd=#{ student_passwd } WHERE student_uid=#{ student_uid };
UPDATE teacher SET teacher_passwd=#{ student_passwd } WHERE teacher_uid=#{ student_uid };
UPDATE student SET student_email=#{ student_email } WHERE student_uid=#{ student_uid };
UPDATE teacher SET teacher_email=#{ teacher_email } WHERE teacher_uid=#{ teacher_uid };

DELETE FROM student WHERE student_uid=#{ student_uid };
DELETE FROM teacher WHERE teacher_uid=#{ teacher_uid };
//退课
DELETE FROM take_course WHERE student_uid=#{ student_uid } AND teacher_uid=#{ teacher_uid };




