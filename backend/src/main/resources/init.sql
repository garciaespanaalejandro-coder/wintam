--CREATE DATABASE IF NOT EXISTS wintam
--    CHARACTER SET utf8mb4
--    COLLATE utf8mb4_unicode_ci;
--
--USE wintam;

CREATE TABLE IF NOT EXISTS users (
    id                BIGINT          AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(100)    NOT NULL,
    surname           VARCHAR(100)    NOT NULL,
    username          VARCHAR(50)     NOT NULL UNIQUE,
    email             VARCHAR(150)    NOT NULL UNIQUE,
    password_hash     VARCHAR(255)    NOT NULL,
    birthdate         DATE,
    description       TEXT,
    verification_code VARCHAR(10),
    is_verified       BOOLEAN         NOT NULL DEFAULT FALSE,
    role              ENUM('USER','ADMIN', 'BANNED') NOT NULL DEFAULT 'USER',
    karma             INT             NOT NULL DEFAULT 50,
    created_at        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS catas (
    id                BIGINT          AUTO_INCREMENT PRIMARY KEY,
    title             VARCHAR(150)    NOT NULL,
    wine_type         VARCHAR(100)    NOT NULL,
    experience_level  ENUM('BEGINNER','INTERMEDIATE','EXPERT') NOT NULL,
    location          VARCHAR(255)    NOT NULL,
    schedule_date     DATE            NOT NULL,
    scheduled_time    TIME            NOT NULL,
    max_attendees     INT             NOT NULL,
    status            ENUM('OPEN','FULL','CANCELLED','COMPLETED') NOT NULL DEFAULT 'OPEN',
    attendance_code   VARCHAR(5),
    code_generated_at DATETIME,
    host_id           BIGINT          NOT NULL,
    created_at        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cata_host FOREIGN KEY (host_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS inscripciones (
    id  BIGINT  AUTO_INCREMENT PRIMARY KEY,
    cata_id BIGINT  NOT NULL,
    player_id   BIGINT  NOT NULL,
    status      ENUM('CONFIRMED','CANCELLED','ATTENDED','NO_SHOW') NOT NULL DEFAULT 'CONFIRMED',
    created_at  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_inscripcion   UNIQUE (cata_id, player_id),
    CONSTRAINT fk_insc_cata     FOREIGN KEY (cata_id)   REFERENCES catas(id),
    CONSTRAINT fk_insc_player   FOREIGN KEY (player_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS reports (
    id            BIGINT   AUTO_INCREMENT PRIMARY KEY,
    reporter_id   BIGINT   NOT NULL,
    reported_id   BIGINT   NOT NULL,
    reason        TEXT     NOT NULL,
    sanction_type ENUM('WARNING','KARMA_PENALTY','BAN'),
    status        ENUM('PENDING','RESOLVED','DISMISSED') NOT NULL DEFAULT 'PENDING',
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_report_reporter FOREIGN KEY (reporter_id) REFERENCES users(id),
    CONSTRAINT fk_report_reported FOREIGN KEY (reported_id) REFERENCES users(id)
);

-- Impedir que un BANNED se inscriba
DELIMITER $$
CREATE TRIGGER check_user_not_banned
BEFORE INSERT ON inscripciones
FOR EACH ROW
BEGIN
  IF (SELECT role FROM users WHERE id = NEW.player_id) = 'BANNED' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Usuario baneado no puede inscribirse';
  END IF;
END$$
DELIMITER ;

-- Poner la cata como FULL cuando se llena
DELIMITER $$
CREATE TRIGGER check_cata_full
AFTER INSERT ON inscripciones
FOR EACH ROW
BEGIN
  DECLARE confirmados INT;
  DECLARE max_cap INT;
  SELECT COUNT(*) INTO confirmados FROM inscripciones
    WHERE cata_id = NEW.cata_id AND status = 'CONFIRMED';
  SELECT max_attendees INTO max_cap FROM catas WHERE id = NEW.cata_id;
  IF confirmados >= max_cap THEN
    UPDATE catas SET status = 'FULL' WHERE id = NEW.cata_id;
  END IF;
END$$
DELIMITER ;
