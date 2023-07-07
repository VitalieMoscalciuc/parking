CREATE TABLE IF NOT EXISTS "user_parking_lot" (
    id      SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES "user_table" (id),
    lot_id  INT NOT NULL REFERENCES "parking_lot" (id)
);