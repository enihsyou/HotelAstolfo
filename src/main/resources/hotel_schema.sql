CREATE DATABASE IF NOT EXISTS astolfo_hotel
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE astolfo_hotel;
DROP TABLE IF EXISTS student CASCADE;

CREATE TABLE IF NOT EXISTS student (
  student_uid    INTEGER UNSIGNED AUTO_INCREMENT NOT NULL UNIQUE PRIMARY KEY COMMENT '学生学号，唯一编号',
  student_email  VARCHAR(100)                    NOT NULL COMMENT '学生邮箱，用于登录',
  student_name   VARCHAR(30) COMMENT '学生姓名',
  student_passwd VARCHAR(64) COMMENT 'SHA-256加密'
) COMMENT '学生表';
