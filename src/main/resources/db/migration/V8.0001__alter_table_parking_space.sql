ALTER TABLE parking_space
ADD COLUMN qr_code_id INT,
ADD CONSTRAINT fk_qr_code_table
    FOREIGN KEY (qr_code_id)
    REFERENCES qr_code_table (id);