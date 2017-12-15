CREATE DATABASE IF NOT EXISTS istudy
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE istudy;
DROP TABLE IF EXISTS student CASCADE;
DROP TABLE IF EXISTS teacher CASCADE;
DROP TABLE IF EXISTS take_course CASCADE;
DROP TABLE IF EXISTS question CASCADE;
DROP TABLE IF EXISTS paper CASCADE;
DROP TABLE IF EXISTS notes CASCADE;

CREATE TABLE IF NOT EXISTS student (
  student_uid    INTEGER UNSIGNED AUTO_INCREMENT NOT NULL UNIQUE PRIMARY KEY COMMENT '学生学号，唯一编号',
  student_email  VARCHAR(100)                    NOT NULL COMMENT '学生邮箱，用于登录',
  student_name   VARCHAR(30) COMMENT '学生姓名',
  student_passwd VARCHAR(64) COMMENT 'SHA-256加密'
) COMMENT '学生表';

CREATE TABLE IF NOT EXISTS teacher (
  teacher_uid    INTEGER UNSIGNED AUTO_INCREMENT NOT NULL UNIQUE PRIMARY KEY COMMENT '老师学号，唯一编号',
  teacher_email  VARCHAR(100)                    NOT NULL COMMENT '老师邮箱，用于登录',
  teacher_name   VARCHAR(30) COMMENT '老师姓名',
  teacher_passwd VARCHAR(64) COMMENT 'SHA-256加密'
) COMMENT '教师表';

CREATE TABLE IF NOT EXISTS take_course (
  student_uid        INTEGER UNSIGNED NOT NULL,
  teacher_uid        INTEGER UNSIGNED NOT NULL,
  usual_behave_grade DOUBLE COMMENT '平时成绩',
  master_test_grade  DOUBLE COMMENT '期末成绩',
  final_term_grade   DOUBLE COMMENT '总评成绩',
  CONSTRAINT PRIMARY KEY (student_uid, teacher_uid),
  CONSTRAINT FOREIGN KEY (student_uid) REFERENCES student (student_uid)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT FOREIGN KEY (teacher_uid) REFERENCES teacher (teacher_uid)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) COMMENT '记录学生登记的课程';

CREATE TABLE IF NOT EXISTS question (
  question_id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL UNIQUE PRIMARY KEY COMMENT '问题编号',
  content     VARCHAR(2000)                   NOT NULL COMMENT '问题题目，或者说主题内容',
  answer      VARCHAR(2000) COMMENT '问题答案，主观题就空吧'
) COMMENT '考试题库';

CREATE TABLE IF NOT EXISTS paper (
  paper_id          INTEGER UNSIGNED AUTO_INCREMENT NOT NULL UNIQUE PRIMARY KEY COMMENT '试卷编号',
  create_time       DATETIME DEFAULT now() COMMENT '试卷创建时间，每次添加的时候自动生成',
  last_modification DATETIME DEFAULT now() ON UPDATE now() COMMENT '试卷最后修订时间，自动更新'
) COMMENT '出题试卷';

CREATE TABLE question__paper (
  question_id INTEGER UNSIGNED NOT NULL,
  paper_id    INTEGER UNSIGNED NOT NULL,
  ind INTEGER UNSIGNED NOT NULL, PRIMARY KEY (question_id, paper_id),
  CONSTRAINT FOREIGN KEY (question_id) REFERENCES istudy.question (question_id),
  CONSTRAINT FOREIGN KEY (paper_id) REFERENCES istudy.paper (paper_id)
) COMMENT '将试卷和试题关联';
CREATE TABLE IF NOT EXISTS notes (
  student_uid INTEGER UNSIGNED AUTO_INCREMENT NOT NULL ,
  note        VARCHAR(10000),
  PRIMARY KEY (student_uid),
  FOREIGN KEY (student_uid) REFERENCES student (student_uid)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


UPDATE student SET student_passwd=#{student_passwd} WHERE student_uid=#{student_uid};
UPDATE teacher SET teacher_passwd=#{student_passwd} WHERE teacher_uid=#{student_uid};
UPDATE student SET student_email=#{student_email} WHERE student_uid=#{student_uid};
UPDATE teacher SET teacher_email=#{teacher_email} WHERE teacher_uid=#{teacher_uid};

DELETE FROM student WHERE student_uid=#{student_uid};
DELETE FROM teacher WHERE teacher_uid=#{teacher_uid};
//退课
DELETE FROM take_course WHERE student_uid=#{student_uid} AND teacher_uid=#{teacher_uid};





