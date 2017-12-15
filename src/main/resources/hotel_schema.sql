DROP DATABASE IF EXISTS astolfo_hotel;
CREATE DATABASE IF NOT EXISTS astolfo_hotel DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;

USE astolfo_hotel;
DROP TABLE IF EXISTS BOOKING_TRANSITION CASCADE;
DROP TABLE IF EXISTS ROOMS CASCADE;
DROP TABLE IF EXISTS REGISTERED_USER CASCADE;
DROP TABLE IF EXISTS GUEST CASCADE;
DROP TABLE IF EXISTS ROOM_TYPE CASCADE;
DROP TABLE IF EXISTS USER_ROLE CASCADE;

CREATE TABLE IF NOT EXISTS USER_ROLE (
  role_id INTEGER AUTO_INCREMENT,
  type    VARCHAR(10) NOT NULL,
  PRIMARY KEY (role_id),
  UNIQUE (role_id)
) COMMENT '用户权限分类';

CREATE TABLE IF NOT EXISTS GUEST (
  identity_serial VARCHAR(18) NOT NULL COMMENT '身份证',
  name            VARCHAR(10) NOT NULL COMMENT '姓名',
  PRIMARY KEY (identity_serial),
  UNIQUE (identity_serial)
) COMMENT '入住住户';

CREATE TABLE IF NOT EXISTS ROOM_TYPE (
  room_type_id MEDIUMINT AUTO_INCREMENT NOT NULL,
  type_name    VARCHAR(10)              NOT NULL COMMENT '房间类型',
  type_price   MEDIUMINT UNSIGNED       NOT NULL COMMENT '该类型价格',
  type_count   MEDIUMINT UNSIGNED       NOT NULL COMMENT '酒店里拥有个数',
  PRIMARY KEY (room_type_id)
) COMMENT '房间类型';

CREATE TABLE IF NOT EXISTS REGISTERED_USER (
  phone_number  VARCHAR(11) NOT NULL COMMENT '用户手机号，可用于登录',
  nickname      VARCHAR(30) COMMENT '昵称',
  password      VARCHAR(64) NOT NULL COMMENT 'SHA-256加密',
  register_date DATETIME DEFAULT now(),
  role          INTEGER,
  PRIMARY KEY (phone_number),
  UNIQUE (phone_number),
  FOREIGN KEY (role) REFERENCES USER_ROLE (role_id)
) COMMENT '注册用户';

CREATE TABLE IF NOT EXISTS ROOMS (
  room_id        INTEGER AUTO_INCREMENT    NOT NULL,
  room_type      MEDIUMINT                 NOT NULL COMMENT '房间类型',
  room_floor     SMALLINT UNSIGNED         NOT NULL COMMENT '楼层',
  room_direction ENUM ('东', '西', '南', '北') NOT NULL COMMENT '朝向',
  room_specialty VARCHAR(1000) DEFAULT '' COMMENT '房间特点',
  romm_occupied  BOOLEAN       DEFAULT FALSE COMMENT '房间是否剩余',
  PRIMARY KEY (room_id),
  FOREIGN KEY (room_type) REFERENCES ROOM_TYPE (room_type_id) ON DELETE CASCADE
) COMMENT '房间';

CREATE TABLE IF NOT EXISTS BOOKING_TRANSITION (
  booking_id        INTEGER AUTO_INCREMENT NOT NULL COMMENT '预定号',
  user_phone_number VARCHAR(11)            NOT NULL,
  room_id           INTEGER                NOT NULL,
  date_from         DATETIME               NOT NULL COMMENT '入住时间',
  date_to           DATETIME               NOT NULL COMMENT '退房时间',
  PRIMARY KEY (booking_id),
  FOREIGN KEY (user_phone_number) REFERENCES REGISTERED_USER (phone_number) ON DELETE CASCADE,
  FOREIGN KEY (room_id) REFERENCES ROOMS (room_id) ON DELETE CASCADE
) COMMENT '预订记录';

