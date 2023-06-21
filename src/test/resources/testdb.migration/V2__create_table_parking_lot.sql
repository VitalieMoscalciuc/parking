CREATE TABLE IF NOT EXISTS "parking_lot" (
    id SERIAL PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    number_of_levels INT NOT NULL,
    spaces_per_level INT NOT NULL,
    working_hours INT NOT NULL,
    state VARCHAR(50) CHECK (state IN ('open', 'temporary_closed')) NOT NULL
);