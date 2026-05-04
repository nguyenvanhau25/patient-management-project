-- CREATE TABLE IF NOT EXISTS users (
--                                      id UUID PRIMARY KEY,
--                                      email VARCHAR(255) UNIQUE NOT NULL,
--     password VARCHAR(255) NOT NULL,
--     role VARCHAR(50) NOT NULL
--     );

INSERT INTO users (id, email, password, role)
SELECT '223e4567-e89b-12d3-a456-426614174006', 'admin1@test.com',
       '$2a$10$.DTflCUTY0sxGgWFVVID9.E7WQwEq/DnwZZMZP0EGYDKVFbgEPr4e', 'ADMIN'
    WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE email = 'admin1@test.com'
);

