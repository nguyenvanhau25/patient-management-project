-- Ensure the 'users' table exists
CREATE TABLE IF NOT EXISTS "users" (
                                       id UUID PRIMARY KEY,
                                       email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
    );

INSERT INTO "users" (id, email, password, role)
SELECT '8a0b0c0d-e1f2-4c32-b1a0-d9e8f7a6b5d7', 'admin@example.com',
       '$2a$10$8.UnVuG9HHgffUDAlk8qnOyzR0W6NmK.5.5k.PZ/zO.J7Z.J.J.Ja', 'ADMIN'
    WHERE NOT EXISTS (
    SELECT 1
    FROM "users"
    WHERE id = '8a0b0c0d-e1f2-4c32-b1a0-d9e8f7a6b5d7'
       OR email = 'admin@example.com'
);


