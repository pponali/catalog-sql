-- Add code column to classification_attribute_values table
ALTER TABLE classification_attribute_values ADD COLUMN code VARCHAR(255) NOT NULL DEFAULT 'default_code';

-- Remove the default value constraint after adding the column
ALTER TABLE classification_attribute_values ALTER COLUMN code DROP DEFAULT;
