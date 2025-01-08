-- Insert units
INSERT INTO unit (id, code, name, type, symbol, description, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('660e8400-e29b-41d4-a716-446655440000', 'INCH', 'Inches', 'LENGTH', 'in', 'Unit for screen size measurement', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('660e8400-e29b-41d4-a716-446655440001', 'MAH', 'Milliampere-hour', 'ELECTRIC_CURRENT', 'mAh', 'Unit for battery capacity', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert base units
INSERT INTO unit (id, code, name, type, symbol, description, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('550e8400-e29b-41d4-a716-446655440000', 'KG', 'Kilogram', 'WEIGHT', 'kg', 'Base unit for weight', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('550e8400-e29b-41d4-a716-446655440001', 'M', 'Meter', 'LENGTH', 'm', 'Base unit for length', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('550e8400-e29b-41d4-a716-446655440002', 'L', 'Liter', 'VOLUME', 'L', 'Base unit for volume', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert base unit_of_measures without base_unit_id
INSERT INTO unit_of_measure (id, code, name, description, unit_id, type, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('660e8400-e29b-41d4-a716-446655440000', 'KG', 'Kilogram', 'Base unit for weight', '550e8400-e29b-41d4-a716-446655440000', 'WEIGHT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('660e8400-e29b-41d4-a716-446655440006', 'M', 'Meter', 'Base unit for length', '550e8400-e29b-41d4-a716-446655440001', 'LENGTH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('660e8400-e29b-41d4-a716-446655440007', 'L', 'Liter', 'Base unit for volume', '550e8400-e29b-41d4-a716-446655440002', 'VOLUME', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert derived unit_of_measures with base_unit_id referencing the base measures
INSERT INTO unit_of_measure (id, code, name, description, unit_id, base_unit_id, conversion_factor, type, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('660e8400-e29b-41d4-a716-446655440001', 'G', 'Gram', 'Weight in grams', '550e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655440000', 0.001, 'WEIGHT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('660e8400-e29b-41d4-a716-446655440003', 'CM', 'Centimeter', 'Length in centimeters', '550e8400-e29b-41d4-a716-446655440001', '660e8400-e29b-41d4-a716-446655440006', 0.010, 'LENGTH', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('660e8400-e29b-41d4-a716-446655440005', 'ML', 'Milliliter', 'Volume in milliliters', '550e8400-e29b-41d4-a716-446655440002', '660e8400-e29b-41d4-a716-446655440007', 0.001, 'VOLUME', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert businesses
INSERT INTO business (id, type, status, name, description, code, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('770e8400-e29b-41d4-a716-446655440000','Enterprise', 'ACTIVE','Apple Inc.', 'Technology company that designs, manufactures, and markets consumer electronics, computer software, and online services.', 'APPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440001','Enterprise', 'ACTIVE','Samsung Electronics', 'Technology company that produces a wide range of consumer and industrial electronics, including smartphones, semiconductors, and home appliances.', 'SAMSUNG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440002','Enterprise', 'ACTIVE','Sony Corporation', 'Technology company that designs, develops, manufactures, and sells electronic equipment, instruments, and devices for consumer, professional, and industrial markets.', 'SONY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert sites
INSERT INTO site (id, business_id, name, domain, locale, currency, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('880e8400-e29b-41d4-a716-446655440000', '770e8400-e29b-41d4-a716-446655440000', 'Apple US', 'apple.com', 'en_US', 'USD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440001', 'Samsung US', 'samsung.com', 'en_US', 'USD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440002', 'Sony US', 'sony.com', 'en_US', 'USD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert catalogs
INSERT INTO catalog (id, business_id, name, description, code, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('990e8400-e29b-41d4-a716-446655440000', '770e8400-e29b-41d4-a716-446655440000', 'Apple Products Catalog', 'Catalog of Apple products, including iPhones, Macs, iPads, and more.', 'APPLE_CATALOG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440001', 'Samsung Products Catalog', 'Catalog of Samsung products, including smartphones, TVs, home appliances, and more.', 'SAMSUNG_CATALOG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440002', 'Sony Products Catalog', 'Catalog of Sony products, including TVs, cameras, audio equipment, and more.', 'SONY_CATALOG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert categories
INSERT INTO category (id, business_id, catalog_id, parent_id, name, description, code, level, metadata, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('aa0e8400-e29b-41d4-a716-446655440000', '770e8400-e29b-41d4-a716-446655440000', '990e8400-e29b-41d4-a716-446655440000', NULL, 'Electronics', 'Electronic devices category', 'ELECTRONICS', 1, '{}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('aa0e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440000', '990e8400-e29b-41d4-a716-446655440000', 'aa0e8400-e29b-41d4-a716-446655440000', 'Phones', 'Mobile phones category', 'PHONES', 2, '{}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('aa0e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440000', '990e8400-e29b-41d4-a716-446655440000', 'aa0e8400-e29b-41d4-a716-446655440000', 'Laptops', 'Laptop computers category', 'LAPTOPS', 2, '{}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert products
INSERT INTO product (id, business_id, catalog_id, unit_of_measure_id, name, description, code, product_type, status, sku, price, metadata, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('ee0e8400-e29b-41d4-a716-446655440000', '770e8400-e29b-41d4-a716-446655440000', '990e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655440000', 'iPhone 12', 'Apple iPhone 12, a smartphone with a 6.1-inch display and dual cameras.', 'IPHONE12', 'ELECTRONICS', 'ACTIVE', 'SKU001', 66499.99, '{}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('ee0e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440000', '990e8400-e29b-41d4-a716-446655440000', '660e8400-e29b-41d4-a716-446655440000', 'MacBook Pro', 'Apple MacBook Pro, a laptop computer with a 13.3-inch display and Intel Core i5 processor.', 'MACBOOK_PRO', 'ELECTRONICS', 'ACTIVE', 'SKU002', 129999.99, '{}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('ee0e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440001', '990e8400-e29b-41d4-a716-446655440001', '660e8400-e29b-41d4-a716-446655440000', 'Samsung S21', 'Samsung Galaxy S21, a smartphone with a 6.2-inch display and triple cameras.', 'SAMSUNG_S21', 'ELECTRONICS', 'ACTIVE', 'SKU003', 54999.99, '{}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert product categories
INSERT INTO product_categories (product_id, category_id)
VALUES
('ee0e8400-e29b-41d4-a716-446655440000', 'aa0e8400-e29b-41d4-a716-446655440001'),
('ee0e8400-e29b-41d4-a716-446655440001', 'aa0e8400-e29b-41d4-a716-446655440002'),
('ee0e8400-e29b-41d4-a716-446655440002', 'aa0e8400-e29b-41d4-a716-446655440001');

-- First insert into feature_template (parent table)
INSERT INTO feature_template (
    id, template_type, name, description, feature_type, data_type, input_type,
    validation_pattern, min_value, max_value, allowed_values, default_value,
    unit_id, required, filterable, searchable, comparable, hidden, multi_valued,
    visible, editable, created_date, last_modified_date, created_by, last_modified_by
) VALUES
-- Screen Size template
('ff0e8400-e29b-41d4-a716-446655440001', 'CATEGORY', 'Screen Size', 'Display size in inches', 
'NUMERIC', 'NUMBER', 'NUMBER', NULL, '4.0', '17.0', NULL, '6.1',
'660e8400-e29b-41d4-a716-446655440000', true, true, true, true, false, false,
true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Battery Capacity template
('ff0e8400-e29b-41d4-a716-446655440002', 'CATEGORY', 'Battery Capacity', 'Battery capacity in mAh',
'NUMERIC', 'NUMBER', 'NUMBER', NULL, '2000', '10000', NULL, '3000',
'660e8400-e29b-41d4-a716-446655440001', true, true, true, true, false, false,
true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Model Number template
('ff0e8400-e29b-41d4-a716-446655440003', 'CATEGORY', 'Model Number', 'Product model number',
'STRING', 'STRING', 'TEXT', '^[A-Z0-9]+$', NULL, NULL, NULL, NULL,
NULL, true, false, true, false, false, false,
true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Color template
('ff0e8400-e29b-41d4-a716-446655440004', 'CATEGORY', 'Color', 'Product color',
'STRING', 'STRING', 'ENUM', NULL, NULL, NULL, '["Red","Blue","Black","White"]', 'Black',
NULL, true, true, true, true, false, false,
true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- 5G Capable template
('ff0e8400-e29b-41d4-a716-446655440005', 'CATEGORY', '5G Capable', '5G network support',
'BOOLEAN', 'BOOLEAN', 'BOOLEAN', NULL, NULL, NULL, NULL, 'true',
NULL, true, true, true, true, false, false,
true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Wireless Charging template
('ff0e8400-e29b-41d4-a716-446655440006', 'CATEGORY', 'Wireless Charging', 'Wireless charging support',
'BOOLEAN', 'BOOLEAN', 'BOOLEAN', NULL, NULL, NULL, NULL, 'true',
NULL, false, true, true, true, false, false,
true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Camera Specs template
('ff0e8400-e29b-41d4-a716-446655440007', 'CATEGORY', 'Camera Specs', 'Camera specifications',
'JSON', 'JSON', 'JSON', NULL, NULL, NULL, NULL, '{"main": "12MP", "ultra": "12MP", "front": "12MP"}',
NULL, false, false, false, false, false, false,
true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Connectivity template
('ff0e8400-e29b-41d4-a716-446655440008', 'CATEGORY', 'Connectivity', 'Connectivity options',
'JSON', 'JSON', 'JSON', NULL, NULL, NULL, NULL, '{"wifi": "Wi-Fi 6", "bluetooth": "5.0", "nfc": true}',
NULL, false, false, false, false, false, false,
true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Then insert into category_feature_template (child table)
INSERT INTO category_feature_template (
    id, code, attribute_type, inherited, mandatory, metadata, category_id,
    created_date, last_modified_date, created_by, last_modified_by
) VALUES
-- Screen Size template category specifics
('ff0e8400-e29b-41d4-a716-446655440001', 'SCREEN_SIZE', 'MEASUREMENT', false, true, '{}',
'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Battery Capacity template category specifics
('ff0e8400-e29b-41d4-a716-446655440002', 'BATTERY_CAPACITY', 'MEASUREMENT', false, true, '{}',
'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Model Number template category specifics
('ff0e8400-e29b-41d4-a716-446655440003', 'MODEL_NUMBER', 'STRING', false, true, '{}',
'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Color template category specifics
('ff0e8400-e29b-41d4-a716-446655440004', 'COLOR', 'ENUM', false, true, '{}',
'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- 5G Capable template category specifics
('ff0e8400-e29b-41d4-a716-446655440005', '5G_CAPABLE', 'BOOLEAN', false, true, '{}',
'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Wireless Charging template category specifics
('ff0e8400-e29b-41d4-a716-446655440006', 'WIRELESS_CHARGING', 'BOOLEAN', false, false, '{}',
'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Camera Specs template category specifics
('ff0e8400-e29b-41d4-a716-446655440007', 'CAMERA_SPECS', 'JSON', false, false, '{}',
'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Connectivity template category specifics
('ff0e8400-e29b-41d4-a716-446655440008', 'CONNECTIVITY', 'JSON', false, false, '{}',
'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert product features based on templates
INSERT INTO product_feature (
    id, product_id, template_id,
    name, description,
    code, feature_type, data_type, input_type,
    validation_pattern, min_value, max_value, allowed_values,
    attribute_type, comparable, visible, searchable, editable,
    multi_valued, default_value, unit_of_measure_id, metadata, required,
    created_date, last_modified_date, created_by, last_modified_by
)
SELECT 
    md5(random()::text || clock_timestamp()::text)::uuid,
    p.id,
    ft.id,
    ft.name,
    ft.description,
    cft.code,
    ft.feature_type,
    ft.data_type,
    ft.input_type,
    ft.validation_pattern,
    ft.min_value,
    ft.max_value,
    ft.allowed_values,
    cft.attribute_type,
    ft.comparable,
    ft.visible,
    ft.searchable,
    ft.editable,
    ft.multi_valued,
    ft.default_value,
    ft.unit_id,
    cft.metadata,
    ft.required,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'system',
    'system'
FROM product p
CROSS JOIN feature_template ft
JOIN category_feature_template cft ON ft.id = cft.id
WHERE p.id IN (
    'ee0e8400-e29b-41d4-a716-446655440000',
    'ee0e8400-e29b-41d4-a716-446655440001',
    'ee0e8400-e29b-41d4-a716-446655440002'
);

-- Insert feature values for iPhone 12
INSERT INTO product_feature_value (id, product_id, feature_id, type, value, created_date, last_modified_date, created_by, last_modified_by)
SELECT 
    md5(random()::text || clock_timestamp()::text)::uuid,
    p.id,
    f.id,
    f.feature_type,
    CASE 
        WHEN f.code = 'SCREEN_SIZE' THEN '{"value": 6.1}'
        WHEN f.code = 'BATTERY_CAPACITY' THEN '{"value": 2815}'
        WHEN f.code = 'MODEL_NUMBER' THEN '{"value": "A2172"}'
        WHEN f.code = 'COLOR' THEN '{"value": "Blue"}'
        WHEN f.code = '5G_CAPABLE' THEN '{"value": true}'
        WHEN f.code = 'WIRELESS_CHARGING' THEN '{"value": true}'
        WHEN f.code = 'CAMERA_SPECS' THEN '{"main": "12MP", "ultra": "12MP", "front": "12MP"}'
        WHEN f.code = 'CONNECTIVITY' THEN '{"wifi": "Wi-Fi 6", "bluetooth": "5.0", "nfc": true}'
    END,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'system',
    'system'
FROM product p
JOIN product_feature f ON p.id = f.product_id
WHERE p.id = 'ee0e8400-e29b-41d4-a716-446655440000';

-- Insert feature values for MacBook Pro
INSERT INTO product_feature_value (id, product_id, feature_id, type, value, created_date, last_modified_date, created_by, last_modified_by)
SELECT 
    md5(random()::text || clock_timestamp()::text)::uuid,
    p.id,
    f.id,
    f.feature_type,
    CASE 
        WHEN f.code = 'SCREEN_SIZE' THEN '{"value": 13.3}'
        WHEN f.code = 'BATTERY_CAPACITY' THEN '{"value": 5000}'
        WHEN f.code = 'MODEL_NUMBER' THEN '{"value": "A2338"}'
        WHEN f.code = 'COLOR' THEN '{"value": "Silver"}'
        WHEN f.code = '5G_CAPABLE' THEN '{"value": false}'
        WHEN f.code = 'WIRELESS_CHARGING' THEN '{"value": false}'
        WHEN f.code = 'CAMERA_SPECS' THEN '{"front": "720p HD"}'
        WHEN f.code = 'CONNECTIVITY' THEN '{"wifi": "Wi-Fi 6", "bluetooth": "5.0", "thunderbolt": 4}'
    END,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'system',
    'system'
FROM product p
JOIN product_feature f ON p.id = f.product_id
WHERE p.id = 'ee0e8400-e29b-41d4-a716-446655440001';

-- Insert feature values for Samsung S21
INSERT INTO product_feature_value (id, product_id, feature_id, type, value, created_date, last_modified_date, created_by, last_modified_by)
SELECT 
    md5(random()::text || clock_timestamp()::text)::uuid,
    p.id,
    f.id,
    f.feature_type,
    CASE 
        WHEN f.code = 'SCREEN_SIZE' THEN '{"value": 6.2}'
        WHEN f.code = 'BATTERY_CAPACITY' THEN '{"value": 4000}'
        WHEN f.code = 'MODEL_NUMBER' THEN '{"value": "SM-G991"}'
        WHEN f.code = 'COLOR' THEN '{"value": "Phantom Gray"}'
        WHEN f.code = '5G_CAPABLE' THEN '{"value": true}'
        WHEN f.code = 'WIRELESS_CHARGING' THEN '{"value": true}'
        WHEN f.code = 'CAMERA_SPECS' THEN '{"main": "12MP", "ultra": "12MP", "telephoto": "64MP", "front": "10MP"}'
        WHEN f.code = 'CONNECTIVITY' THEN '{"wifi": "Wi-Fi 6E", "bluetooth": "5.0", "nfc": true}'
    END,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'system',
    'system'
FROM product p
JOIN product_feature f ON p.id = f.product_id
WHERE p.id = 'ee0e8400-e29b-41d4-a716-446655440002';

-- Insert site catalog assignments
INSERT INTO site_catalog (site_id, catalog_id, is_default, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('880e8400-e29b-41d4-a716-446655440000', '990e8400-e29b-41d4-a716-446655440000', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440001', '990e8400-e29b-41d4-a716-446655440001', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440002', '990e8400-e29b-41d4-a716-446655440002', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');
