-- Add new columns to unit_of_measure table
ALTER TABLE unit_of_measure
    ALTER COLUMN code SET NOT NULL,
    ALTER COLUMN name SET NOT NULL,
    ALTER COLUMN conversion_factor SET NOT NULL,
    ADD COLUMN IF NOT EXISTS type VARCHAR(50) NOT NULL DEFAULT 'LENGTH',
    ADD COLUMN IF NOT EXISTS display_symbol VARCHAR(20),
    ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT true,
    ADD COLUMN IF NOT EXISTS metadata JSONB;

-- Add unique constraint to code
ALTER TABLE unit_of_measure
    ADD CONSTRAINT uk_unit_of_measure_code UNIQUE (code);

-- Create index on commonly queried columns
CREATE INDEX IF NOT EXISTS idx_unit_of_measure_type ON unit_of_measure (type);
CREATE INDEX IF NOT EXISTS idx_unit_of_measure_active ON unit_of_measure (active);

-- Add some basic unit of measure data if not exists
INSERT INTO unit_of_measure (id, code, name, description, base_unit, conversion_factor, type, display_symbol, active)
VALUES
    (gen_random_uuid(), 'M', 'Meter', 'Standard unit of length', 'M', 1.0, 'LENGTH', 'm', true),
    (gen_random_uuid(), 'CM', 'Centimeter', '1/100 of a meter', 'M', 0.01, 'LENGTH', 'cm', true),
    (gen_random_uuid(), 'MM', 'Millimeter', '1/1000 of a meter', 'M', 0.001, 'LENGTH', 'mm', true),
    (gen_random_uuid(), 'KG', 'Kilogram', 'Standard unit of mass', 'KG', 1.0, 'MASS', 'kg', true),
    (gen_random_uuid(), 'G', 'Gram', '1/1000 of a kilogram', 'KG', 0.001, 'MASS', 'g', true),
    (gen_random_uuid(), 'L', 'Liter', 'Standard unit of volume', 'L', 1.0, 'VOLUME', 'L', true),
    (gen_random_uuid(), 'ML', 'Milliliter', '1/1000 of a liter', 'L', 0.001, 'VOLUME', 'mL', true)
ON CONFLICT (code) DO NOTHING;
