# INSERT INTO registered_user (phone_number, nickname, password) VALUES
#   (18888888888, '我是第一个注册的', sha2('password', 256));
INSERT INTO user_role (id, type) VALUES (1, '管理员');
INSERT INTO user_role (id, type) VALUES (2, '前台');
INSERT INTO user_role (id, type) VALUES (3, '注册用户');
