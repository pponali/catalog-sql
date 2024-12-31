-- Make required fields non-nullable in category_feature_template
ALTER TABLE category_feature_template 
    ALTER COLUMN code SET NOT NULL,
    ALTER COLUMN name SET NOT NULL,
    ALTER COLUMN attribute_type SET NOT NULL,
    ALTER COLUMN feature_type SET NOT NULL;

-- Add default values for boolean fields in category_feature_template
ALTER TABLE category_feature_template 
    ALTER COLUMN visible SET DEFAULT true,
    ALTER COLUMN editable SET DEFAULT true,
    ALTER COLUMN searchable SET DEFAULT true,
    ALTER COLUMN comparable SET DEFAULT true,
    ALTER COLUMN mandatory SET DEFAULT false,
    ALTER COLUMN multi_valued SET DEFAULT false;

-- Add default values for string fields
ALTER TABLE category_feature_template 
    ALTER COLUMN validation_pattern SET DEFAULT '',
    ALTER COLUMN min_value SET DEFAULT '',
    ALTER COLUMN max_value SET DEFAULT '',
    ALTER COLUMN allowed_values SET DEFAULT '',
    ALTER COLUMN default_value SET DEFAULT '',
    ALTER COLUMN metadata SET DEFAULT '';

-- Add indexes for frequently queried columns
CREATE INDEX IF NOT EXISTS idx_category_feature_template_code ON category_feature_template(code);
CREATE INDEX IF NOT EXISTS idx_category_feature_template_category_id ON category_feature_template(category_id);
CREATE INDEX IF NOT EXISTS idx_category_feature_template_unit_id ON category_feature_template(unit_id);

-- Add unique constraint to prevent duplicate feature codes within a category
ALTER TABLE category_feature_template 
ADD CONSTRAINT uk_category_feature_template_category_id_code 
UNIQUE (category_id, code);
