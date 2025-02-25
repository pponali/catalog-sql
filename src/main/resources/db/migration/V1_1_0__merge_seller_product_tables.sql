-- Create new merged seller_product table
CREATE TABLE IF NOT EXISTS seller_product_new (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL REFERENCES product(id),
    seller_id UUID NOT NULL REFERENCES seller(id),
    merchant_id UUID NOT NULL REFERENCES merchant(id),
    is_manufacturer BOOLEAN DEFAULT false,
    price DECIMAL(19,2),
    stock INTEGER,
    status VARCHAR(50),
    commission_rate DECIMAL(5,2),
    metadata JSONB,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    UNIQUE(product_id, seller_id, merchant_id)
);

-- Migrate data from product_seller
INSERT INTO seller_product_new (
    product_id, 
    seller_id, 
    is_manufacturer,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date
)
SELECT 
    product_id,
    seller_id,
    is_manufacturer,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date
FROM product_seller;

-- Migrate data from seller_product
INSERT INTO seller_product_new (
    product_id,
    seller_id,
    price,
    stock,
    status,
    commission_rate,
    metadata,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date
)
SELECT 
    product_id,
    seller_id,
    price,
    stock,
    status,
    commission_rate,
    metadata,
    created_by,
    created_date,
    last_modified_by,
    last_modified_date
FROM seller_product
ON CONFLICT (product_id, seller_id) 
DO UPDATE SET
    price = EXCLUDED.price,
    stock = EXCLUDED.stock,
    status = EXCLUDED.status,
    commission_rate = EXCLUDED.commission_rate,
    metadata = EXCLUDED.metadata;

-- Drop old tables
DROP TABLE IF EXISTS product_seller;
DROP TABLE IF EXISTS seller_product;

-- Rename new table
ALTER TABLE seller_product_new RENAME TO seller_product;
