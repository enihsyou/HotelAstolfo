CREATE DATABASE IF NOT EXISTS astolfo_hotel
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE astolfo_hotel;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS authorities CASCADE;
DROP TABLE IF EXISTS rooms CASCADE;
DROP TABLE IF EXISTS room_type CASCADE ;
DROP TABLE IF EXISTS registered_user CASCADE;

CREATE TABLE registered_user (
  phone_number  BIGINT UNSIGNED NOT NULL UNIQUE PRIMARY KEY COMMENT '用户手机号，可用于登录',
  nickname          VARCHAR(30) DEFAULT '' COMMENT '昵称',
  password      VARCHAR(64)     NOT NULL COMMENT 'SHA-256加密',
  register_date DATETIME    DEFAULT now(),
  CONSTRAINT UNIQUE (phone_number)
) COMMENT '注册用户';

CREATE TABLE authorities (
  phone_number BIGINT UNSIGNED NOT NULL UNIQUE,
  authority     ENUM ('管理员', '前台', '注册用户')
);

CREATE TABLE room_type (
  room_type_id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '房间类型号',
  room_price BIGINT UNSIGNED NOT NULL COMMENT '房间的价格',
  room_count BIGINT UNSIGNED NOT NULL COMMENT '房间的个数'
)COMMENT '房间类型';

CREATE TABLE rooms(
  room_id BIGINT UNSIGNED NOT NULL PRIMARY KEY COMMENT '房间的',
  room_type_id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '房间类型号',
  room_floor INT UNSIGNED NOT NULL COMMENT '楼层',
  room_direction VARCHAR(10) NOT NULL COMMENT '朝向',
  room_specialty VARCHAR(100) DEFAULT '' COMMENT '房间特点',
  romm_left BOOLEAN DEFAULT TRUE COMMENT '房间是否剩余',
  FOREIGN KEY (room_type_id) REFERENCES astolfo_hotel.room_type (room_type_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)COMMENT '房间';

CREATE TABLE bookings(
  booking_id BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '预定号',
  phone_number BIGINT UNSIGNED NOT NULL UNIQUE ,
  room_id BIGINT UNSIGNED NOT NULL ,
  date_from DATE NOT NULL COMMENT '入住时间',
  date_to DATE NOT NULL COMMENT '退房时间',
  FOREIGN KEY (phone_number) REFERENCES astolfo_hotel.registered_user(phone_number),
  FOREIGN KEY (room_id) REFERENCES astolfo_hotel.rooms(room_id)
);

--更具手机号获取职员职位
SELECT authority FROM authorities where phone_number=#{phone_number};

--更具手机号获取登录密码，用于登录
SELECT password FROM registered_user WHERE phone_number=#{phone_number};
--同上，使用nickname  修改一下DDL 其中nickname我忘记写unique了
SELECT password FROM  registered_user WHERE nickname=#{nickname};

--获取表的所有信息
SELECT * FROM registered_user WHERE nickname=#{nickname};
SELECT * FROM registered_user WHERE phone_number = #{phone_number};
SELECT * FROM room_type;





--查询适合的房间
SELECT room_id FROM rooms WHERE room_left=TRUE AND room_direction=#{room_direction} AND room_type_id=#{room_type_id};
SELECT room_id FROM rooms WHERE room_left=TRUE AND room_direction=#{room_direction};


--根据手机号查询旅客定的房间
SELECT room_id FROM bookings WHERE phone_number=#{phone_number};


--根据订房的编号查询旅客定的房间
SELECT room_id FROM bookings WHERE booking_id=#{booking_id};







INSERT INTO registered_user (phone_number, nickname, password, register_date) VALUES (#{phone_number},#{nickname},#{password},#{register_date});

INSERT INTO authorities (phone_number, authority) VALUES (#{phone_number},#{authority});

INSERT INTO room_type (room_price, room_count) VALUES (#{room_price},#{room_count});

INSERT INTO rooms (room_id, room_type_id, room_floor, room_direction, room_specialty, romm_left) VALUES (#{room_id},#{room_type_id},#{room_floor},#{room_direction},#{room_specialty},#{romm_left});

INSERT INTO bookings (booking_id, phone_number, room_id, date_from, date_to) VALUES (#{booking_id},#{phone_number},#{room_id},#{date_from},#{date_to});





-- 依据手机号解雇员工
DELETE FROM authorities WHERE phone_number=#{phone_number};

-- 依据手机号删除用户
DELETE FROM registered_user WHERE phone_number=#{phone_number};

-- 依据nickname删除用户
DELETE FROM registered_user where nickname=#{nickname};

-- 依据手机号修改员工职位
UPDATE authorities SET authority=#{authority} WHERE phone_number=#{phone_number};

-- 修改员工的手机号
UPDATE authorities SET phone_number = #{new_phone_number} WHERE phone_number = #{old_phone_number};

-- 依据手机号修改用户密码
UPDATE registered_user SET password=#{password} WHERE phone_number=#{phone_number};

-- 依据nickname修改用户密码
UPDATE registered_user SET password=#{password} WHERE nickname=#{nickname};

-- 修改用户的手机号
UPDATE registered_user SET phone_number=#{new_phone_number} WHERE phone_number=#{old_phone_number};

-- 修改用户的nickname
-- 由于设置了nickname->unique 所以如果同名会直接出错
UPDATE registered_user SET nickname=#{new_nickname} WHERE nickname=#{old_nickname};

-- 取消预定
DELETE FROM bookings WHERE booking_id=#{booking_id};

-- 修改预定信息
UPDATE bookings SET room_id=#{room_id} WHERE booking_id=#{booking_id};
UPDATE bookings SET date_from=#{date_from} WHERE booking_id=#{booking_id};
UPDATE bookings SET date_to = #{date_to} WHERE booking_id = #{booking_id};

-- 旅客定好房间后，房间显示为已被预定
UPDATE rooms SET romm_left=FALSE WHERE room_id=#{room_id};

-- 旅客取消或者退房后，房间显示可以被定
UPDATE rooms SET romm_left=TRUE  WHERE room_id=#{room_id};

-- 奸商表示要修改房间价格
UPDATE room_type SET room_price=#{room_price} WHERE room_type_id=#{room_type_id};


-- 查询某日有哪些空房间可以预定
SELECT room_id FROM  rooms WHERE room_id NOT IN
  (SELECT room_id FROM bookings WHERE #{date}>bookings.date_to AND #{date}<=bookings.date_from);
