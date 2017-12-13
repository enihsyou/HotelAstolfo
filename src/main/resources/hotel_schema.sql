CREATE DATABASE IF NOT EXISTS astolfo_hotel
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE astolfo_hotel;
DROP TABLE IF EXISTS registered_user CASCADE;
DROP TABLE IF EXISTS authorities CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS rooms CASCADE;
DROP TABLE IF EXISTS room_type CASCADE;
DROP TABLE IF EXISTS registered_user CASCADE;

CREATE TABLE registered_user (
  phone_number  BIGINT      NOT NULL UNIQUE PRIMARY KEY COMMENT '用户手机号，可用于登录',
  nickname      VARCHAR(30)                DEFAULT '' COMMENT '昵称',
  password      VARCHAR(64) NOT NULL COMMENT 'SHA-256加密',
  register_date DATETIME                   DEFAULT now(),
  user_role     INT DEFAULT 3,
  CONSTRAINT UNIQUE (phone_number),
  FOREIGN KEY (user_role) REFERENCES user_role (id)
) COMMENT '注册用户';

CREATE TABLE user_role (
  id   INT PRIMARY KEY,
  role VARCHAR(10)
);
CREATE TABLE room_type (
  room_type_id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '房间类型号',
  type_name    VARCHAR(10),
  type_price   BIGINT UNSIGNED                NOT NULL COMMENT '房间的价格',
  type_count   BIGINT UNSIGNED                NOT NULL COMMENT '房间的个数'
) COMMENT '房间类型';

CREATE TABLE rooms (
  room_id        BIGINT UNSIGNED                NOT NULL PRIMARY KEY COMMENT '房间的',
  room_type_id   BIGINT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '房间类型号',
  room_floor     INT UNSIGNED                   NOT NULL COMMENT '楼层',
  room_direction ENUM ('东', '西', '南', '北')      NOT NULL COMMENT '朝向',
  room_specialty VARCHAR(100) DEFAULT '' COMMENT '房间特点',
  romm_left      BOOLEAN      DEFAULT TRUE COMMENT '房间是否剩余',
  FOREIGN KEY (room_type_id) REFERENCES astolfo_hotel.room_type (room_type_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) COMMENT '房间';

CREATE TABLE bookings (
  booking_id   BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '预定号',
  phone_number BIGINT UNSIGNED                NOT NULL UNIQUE,
  room_id      BIGINT UNSIGNED                NOT NULL,
  date_from    DATE                           NOT NULL COMMENT '入住时间',
  date_to      DATE                           NOT NULL COMMENT '退房时间',
  FOREIGN KEY (phone_number) REFERENCES astolfo_hotel.registered_user (phone_number),
  FOREIGN KEY (room_id) REFERENCES astolfo_hotel.rooms (room_id)
);
