-- Make required fields non-nullable in product_feature
ALTER TABLE product_feature 
    ALTER COLUMN code SET NOT NULL,
    ALTER COLUMN name SET NOT NULL,
    ALTER COLUMN attribute_type SET NOT NULL,
    ALTER COLUMN feature_type SET NOT NULL;

-- Add default values for boolean fields in product_feature
ALTER TABLE product_feature 
    ALTER COLUMN visible SET DEFAULT true,
    ALTER COLUMN editable SET DEFAULT true,
    ALTER COLUMN searchable SET DEFAULT false,
    ALTER COLUMN comparable SET DEFAULT false,
    ALTER COLUMN required SET DEFAULT false,
    ALTER COLUMN multi_valued SET DEFAULT false;

-- Make product_id and feature_id non-nullable in product_feature_value
UPDATE product_feature_value 
SET product_id = (SELECT id FROM products LIMIT 1) 
WHERE product_id IS NULL;

UPDATE product_feature_value 
SET feature_id = (SELECT id FROM product_feature LIMIT 1) 
WHERE feature_id IS NULL;

ALTER TABLE product_feature_value 
    ALTER COLUMN product_id SET NOT NULL,
    ALTER COLUMN feature_id SET NOT NULL,
    ALTER COLUMN type SET NOT NULL,
    ALTER COLUMN attribute_values SET NOT NULL;

-- Add indexes for frequently queried columns
CREATE INDEX IF NOT EXISTS idx_product_feature_code ON product_feature(code);
CREATE INDEX IF NOT EXISTS idx_product_feature_product_id ON product_feature(product_id);
CREATE INDEX IF NOT EXISTS idx_product_feature_template_id ON product_feature(template_id);

CREATE INDEX IF NOT EXISTS idx_product_feature_value_product_id ON product_feature_value(product_id);
CREATE INDEX IF NOT EXISTS idx_product_feature_value_feature_id ON product_feature_value(feature_id);
CREATE INDEX IF NOT EXISTS idx_product_feature_value_template_id ON product_feature_value(template_id);

-- Add unique constraint to prevent duplicate feature codes within a product
ALTER TABLE product_feature 
ADD CONSTRAINT uk_product_feature_product_id_code 
UNIQUE (product_id, code);
