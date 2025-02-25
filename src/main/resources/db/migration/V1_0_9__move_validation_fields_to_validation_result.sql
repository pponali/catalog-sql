-- First, migrate existing validation data to validation_result table
INSERT INTO validation_result (
    id,
    entity_id,
    entity_type,
    field_name,
    validation_status,
    created_date,
    last_modified_date,
    created_by,
    last_modified_by
)
SELECT 
    gen_random_uuid(),
    id as entity_id,
    'PRODUCT_FEATURE_VALUE' as entity_type,
    'value' as field_name,
    validation_status::validation_status as validation_status,
    created_date,
    last_modified_date,
    created_by,
    last_modified_by
FROM product_feature_value
WHERE validation_status IS NOT NULL;

-- Add validation_result_id column to product_feature_value
ALTER TABLE product_feature_value 
ADD COLUMN validation_result_id UUID REFERENCES validation_result(id);

-- Update the references
UPDATE product_feature_value pfv
SET validation_result_id = vr.id
FROM validation_result vr
WHERE vr.entity_id = pfv.id;

-- Remove old validation columns
ALTER TABLE product_feature_value 
DROP COLUMN IF EXISTS validation_status,
DROP COLUMN IF EXISTS validation_pattern,
DROP COLUMN IF EXISTS validation_message;
