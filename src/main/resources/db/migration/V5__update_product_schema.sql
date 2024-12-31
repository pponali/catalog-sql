-- Make product_type non-nullable
UPDATE products SET product_type = 'DEFAULT' WHERE product_type IS NULL;
ALTER TABLE products ALTER COLUMN product_type SET NOT NULL;

-- Make business_id non-nullable
UPDATE products SET business_id = (SELECT id FROM business LIMIT 1) WHERE business_id IS NULL;
ALTER TABLE products ALTER COLUMN business_id SET NOT NULL;

-- Add indexes for foreign keys and frequently queried columns
CREATE INDEX IF NOT EXISTS idx_products_business_id ON products(business_id);
CREATE INDEX IF NOT EXISTS idx_products_catalog_id ON products(catalog_id);
CREATE INDEX IF NOT EXISTS idx_products_unit_of_measure_id ON products(unit_of_measure_id);
CREATE INDEX IF NOT EXISTS idx_products_code ON products(code);
CREATE INDEX IF NOT EXISTS idx_products_sku ON products(sku);

-- Add unique constraint on code within a business
ALTER TABLE products 
ADD CONSTRAINT uk_products_business_id_code 
UNIQUE (business_id, code);
