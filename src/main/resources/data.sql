# INSERT INTO astolfo_hotel.hibernate_sequence (next_val)
# VALUES (1);

INSERT INTO astolfo_hotel.USER (id, nickname, password, phone_number, register_date, role)
VALUES
  (1, '老子最大', '124640bf2792a0cdce2c04e13326d67bf013bac6ce546616b04888e7c4e68631', '18888888888', '2017-12-30 14:20:07', '经理');
INSERT INTO astolfo_hotel.USER (id, nickname, password, phone_number, register_date, role)
VALUES
  (2, '前台小姐姐', '124640bf2792a0cdce2c04e13326d67bf013bac6ce546616b04888e7c4e68631', '17788888888', '2017-12-30 14:20:43', '前台');
INSERT INTO astolfo_hotel.USER (id, nickname, password, phone_number, register_date, role)
VALUES
  (3, '一娃', '124640bf2792a0cdce2c04e13326d67bf013bac6ce546616b04888e7c4e68631', '18800000001', '2017-12-30 14:21:22', '注册用户');
INSERT INTO astolfo_hotel.USER (id, nickname, password, phone_number, register_date, role)
VALUES
  (4, '二娃', '124640bf2792a0cdce2c04e13326d67bf013bac6ce546616b04888e7c4e68631', '18800000002', '2017-12-30 14:21:32', '注册用户');
INSERT INTO astolfo_hotel.USER (id, nickname, password, phone_number, register_date, role)
VALUES
  (5, '三娃', '124640bf2792a0cdce2c04e13326d67bf013bac6ce546616b04888e7c4e68631', '18800000003', '2017-12-30 14:21:38', '注册用户');

INSERT INTO astolfo_hotel.ROOM_TYPE (id, description, type)
VALUES (6, '冬暖夏狼', '大床房');
INSERT INTO astolfo_hotel.ROOM_DIRECTION (id, description, type)
VALUES (7, '车万', '东方');

INSERT INTO astolfo_hotel.ROOM (id, broken, createdDate, price, floor, number, specialty, direction_id, type_id)
VALUES
  (8, FALSE, 0xACED00057372000D6A6176612E74696D652E536572955D84BA1B2248B20C00007870770E05000007E10C1E0E201B111B45C078, 143, 1, 1, '小房间', 7, 6);
INSERT INTO astolfo_hotel.ROOM (id, broken, createdDate, price, floor, number, specialty, direction_id, type_id)
VALUES
  (9, FALSE, 0xACED00057372000D6A6176612E74696D652E536572955D84BA1B2248B20C00007870770E05000007E10C1E0E20290D3B738078, 133, 1, 2, '小房间', 7, 6);
INSERT INTO astolfo_hotel.ROOM (id, broken, createdDate, price, floor, number, specialty, direction_id, type_id)
VALUES
  (10, FALSE, 0xACED00057372000D6A6176612E74696D652E536572955D84BA1B2248B20C00007870770E05000007E10C1E0E21333AC52A8078, 123, 1, 3, '小房间', 7, 6);
INSERT INTO astolfo_hotel.ROOM (id, broken, createdDate, price, floor, number, specialty, direction_id, type_id)
VALUES
  (11, TRUE, 0xACED00057372000D6A6176612E74696D652E536572955D84BA1B2248B20C00007870770E05000007E10C1E0E220524A827C078, 123, 1, 4, '小房间', 7, 6);

INSERT INTO astolfo_hotel.GUEST (id, identification, name)
VALUES (12, '111111200001013333', '宋娘');
INSERT INTO astolfo_hotel.GUEST (id, identification, name)
VALUES (13, '111111200001013334', '宋二娘');
INSERT INTO astolfo_hotel.GUEST (id, identification, name)
VALUES (14, '111111200001013335', '宋三娘');
INSERT INTO astolfo_hotel.GUEST (id, identification, name)
VALUES (15, '15616516516161161X', '送终机');
INSERT INTO astolfo_hotel.GUEST (id, identification, name)
VALUES (16, '156165161611616161', 'Mick');
INSERT INTO astolfo_hotel.GUEST (id, identification, name)
VALUES (17, '156156161616112313', 'jaky');
INSERT INTO astolfo_hotel.GUEST (id, identification, name)
VALUES (18, '213123123123123123', 'qweq');
INSERT INTO astolfo_hotel.GUEST (id, identification, name)
VALUES (19, '211651565161616516', 'qweqweqw');

INSERT INTO astolfo_hotel.TRANSACTION (id, activated, createdDate, dateFrom, dateTo, used, comment_id, room_id, user_id)
VALUES (1, TRUE, '2017-12-31 14:29:14', '2017-12-31 00:00:00', '2018-01-01 00:00:00', FALSE, NULL, 8, 3);
INSERT INTO astolfo_hotel.TRANSACTION (id, activated, createdDate, dateFrom, dateTo, used, comment_id, room_id, user_id)
VALUES (2, TRUE, '2017-12-31 14:39:41', '2017-12-31 00:00:00', '2018-01-01 00:00:00', FALSE, NULL, 8, 3);
INSERT INTO astolfo_hotel.TRANSACTION (id, activated, createdDate, dateFrom, dateTo, used, comment_id, room_id, user_id)
VALUES (3, TRUE, '2017-12-31 14:39:50', '2017-12-31 00:00:00', '2018-01-01 00:00:00', FALSE, NULL, 8, 3);
INSERT INTO astolfo_hotel.TRANSACTION (id, activated, createdDate, dateFrom, dateTo, used, comment_id, room_id, user_id)
VALUES (4, TRUE, '2017-12-31 14:39:53', '2017-12-31 00:00:00', '2018-01-01 00:00:00', FALSE, NULL, 8, 3);

INSERT INTO astolfo_hotel.TRANSACTION_guests (transactions_id, guests_id)
VALUES (1, 13);
INSERT INTO astolfo_hotel.TRANSACTION_guests (transactions_id, guests_id)
VALUES (2, 16);
INSERT INTO astolfo_hotel.TRANSACTION_guests (transactions_id, guests_id)
VALUES (3, 16);
INSERT INTO astolfo_hotel.TRANSACTION_guests (transactions_id, guests_id)
VALUES (3, 17);
INSERT INTO astolfo_hotel.TRANSACTION_guests (transactions_id, guests_id)
VALUES (4, 16);
INSERT INTO astolfo_hotel.TRANSACTION_guests (transactions_id, guests_id)
VALUES (4, 17);

INSERT INTO astolfo_hotel.ROOM_transactions (Room_id, transactions_id)
VALUES (8, 1);
INSERT INTO astolfo_hotel.ROOM_transactions (Room_id, transactions_id)
VALUES (8, 2);
INSERT INTO astolfo_hotel.ROOM_transactions (Room_id, transactions_id)
VALUES (8, 3);
INSERT INTO astolfo_hotel.ROOM_transactions (Room_id, transactions_id)
VALUES (8, 4);

INSERT INTO astolfo_hotel.COMMENT (id, body, createdDate, user_id)
VALUES (1, 'abc', '2017-12-30 15:21:18', 3);
INSERT INTO astolfo_hotel.ROOM_comments (Room_id, comments_id)
VALUES (8, 1);
INSERT INTO astolfo_hotel.USER_guests (user_id, guests_id)
VALUES (4, 14);
INSERT INTO astolfo_hotel.USER_guests (user_id, guests_id)
VALUES (5, 12);
INSERT INTO astolfo_hotel.USER_guests (user_id, guests_id)
VALUES (3, 12);
INSERT INTO astolfo_hotel.USER_guests (user_id, guests_id)
VALUES (3, 13);
INSERT INTO astolfo_hotel.USER_guests (user_id, guests_id)
VALUES (3, 15);
INSERT INTO astolfo_hotel.USER_guests (user_id, guests_id)
VALUES (3, 16);
INSERT INTO astolfo_hotel.USER_guests (user_id, guests_id)
VALUES (3, 17);
