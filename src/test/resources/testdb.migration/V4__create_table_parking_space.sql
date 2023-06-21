CREATE TABLE IF NOT EXISTS "parking_space" (
    id SERIAL PRIMARY KEY,
    number INT NOT NULL,
    type VARCHAR(255) DEFAULT 'regular' CHECK (type IN('regular', 'accessible', 'family')) NOT NULL,
    state VARCHAR(255) CHECK (state IN('available', 'occupied', 'temporarily closed')) NOT NULL,
    parking_level_id INT REFERENCES "parking_level"(id),
    user_id INT REFERENCES "user_table"(id) UNIQUE
);