CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    first_name    VARCHAR(50)         NOT NULL,
    last_name     VARCHAR(50)         NOT NULL,
    middle_name   VARCHAR(50),
    date_of_birth DATE                NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    phone_number  VARCHAR(20)         NOT NULL,
    photo         BYTEA
);
