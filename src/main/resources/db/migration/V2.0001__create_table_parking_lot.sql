CREATE TABLE IF NOT EXISTS "parking_lot" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL UNIQUE,
    begin_working_hour time(0) NOT NULL,
    end_working_hour time(0) NOT NULL,
    status_close BOOLEAN NOT NULL
);

COMMENT ON COLUMN parking_lot.id IS 'This is the id of the parking lot so we can reference it in the parking_level table';
COMMENT ON COLUMN parking_lot.address IS 'This is the address of the parking lot';
COMMENT ON COLUMN parking_lot.name IS 'This is the name of the parking lot';
COMMENT ON COLUMN parking_lot.status_close IS 'This is the state of the parking lot, open or temporary_closed';