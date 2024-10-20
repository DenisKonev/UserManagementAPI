DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    created_at TIMESTAMP           NOT NULL,
    updated_at TIMESTAMP           NOT NULL
);

CREATE TABLE user_roles
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role_id INTEGER NOT NULL
);