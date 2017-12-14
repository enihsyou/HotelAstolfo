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
