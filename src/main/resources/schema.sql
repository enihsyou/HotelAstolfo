CREATE TABLE GUEST (
  id             INT          NOT NULL
    PRIMARY KEY,
  identification VARCHAR(255) NULL,
  name           VARCHAR(255) NULL
) ENGINE = InnoDB;

CREATE TABLE ROOM (
  id           INT          NOT NULL
    PRIMARY KEY,
  broken       BIT          NOT NULL,
  price        INT          NOT NULL,
  floor        INT          NOT NULL,
  number       INT          NOT NULL,
  specialty    VARCHAR(255) NULL,
  direction_id INT          NULL,
  type_id      INT          NULL
) ENGINE = InnoDB;

CREATE INDEX FKsrst2gf0qqqgtmsng9pblc1db ON ROOM (direction_id);

CREATE INDEX FKk3b610pxf7lxj9mxkoy0nqe56 ON ROOM (type_id);

CREATE TABLE ROOM_DIRECTION (
  id          INT          NOT NULL
    PRIMARY KEY,
  description VARCHAR(255) NULL,
  type        VARCHAR(255) NULL,
  CONSTRAINT UK_jmdguxbqlnl9x6m0eyatjewrl UNIQUE (type)
) ENGINE = InnoDB;

ALTER TABLE ROOM
  ADD CONSTRAINT FKsrst2gf0qqqgtmsng9pblc1db FOREIGN KEY (direction_id) REFERENCES ROOM_DIRECTION (id) ON DELETE CASCADE;

CREATE TABLE ROOM_DIRECTION_rooms (
  RoomDirection_id INT NOT NULL,
  rooms_id         INT NOT NULL,
  CONSTRAINT UK_p38oajnj2tvvdqw0wcnxms3jo UNIQUE (rooms_id),
  CONSTRAINT FKckq8rg38vnqstpug6lt2wb7hp FOREIGN KEY (RoomDirection_id) REFERENCES ROOM_DIRECTION (id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT FKa5daaacpr8af4a22virevu3u3 FOREIGN KEY (rooms_id) REFERENCES ROOM (id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE INDEX FKckq8rg38vnqstpug6lt2wb7hp ON ROOM_DIRECTION_rooms (RoomDirection_id);

CREATE TABLE ROOM_TYPE (
  id          INT          NOT NULL
    PRIMARY KEY,
  description VARCHAR(255) NULL,
  type        VARCHAR(255) NULL,
  CONSTRAINT UK_lqk3g6971q6kqnscfjon5eny3 UNIQUE (type)
) ENGINE = InnoDB;

ALTER TABLE ROOM
  ADD CONSTRAINT FKk3b610pxf7lxj9mxkoy0nqe56 FOREIGN KEY (type_id) REFERENCES ROOM_TYPE (id) ON DELETE CASCADE;

CREATE TABLE ROOM_TYPE_rooms (
  RoomType_id INT NOT NULL,
  rooms_id    INT NOT NULL,
  CONSTRAINT UK_7wewt56xuaqjf0k8d1nffcyux UNIQUE (rooms_id),
  CONSTRAINT FK1sh5rdq9dc7r66q5uuvhnsijb FOREIGN KEY (RoomType_id) REFERENCES ROOM_TYPE (id),
  CONSTRAINT FKckte7ji0ot8b85r0n8j49gmla FOREIGN KEY (rooms_id) REFERENCES ROOM (id)
) ENGINE = InnoDB;

CREATE INDEX FK1sh5rdq9dc7r66q5uuvhnsijb ON ROOM_TYPE_rooms (RoomType_id);

CREATE TABLE ROOM_transactions (
  Room_id         INT NOT NULL,
  transactions_id INT NOT NULL,
  CONSTRAINT UK_tg897lkiuhwbrsjtqtmlov6iq UNIQUE (transactions_id),
  CONSTRAINT FK4lsmu6y6nn99d832c8w44kj4p FOREIGN KEY (Room_id) REFERENCES ROOM (id)
) ENGINE = InnoDB;

CREATE INDEX FK4lsmu6y6nn99d832c8w44kj4p ON ROOM_transactions (Room_id);

CREATE TABLE TRANSACTION (
  id         INT      NOT NULL
    PRIMARY KEY,
  activated  BIT      NOT NULL,
  createDate DATETIME NULL,
  dateFrom   DATETIME NULL,
  dateTo     DATETIME NULL,
  used       BIT      NOT NULL,
  room_id    INT      NULL,
  user_id    INT      NULL,
  CONSTRAINT FKdqak08so2mpnsp1b90iumb2pb FOREIGN KEY (room_id) REFERENCES ROOM (id)
) ENGINE = InnoDB;

CREATE INDEX FKdqak08so2mpnsp1b90iumb2pb ON TRANSACTION (room_id);

CREATE INDEX FKcoscckqobgx9888voske63vsk ON TRANSACTION (user_id);

ALTER TABLE ROOM_transactions
  ADD CONSTRAINT FKcf21qgijd5pgfv3lco3ux58b8 FOREIGN KEY (transactions_id) REFERENCES TRANSACTION (id);

CREATE TABLE TRANSACTION_guests (
  transactions_id INT NOT NULL,
  guests_id       INT NOT NULL,
  CONSTRAINT FKb0db5dn6a0dv5dqf0e39hjicr FOREIGN KEY (transactions_id) REFERENCES TRANSACTION (id),
  CONSTRAINT FK7fd0sd1337bdtmb023mpahhsn FOREIGN KEY (guests_id) REFERENCES GUEST (id)
) ENGINE = InnoDB;

CREATE INDEX FKb0db5dn6a0dv5dqf0e39hjicr ON TRANSACTION_guests (transactions_id);

CREATE INDEX FK7fd0sd1337bdtmb023mpahhsn ON TRANSACTION_guests (guests_id);

CREATE TABLE USER (
  id            INT          NOT NULL
    PRIMARY KEY,
  nickname      VARCHAR(255) NULL,
  password      VARCHAR(255) NOT NULL,
  phone_number  VARCHAR(255) NOT NULL,
  register_date DATETIME     NULL,
  role          VARCHAR(255) NULL,
  CONSTRAINT UK_31rmpfll2abii4qbefw4b6o2p UNIQUE (phone_number)
) ENGINE = InnoDB;

ALTER TABLE TRANSACTION
  ADD CONSTRAINT FKcoscckqobgx9888voske63vsk FOREIGN KEY (user_id) REFERENCES USER (id);

CREATE TABLE USER_guests (
  user_id   INT NOT NULL,
  guests_id INT NOT NULL,
  CONSTRAINT FK3355ipfsx2x1c0osda5s4kqey FOREIGN KEY (user_id) REFERENCES USER (id),
  CONSTRAINT FKi950ua9s75dox94jy40rh05xu FOREIGN KEY (guests_id) REFERENCES GUEST (id)
) ENGINE = InnoDB;

CREATE INDEX FK3355ipfsx2x1c0osda5s4kqey ON USER_guests (user_id);

CREATE INDEX FKi950ua9s75dox94jy40rh05xu ON USER_guests (guests_id);

CREATE TABLE hibernate_sequence (
  next_val BIGINT NULL
) ENGINE = InnoDB;
