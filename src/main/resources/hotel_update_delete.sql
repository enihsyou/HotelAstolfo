
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