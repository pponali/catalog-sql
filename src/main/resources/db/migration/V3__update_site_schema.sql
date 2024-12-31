-- Add new columns to site table
ALTER TABLE site 
    ADD COLUMN domain VARCHAR(255) NOT NULL DEFAULT 'example.com',
    ADD COLUMN locale VARCHAR(10) NOT NULL DEFAULT 'en-US',
    ADD COLUMN currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    ADD COLUMN active BOOLEAN NOT NULL DEFAULT true,
    ADD COLUMN timezone VARCHAR(50),
    ADD COLUMN status VARCHAR(50);

-- Drop the code column as it's no longer needed
ALTER TABLE site DROP COLUMN IF EXISTS code;

-- Make business_id non-nullable
UPDATE site SET business_id = (SELECT id FROM business LIMIT 1) WHERE business_id IS NULL;
ALTER TABLE site ALTER COLUMN business_id SET NOT NULL;
