INSERT INTO registered_user (phone_number, nickname, password, register_date) VALUES (#{phone_number},#{nickname},#{password},#{register_date});


INSERT INTO authorities (phone_number, authority) VALUES (#{phone_number},#{authority});

INSERT INTO room_type (room_price, room_count) VALUES (#{room_price},#{room_count});

INSERT INTO rooms (room_id, room_type_id, room_floor, room_direction, room_specialty, romm_left) VALUES (#{room_id},#{room_type_id},#{room_floor},#{room_direction},#{room_specialty},#{romm_left});

INSERT INTO bookings (booking_id, phone_number, room_id, date_from, date_to) VALUES (#{booking_id},#{phone_number},#{room_id},#{date_from},#{date_to});