CREATE TABLE IF NOT EXISTS "parking_lot" (
    id                 SERIAL PRIMARY KEY,
    name               VARCHAR(255) NOT NULL UNIQUE,
    address            VARCHAR(255) NOT NULL UNIQUE,
    begin_working_hour time(0)      NOT NULL,
    end_working_hour   time(0)      NOT NULL,
    status_close       BOOLEAN      NOT NULL
);