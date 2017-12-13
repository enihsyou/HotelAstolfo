CREATE DATABASE IF NOT EXISTS astolfo_hotel
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE astolfo_hotel;
DROP TABLE IF EXISTS registered_user CASCADE;
DROP TABLE IF EXISTS authorities CASCADE;

CREATE TABLE registered_user (
  phone_number  BIGINT UNSIGNED NOT NULL UNIQUE PRIMARY KEY COMMENT '用户手机号，可用于登录',
  nickname          VARCHAR(30) DEFAULT '' COMMENT '昵称',
  password      VARCHAR(64)     NOT NULL COMMENT 'SHA-256加密',
  register_date DATETIME    DEFAULT now(),
  CONSTRAINT UNIQUE (phone_number)
) COMMENT '注册用户';
CREATE TABLE authorities (
  phone_numbser BIGINT UNSIGNED NOT NULL UNIQUE,
  authority     ENUM ('管理员', '前台', '注册用户')
)
