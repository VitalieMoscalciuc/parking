CREATE TABLE IF NOT EXISTS "parking_level" (
    id SERIAL PRIMARY KEY,
    floor VARCHAR(1) NOT NULL,
    number_of_spaces INT NOT NULL,
    lot_id INT NOT NULL REFERENCES "parking_lot"(id)
);