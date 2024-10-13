INSERT INTO users (first_name, last_name, middle_name, date_of_birth)
VALUES
    ('Иван', 'Иванов', 'Иванович', '1990-01-01'),
    ('Петр', 'Петров', 'Петрович', '1985-05-15'),
    ('Светлана', 'Сидорова', NULL, '1992-07-20');

INSERT INTO contacts (user_id, email, phone_number)
VALUES
    (1, 'ivan.ivanov@example.com', '+79001234567'),
    (2, 'petr.petrov@example.com', '+79007654321'),
    (3, 'svetlana.sidorova@example.com', '+79005553322');

INSERT INTO user_photos (user_id, photo)
VALUES
    (1, NULL),
    (2, NULL),
    (3, NULL);
