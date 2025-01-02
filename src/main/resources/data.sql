-- Insert base units
INSERT INTO unit (id, code, name, description, type, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('550e8400-e29b-41d4-a716-446655440000', 'KG', 'Kilogram', 'Base unit for weight', 'WEIGHT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('550e8400-e29b-41d4-a716-446655440001', 'M', 'Meter', 'Base unit for length', 'LENGTH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('550e8400-e29b-41d4-a716-446655440002', 'L', 'Liter', 'Base unit for volume', 'VOLUME', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- First insert base unit_of_measures without base_unit_id
INSERT INTO unit_of_measure (id, code, name, description, unit_id, type, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('660e8400-e29b-41d4-a716-446655440000', 'KG', 'Kilogram', 'Base unit for weight', '550e8400-e29b-41d4-a716-446655440000', 'WEIGHT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('660e8400-e29b-41d4-a716-446655440006', 'M', 'Meter', 'Base unit for length', '550e8400-e29b-41d4-a716-446655440001', 'LENGTH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('660e8400-e29b-41d4-a716-446655440007', 'L', 'Liter', 'Base unit for volume', '550e8400-e29b-41d4-a716-446655440002', 'VOLUME', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Then insert derived unit_of_measures with base_unit_id referencing the base measures
INSERT INTO unit_of_measure (id, code, name, description, unit_id, base_unit_id, conversion_factor, type, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('660e8400-e29b-41d4-a716-446655440001', 'G', 'Gram', 'Weight in grams', '550e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655440000', 0.001, 'WEIGHT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('660e8400-e29b-41d4-a716-446655440003', 'CM', 'Centimeter', 'Length in centimeters', '550e8400-e29b-41d4-a716-446655440001', '660e8400-e29b-41d4-a716-446655440006', 0.010, 'LENGTH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('660e8400-e29b-41d4-a716-446655440005', 'ML', 'Milliliter', 'Volume in milliliters', '550e8400-e29b-41d4-a716-446655440002', '660e8400-e29b-41d4-a716-446655440007', 0.001, 'VOLUME', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert businesses
INSERT INTO business (id, code, name, description, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('770e8400-e29b-41d4-a716-446655440000', 'BUS1', 'Business 1', 'First business', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440001', 'BUS2', 'Business 2', 'Second business', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440002', 'BUS3', 'Business 3', 'Third business', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert sites
INSERT INTO site (id, business_id, name, domain, locale, currency, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('880e8400-e29b-41d4-a716-446655440000', '770e8400-e29b-41d4-a716-446655440000', 'Site 1', 'site1.com', 'en_US', 'USD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440001', 'Site 2', 'site2.com', 'en_GB', 'GBP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440002', 'Site 3', 'site3.com', 'fr_FR', 'EUR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert catalogs
INSERT INTO catalog (id, code, name, description, business_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('990e8400-e29b-41d4-a716-446655440000', 'CAT1', 'Catalog 1', 'First catalog', '770e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440001', 'CAT2', 'Catalog 2', 'Second catalog', '770e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440002', 'CAT3', 'Catalog 3', 'Third catalog', '770e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert categories
INSERT INTO category (id, code, business_id, catalog_id, parent_id, name, description, created_date, last_modified_date, created_by, last_modified_by, dtype)
VALUES 
('aa0e8400-e29b-41d4-a716-446655440000', 'ELEC', '770e8400-e29b-41d4-a716-446655440000', '990e8400-e29b-41d4-a716-446655440000', NULL, 'Electronics', 'Electronic items', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system', 'STANDARD'),
('aa0e8400-e29b-41d4-a716-446655440001', 'PHONE', '770e8400-e29b-41d4-a716-446655440000', '990e8400-e29b-41d4-a716-446655440000', 'aa0e8400-e29b-41d4-a716-446655440000', 'Phones', 'Mobile phones', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system', 'STANDARD'),
('aa0e8400-e29b-41d4-a716-446655440002', 'LAPTOP', '770e8400-e29b-41d4-a716-446655440000', '990e8400-e29b-41d4-a716-446655440000', 'aa0e8400-e29b-41d4-a716-446655440000', 'Laptops', 'Laptop computers', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system', 'STANDARD');

-- Insert products with prices in INR and unit of measure references
INSERT INTO product (id, business_id, catalog_id, name, description, sku, code, product_type, status, price, unit_of_measure_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('ee0e8400-e29b-41d4-a716-446655440000', '770e8400-e29b-41d4-a716-446655440000', '990e8400-e29b-41d4-a716-446655440000', 'iPhone 12', 'Apple iPhone 12', 'SKU001', 'IPHONE12', 'ELECTRONICS', 'ACTIVE', 66499.99, '660e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('ee0e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440001', '990e8400-e29b-41d4-a716-446655440001', 'MacBook Pro', 'Apple MacBook Pro', 'SKU002', 'MACBOOKPRO', 'ELECTRONICS', 'ACTIVE', 129999.99, '660e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('ee0e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440002', '990e8400-e29b-41d4-a716-446655440002', 'Samsung S21', 'Samsung Galaxy S21', 'SKU003', 'SAMSUNGS21', 'ELECTRONICS', 'ACTIVE', 74999.99, '660e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert product categories
INSERT INTO product_categories (product_id, category_id)
VALUES
('ee0e8400-e29b-41d4-a716-446655440000', 'aa0e8400-e29b-41d4-a716-446655440001'),
('ee0e8400-e29b-41d4-a716-446655440001', 'aa0e8400-e29b-41d4-a716-446655440002'),
('ee0e8400-e29b-41d4-a716-446655440002', 'aa0e8400-e29b-41d4-a716-446655440001');

-- Insert site catalog assignments
INSERT INTO site_catalog (site_id, catalog_id, is_default, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('880e8400-e29b-41d4-a716-446655440000', '990e8400-e29b-41d4-a716-446655440000', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440001', '990e8400-e29b-41d4-a716-446655440001', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440002', '990e8400-e29b-41d4-a716-446655440002', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');
