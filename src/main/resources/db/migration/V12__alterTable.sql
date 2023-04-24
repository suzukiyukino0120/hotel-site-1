ALTER TABLE reservation_table DROP COLUMN zip_code;
ALTER TABLE reservation_table DROP COLUMN address;
ALTER TABLE reservation_table ADD checkin_time VARCHAR(5) NOT NULL;