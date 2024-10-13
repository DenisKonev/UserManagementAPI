DROP TABLE IF EXISTS user_photos CASCADE;
DROP TABLE IF EXISTS contacts CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       middle_name VARCHAR(50),
                       date_of_birth DATE NOT NULL
);

CREATE TABLE contacts (
                          id SERIAL PRIMARY KEY,
                          user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          phone_number VARCHAR(20) NOT NULL
);

CREATE TABLE user_photos (
                             id SERIAL PRIMARY KEY,
                             user_id INTEGER NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                             photo BYTEA
);
