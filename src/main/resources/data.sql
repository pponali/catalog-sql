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
INSERT INTO business (id, name, description, code, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('770e8400-e29b-41d4-a716-446655440000', 'Apple Inc.', 'Technology company that designs, manufactures, and markets consumer electronics, computer software, and online services.', 'APPLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440001', 'Samsung Electronics', 'Technology company that produces a wide range of consumer and industrial electronics, including smartphones, semiconductors, and home appliances.', 'SAMSUNG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440002', 'Sony Corporation', 'Technology company that designs, develops, manufactures, and sells electronic equipment, instruments, and devices for consumer, professional, and industrial markets.', 'SONY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

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
('aa0e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440000', '990e8400-e29b-41d4-a716-446655440000', NULL, 'Electronics', 'Electronic devices, including smartphones, laptops, and tablets.', 'ELECTRONICS', 1, '{}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('aa0e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440001', '990e8400-e29b-41d4-a716-446655440001', NULL, 'Mobiles', 'Mobile devices, including smartphones and tablets.', 'MOBILES', 1, '{}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('aa0e8400-e29b-41d4-a716-446655440003', '770e8400-e29b-41d4-a716-446655440002', '990e8400-e29b-41d4-a716-446655440002', NULL, 'Laptops', 'Laptop computers, including ultrabooks and gaming laptops.', 'LAPTOPS', 1, '{}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

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
('ee0e8400-e29b-41d4-a716-446655440002', 'aa0e8400-e29b-41d4-a716-446655440003');

-- Insert catalog categories
INSERT INTO catalog_category (catalog_id, category_id, primary_category, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('990e8400-e29b-41d4-a716-446655440000', 'aa0e8400-e29b-41d4-a716-446655440000', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440001', 'aa0e8400-e29b-41d4-a716-446655440001', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440002', 'aa0e8400-e29b-41d4-a716-446655440002', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert category feature templates for different types
INSERT INTO category_feature_template (id, name, description, code, feature_type, validation_pattern, min_value, max_value, allowed_values, attribute_type, comparable, visible, searchable, editable, multi_valued, default_value, unit_id, required, category_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
-- Numeric features
('ff0e8400-e29b-41d4-a716-446655440001', 'Screen Size', 'Display size in inches', 'SCREEN_SIZE', 'NUMERIC', NULL, '4.0', '17.0', NULL, 'NUMERIC', true, true, true, true, false, '6.1', '660e8400-e29b-41d4-a716-446655440000', true, 'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('ff0e8400-e29b-41d4-a716-446655440002', 'Battery Capacity', 'Battery capacity in mAh', 'BATTERY_CAPACITY', 'NUMERIC', NULL, '2000', '10000', NULL, 'NUMERIC', true, true, true, true, false, '3000', '660e8400-e29b-41d4-a716-446655440001', true, 'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- String features
('ff0e8400-e29b-41d4-a716-446655440003', 'Model Number', 'Product model number', 'MODEL_NUMBER', 'STRING', '^[A-Z0-9]+$', NULL, NULL, NULL, 'STRING', false, true, true, true, false, NULL, NULL, true, 'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('ff0e8400-e29b-41d4-a716-446655440004', 'Color', 'Product color', 'COLOR', 'STRING', NULL, NULL, NULL, 'Red,Blue,Black,White', 'ENUM', true, true, true, true, false, 'Black', NULL, true, 'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Boolean features
('ff0e8400-e29b-41d4-a716-446655440005', '5G Capable', '5G network support', '5G_CAPABLE', 'BOOLEAN', NULL, NULL, NULL, NULL, 'BOOLEAN', true, true, true, true, false, 'true', NULL, true, 'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('ff0e8400-e29b-41d4-a716-446655440006', 'Wireless Charging', 'Wireless charging support', 'WIRELESS_CHARGING', 'BOOLEAN', NULL, NULL, NULL, NULL, 'BOOLEAN', true, true, true, true, false, 'true', NULL, false, 'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- JSON features
('ff0e8400-e29b-41d4-a716-446655440007', 'Camera Specs', 'Camera specifications', 'CAMERA_SPECS', 'JSON', NULL, NULL, NULL, NULL, 'JSON', false, true, false, true, false, '{"main": "12MP", "ultra": "12MP", "front": "12MP"}', NULL, false, 'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('ff0e8400-e29b-41d4-a716-446655440008', 'Connectivity', 'Connectivity options', 'CONNECTIVITY', 'JSON', NULL, NULL, NULL, NULL, 'JSON', false, true, false, true, false, '{"wifi": "Wi-Fi 6", "bluetooth": "5.0", "nfc": true}', NULL, false, 'aa0e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert product features based on templates
INSERT INTO product_feature (id, product_id, template_id, name, description, code, feature_type, validation_pattern, min_value, max_value, allowed_values, attribute_type, comparable, visible, searchable, editable, multi_valued, default_value, unit_id, metadata, created_date, last_modified_date, created_by, last_modified_by)
SELECT 
    md5(random()::text || clock_timestamp()::text)::uuid,
    p.id,
    t.id,
    t.name,
    t.description,
    t.code,
    t.feature_type,
    t.validation_pattern,
    t.min_value,
    t.max_value,
    t.allowed_values,
    t.attribute_type,
    t.comparable,
    t.visible,
    t.searchable,
    t.editable,
    t.multi_valued,
    t.default_value,
    t.unit_id,
    '{}',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'system',
    'system'
FROM product p
CROSS JOIN category_feature_template t
WHERE p.id IN ('ee0e8400-e29b-41d4-a716-446655440000', 'ee0e8400-e29b-41d4-a716-446655440001', 'ee0e8400-e29b-41d4-a716-446655440002');

-- Insert feature values for iPhone 12
INSERT INTO product_feature_value (id, product_id, feature_id, type, value, string_value, numeric_value, boolean_value, created_date, last_modified_date, created_by, last_modified_by)
SELECT 
    md5(random()::text || clock_timestamp()::text)::uuid,
    p.id,
    f.id,
    f.feature_type,
    CASE 
        WHEN f.code = 'SCREEN_SIZE' THEN '6.1'
        WHEN f.code = 'BATTERY_CAPACITY' THEN '2815'
        WHEN f.code = 'MODEL_NUMBER' THEN 'A2172'
        WHEN f.code = 'COLOR' THEN 'Blue'
        WHEN f.code = '5G_CAPABLE' THEN 'true'
        WHEN f.code = 'WIRELESS_CHARGING' THEN 'true'
        WHEN f.code = 'CAMERA_SPECS' THEN '{"main": "12MP", "ultra": "12MP", "front": "12MP"}'
        WHEN f.code = 'CONNECTIVITY' THEN '{"wifi": "Wi-Fi 6", "bluetooth": "5.0", "nfc": true}'
    END,
    CASE WHEN f.feature_type = 'STRING' OR f.feature_type = 'ENUM' THEN 
        CASE 
            WHEN f.code = 'MODEL_NUMBER' THEN 'A2172'
            WHEN f.code = 'COLOR' THEN 'Blue'
            ELSE NULL
        END
    ELSE NULL END,
    CASE WHEN f.feature_type = 'NUMERIC' THEN 
        CASE 
            WHEN f.code = 'SCREEN_SIZE' THEN 6.1
            WHEN f.code = 'BATTERY_CAPACITY' THEN 2815
            ELSE NULL
        END
    ELSE NULL END,
    CASE WHEN f.feature_type = 'BOOLEAN' THEN 
        CASE 
            WHEN f.code = '5G_CAPABLE' THEN true
            WHEN f.code = 'WIRELESS_CHARGING' THEN true
            ELSE NULL
        END
    ELSE NULL END,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'system',
    'system'
FROM product p
JOIN product_feature f ON p.id = f.product_id
WHERE p.id = 'ee0e8400-e29b-41d4-a716-446655440000';

-- Insert feature values for MacBook Pro
INSERT INTO product_feature_value (id, product_id, feature_id, type, value, string_value, numeric_value, boolean_value, created_date, last_modified_date, created_by, last_modified_by)
SELECT 
    md5(random()::text || clock_timestamp()::text)::uuid,
    p.id,
    f.id,
    f.feature_type,
    CASE 
        WHEN f.code = 'SCREEN_SIZE' THEN '13.3'
        WHEN f.code = 'BATTERY_CAPACITY' THEN '5000'
        WHEN f.code = 'MODEL_NUMBER' THEN 'A2338'
        WHEN f.code = 'COLOR' THEN 'Silver'
        WHEN f.code = '5G_CAPABLE' THEN 'false'
        WHEN f.code = 'WIRELESS_CHARGING' THEN 'false'
        WHEN f.code = 'CAMERA_SPECS' THEN '{"front": "720p HD"}'
        WHEN f.code = 'CONNECTIVITY' THEN '{"wifi": "Wi-Fi 6", "bluetooth": "5.0", "thunderbolt": 4}'
    END,
    CASE WHEN f.feature_type = 'STRING' OR f.feature_type = 'ENUM' THEN 
        CASE 
            WHEN f.code = 'MODEL_NUMBER' THEN 'A2338'
            WHEN f.code = 'COLOR' THEN 'Silver'
            ELSE NULL
        END
    ELSE NULL END,
    CASE WHEN f.feature_type = 'NUMERIC' THEN 
        CASE 
            WHEN f.code = 'SCREEN_SIZE' THEN 13.3
            WHEN f.code = 'BATTERY_CAPACITY' THEN 5000
            ELSE NULL
        END
    ELSE NULL END,
    CASE WHEN f.feature_type = 'BOOLEAN' THEN 
        CASE 
            WHEN f.code = '5G_CAPABLE' THEN false
            WHEN f.code = 'WIRELESS_CHARGING' THEN false
            ELSE NULL
        END
    ELSE NULL END,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'system',
    'system'
FROM product p
JOIN product_feature f ON p.id = f.product_id
WHERE p.id = 'ee0e8400-e29b-41d4-a716-446655440001';

-- Insert feature values for Samsung S21
INSERT INTO product_feature_value (id, product_id, feature_id, type, value, string_value, numeric_value, boolean_value, created_date, last_modified_date, created_by, last_modified_by)
SELECT 
    md5(random()::text || clock_timestamp()::text)::uuid,
    p.id,
    f.id,
    f.feature_type,
    CASE 
        WHEN f.code = 'SCREEN_SIZE' THEN '6.2'
        WHEN f.code = 'BATTERY_CAPACITY' THEN '4000'
        WHEN f.code = 'MODEL_NUMBER' THEN 'SM-G991'
        WHEN f.code = 'COLOR' THEN 'Phantom Gray'
        WHEN f.code = '5G_CAPABLE' THEN 'true'
        WHEN f.code = 'WIRELESS_CHARGING' THEN 'true'
        WHEN f.code = 'CAMERA_SPECS' THEN '{"main": "12MP", "ultra": "12MP", "telephoto": "64MP", "front": "10MP"}'
        WHEN f.code = 'CONNECTIVITY' THEN '{"wifi": "Wi-Fi 6E", "bluetooth": "5.0", "nfc": true}'
    END,
    CASE WHEN f.feature_type = 'STRING' OR f.feature_type = 'ENUM' THEN 
        CASE 
            WHEN f.code = 'MODEL_NUMBER' THEN 'SM-G991'
            WHEN f.code = 'COLOR' THEN 'Phantom Gray'
            ELSE NULL
        END
    ELSE NULL END,
    CASE WHEN f.feature_type = 'NUMERIC' THEN 
        CASE 
            WHEN f.code = 'SCREEN_SIZE' THEN 6.2
            WHEN f.code = 'BATTERY_CAPACITY' THEN 4000
            ELSE NULL
        END
    ELSE NULL END,
    CASE WHEN f.feature_type = 'BOOLEAN' THEN 
        CASE 
            WHEN f.code = '5G_CAPABLE' THEN true
            WHEN f.code = 'WIRELESS_CHARGING' THEN true
            ELSE NULL
        END
    ELSE NULL END,
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
