CREATE TABLE IF NOT EXISTS "user_table" (
    id SERIAL PRIMARY KEY ,
    name VARCHAR(30) NOT NULL,
    password VARCHAR(10) NOT NULL,
    phone_number VARCHAR(9) NOT NULL,
    email VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT true,
    CONSTRAINT name_length CHECK (LENGTH(name) <= 30),
    CONSTRAINT password_length CHECK (LENGTH(password) >= 5 AND LENGTH(password) <= 10),
    CONSTRAINT phone_number_length CHECK (LENGTH(phone_number) = 9)
);