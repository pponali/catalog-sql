-- Add new columns to site_catalog table
ALTER TABLE site_catalog 
    ADD COLUMN is_default BOOLEAN NOT NULL DEFAULT false,
    ADD COLUMN status VARCHAR(50),
    ADD COLUMN start_date TIMESTAMP,
    ADD COLUMN end_date TIMESTAMP;

-- Make foreign key columns non-nullable
UPDATE site_catalog 
SET site_id = (SELECT id FROM site LIMIT 1) 
WHERE site_id IS NULL;

UPDATE site_catalog 
SET catalog_id = (SELECT id FROM catalog LIMIT 1) 
WHERE catalog_id IS NULL;

ALTER TABLE site_catalog 
    ALTER COLUMN site_id SET NOT NULL,
    ALTER COLUMN catalog_id SET NOT NULL;

-- Add unique constraint to prevent duplicate site-catalog combinations
ALTER TABLE site_catalog 
ADD CONSTRAINT uk_site_catalog_site_id_catalog_id 
UNIQUE (site_id, catalog_id);
