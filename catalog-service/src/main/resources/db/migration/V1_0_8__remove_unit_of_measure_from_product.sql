-- Remove unit_of_measure_id from product table
ALTER TABLE product DROP COLUMN IF EXISTS unit_of_measure_id;
