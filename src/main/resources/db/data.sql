INSERT INTO users (username, email, password, created_at, updated_at)
VALUES ('Иван Иванов', 'ivan.ivanov@example.com', 'encrypted_password1', NOW(), NOW()),
       ('Петр Петров', 'petr.petrov@example.com', 'encrypted_password2', NOW(), NOW()),
       ('Светлана Сидорова', 'svetlana.sidorova@example.com', 'encrypted_password3', NOW(), NOW());

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (3, 3);
