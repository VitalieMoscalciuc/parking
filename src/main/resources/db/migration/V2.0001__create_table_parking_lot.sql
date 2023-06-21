CREATE TABLE IF NOT EXISTS "parking_lot" (
    id SERIAL PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    number_of_levels INT NOT NULL,
    spaces_per_level INT NOT NULL,
    working_hours INT NOT NULL,
    state VARCHAR(50) CHECK (state IN ('open', 'temporary_closed')) NOT NULL
);

COMMENT ON COLUMN parking_lot.id IS 'This is the id of the parking lot so we can reference it in the parking_level table';
COMMENT ON COLUMN parking_lot.address IS 'This is the address of the parking lot';
COMMENT ON COLUMN parking_lot.name IS 'This is the name of the parking lot';
COMMENT ON COLUMN parking_lot.number_of_levels IS 'This is the number of levels (floors) in the parking lot';
COMMENT ON COLUMN parking_lot.spaces_per_level IS 'This is the number of space on each level';
COMMENT ON COLUMN parking_lot.working_hours IS 'This shows when the parking lot can be accessed';
COMMENT ON COLUMN parking_lot.state IS 'This is the state of the parking lot, open or temporary_closed';