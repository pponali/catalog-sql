-- Create a temporary column for the UUID conversion
ALTER TABLE product_feature_value ADD COLUMN template_id_uuid UUID;

-- Update the temporary column with UUID values
-- This assumes you want to generate new UUIDs for existing records
UPDATE product_feature_value SET template_id_uuid = gen_random_uuid() WHERE template_id IS NOT NULL;

-- Drop the old column
ALTER TABLE product_feature_value DROP COLUMN template_id;

-- Rename the new column to the original name
ALTER TABLE product_feature_value RENAME COLUMN template_id_uuid TO template_id;
