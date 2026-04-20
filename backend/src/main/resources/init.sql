-- ============================================================
--  Wintam · Base de datos inicial
--  Motor: MySQL 8+
--  Ejecutar una sola vez al arrancar el proyecto
-- ============================================================

CREATE DATABASE IF NOT EXISTS wintam
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE wintam;

-- ── USERS ────────────────────────────────────────────────────
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
    role              ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER',
    karma             INT             NOT NULL DEFAULT 50,
    created_at        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ── CATAS ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS catas (
    id                BIGINT          AUTO_INCREMENT PRIMARY KEY,
    title             VARCHAR(150)    NOT NULL,
    wine_type         VARCHAR(100)    NOT NULL,          -- temática / tipo de vino
    experience_level  ENUM('BEGINNER','INTERMEDIATE','EXPERT') NOT NULL,
    location          VARCHAR(255)    NOT NULL,
    schedule_date     DATE            NOT NULL,
    scheduled_time    TIME            NOT NULL,
    max_attendees     INT             NOT NULL,
    status            ENUM('OPEN','FULL','CANCELLED','COMPLETED') NOT NULL DEFAULT 'OPEN',
    attendance_code   VARCHAR(5),                        -- código 5 dígitos RF_4.1
    code_generated_at DATETIME,
    host_id           BIGINT          NOT NULL,
    created_at        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cata_host FOREIGN KEY (host_id) REFERENCES users(id)
);

-- ── INSCRIPCIONES ────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS inscripciones (
    id          BIGINT      AUTO_INCREMENT PRIMARY KEY,
    cata_id     BIGINT      NOT NULL,
    player_id   BIGINT      NOT NULL,
    status      ENUM('CONFIRMED','CANCELLED','ATTENDED','NO_SHOW') NOT NULL DEFAULT 'CONFIRMED',
    created_at  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Un usuario no puede inscribirse dos veces en la misma cata (RF_3.3)
    CONSTRAINT uq_inscripcion   UNIQUE (cata_id, player_id),
    CONSTRAINT fk_insc_cata     FOREIGN KEY (cata_id)   REFERENCES catas(id),
    CONSTRAINT fk_insc_player   FOREIGN KEY (player_id) REFERENCES users(id)
);

-- ── REPORTS ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS reports (
    id           BIGINT   AUTO_INCREMENT PRIMARY KEY,
    reporter_id  BIGINT   NOT NULL,
    reported_id  BIGINT   NOT NULL,
    reason       TEXT     NOT NULL,
    status       ENUM('PENDING','RESOLVED','DISMISSED') NOT NULL DEFAULT 'PENDING',
    created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_report_reporter FOREIGN KEY (reporter_id) REFERENCES users(id),
    CONSTRAINT fk_report_reported FOREIGN KEY (reported_id) REFERENCES users(id)
);

-- ── USUARIO ADMIN POR DEFECTO ─────────────────────────────────
-- Contraseña: admin1234  (BCrypt · cámbiala en producción)
INSERT INTO users (name, surname, username, email, password_hash, is_verified, role, karma)
VALUES (
    'Admin',
    'CatApp',
    'admin',
    'admin@wintam.com',
    '$2a$10$7QBBBmXxXQ6JpRBc1W1l3.gkI7H/ZxW6w5J5K8rN2eYqM0Pf3u0Oy',
    TRUE,
    'ADMIN',
    100
);