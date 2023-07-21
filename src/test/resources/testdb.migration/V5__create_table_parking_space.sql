CREATE TABLE IF NOT EXISTS "parking_space"
(
    id               SERIAL PRIMARY KEY,
    number           INT NOT NULL,
    type             character CHECK (type IN ('REGULAR', 'ACCESSIBLE', 'FAMILY', 'TEMPORARILY_CLOSED')) NOT NULL,
    state            character CHECK (state IN ('AVAILABLE', 'OCCUPIED')) NOT NULL,
    parking_level_id INT REFERENCES "parking_level" (id),
    user_id          INT REFERENCES "user_table" (id) UNIQUE
);