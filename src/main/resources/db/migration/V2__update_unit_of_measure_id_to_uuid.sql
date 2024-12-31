-- Create a temporary column for UUID conversion
ALTER TABLE unit_of_measure ADD COLUMN id_uuid UUID;

-- Update the temporary column with UUID values
UPDATE unit_of_measure SET id_uuid = gen_random_uuid() WHERE id IS NOT NULL;

-- Update foreign key references in product_feature table
ALTER TABLE product_feature ADD COLUMN unit_id_uuid UUID;
UPDATE product_feature pf 
SET unit_id_uuid = uom.id_uuid 
FROM unit_of_measure uom 
WHERE pf.unit_id = uom.id;

-- Drop the old foreign key constraint
ALTER TABLE product_feature DROP CONSTRAINT IF EXISTS product_feature_unit_id_fkey;

-- Drop the old columns
ALTER TABLE product_feature DROP COLUMN unit_id;
ALTER TABLE unit_of_measure DROP COLUMN id;

-- Rename the new columns
ALTER TABLE product_feature RENAME COLUMN unit_id_uuid TO unit_id;
ALTER TABLE unit_of_measure RENAME COLUMN id_uuid TO id;

-- Make the new id column the primary key
ALTER TABLE unit_of_measure ADD PRIMARY KEY (id);

-- Add back the foreign key constraint with UUID
ALTER TABLE product_feature 
ADD CONSTRAINT product_feature_unit_id_fkey 
FOREIGN KEY (unit_id) REFERENCES unit_of_measure(id);
