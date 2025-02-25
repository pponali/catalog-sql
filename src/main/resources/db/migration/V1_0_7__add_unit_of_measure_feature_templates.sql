-- Add unit of measure feature templates
INSERT INTO feature_template (id, code, name, description, data_type, created_date, last_modified_date, created_by, last_modified_by)
VALUES 
(gen_random_uuid(), 'UNIT_OF_MEASURE', 'Unit of Measure', 'The unit in which the product is measured', 'ENUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(gen_random_uuid(), 'QUANTITY_PER_UNIT', 'Quantity Per Unit', 'The quantity of the product per unit', 'DECIMAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Add common unit of measure enum values
INSERT INTO enum_value (id, code, name, description, created_date, last_modified_date, created_by, last_modified_by)
VALUES 
-- Weight units
(gen_random_uuid(), 'KILOGRAM', 'Kilogram', 'Metric unit of mass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(gen_random_uuid(), 'GRAM', 'Gram', 'Metric unit of mass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Volume units
(gen_random_uuid(), 'LITER', 'Liter', 'Metric unit of volume', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(gen_random_uuid(), 'MILLILITER', 'Milliliter', 'Metric unit of volume', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Count units
(gen_random_uuid(), 'PIECE', 'Piece', 'Individual unit', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(gen_random_uuid(), 'PACK', 'Pack', 'Package unit', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Length units
(gen_random_uuid(), 'METER', 'Meter', 'Metric unit of length', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(gen_random_uuid(), 'CENTIMETER', 'Centimeter', 'Metric unit of length', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Area units
(gen_random_uuid(), 'SQUARE_METER', 'Square Meter', 'Metric unit of area', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Jewelry specific
(gen_random_uuid(), 'CARAT', 'Carat', 'Unit of mass for gemstones', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');
