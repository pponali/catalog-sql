-- Insert units for measurements and sizes
INSERT INTO unit (id, code, name, type, symbol, description, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('880e8400-e29b-41d4-a716-446655440000', 'INCH_MEASURE', 'Inch', 'LENGTH', 'in_inch', 'Imperial length measurement', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440001', 'KG_MEASURE', 'Kilogram', 'WEIGHT', 'kg_kg', 'Metric weight measurement', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440001', 'SIZE_INT', 'International Size', 'SIZE', 'INT', 'International clothing size standard', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440002', 'SIZE_UK', 'UK Size', 'SIZE', 'UK', 'UK clothing size standard', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440003', 'MM', 'Millimeter', 'LENGTH', 'mm', 'Millimeter measurement', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440004', 'OZ', 'Ounce', 'WEIGHT', 'oz', 'Imperial weight measurement', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert unit of measures with conversions
INSERT INTO unit_of_measure (id, code, name, type, active, display_symbol, description, unit_id, conversion_factor, base_unit_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('880e8400-e29b-41d4-a716-446655440000', 'EU_DRESS', 'EU Dress Size', 'SIZE', true, 'EU', 'European dress size', '880e8400-e29b-41d4-a716-446655440000', 1.0, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440006', 'US_DRESS', 'US Dress Size', 'SIZE', true, 'US', 'US dress size', '880e8400-e29b-41d4-a716-446655440001', 1.0, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440007', 'CM_MEASURE', 'Centimeter Measure', 'LENGTH', true, 'cm', 'Centimeter measurement', '880e8400-e29b-41d4-a716-446655440000', 1.0, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440008', 'INCH_MEASURE', 'Inch Measure', 'LENGTH', true, 'in', 'Inch measurement', '880e8400-e29b-41d4-a716-446655440000', 2.54, '880e8400-e29b-41d4-a716-446655440007', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440009', 'KG_MEASURE', 'Kilogram Measure', 'WEIGHT', true, 'kg', 'Kilogram measurement', '880e8400-e29b-41d4-a716-446655440000', 1.0, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440005', 'INT_SIZE', 'International Size', 'SIZE', true, 'INT', 'International size standard', '990e8400-e29b-41d4-a716-446655440001', 1.0, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440006', 'UK_SIZE', 'UK Size', 'SIZE', true, 'UK', 'UK size standard', '990e8400-e29b-41d4-a716-446655440002', 1.0, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440007', 'MM_LENGTH', 'Millimeter', 'LENGTH', true, 'mm', 'Millimeter measurement', '990e8400-e29b-41d4-a716-446655440003', 0.1, '660e8400-e29b-41d4-a716-446655440006', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440008', 'OZ_WEIGHT', 'Ounce', 'WEIGHT', true, 'oz', 'Imperial weight measurement', '990e8400-e29b-41d4-a716-446655440004', 0.0283495, '660e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert businesses
INSERT INTO business (id, code, name, type, status, description, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('880e8400-e29b-41d4-a716-446655440010', 'LUXFASHION', 'Luxury Fashion House', 'RETAIL', 'ACTIVE', 'High-end fashion retail business', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440011', 'SPORTSWEAR', 'Sports & Active Wear', 'RETAIL', 'ACTIVE', 'Sports and athletic wear retail', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440012', 'KIDSFASHION', 'Kids Fashion World', 'RETAIL', 'ACTIVE', 'Children''s clothing and accessories', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440009', 'LUXURYBRAND', 'Luxury Brand Co', 'RETAIL', 'ACTIVE', 'Premium luxury fashion brand', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440010', 'PREMIUMSPORTS', 'Premium Sports', 'RETAIL', 'ACTIVE', 'High-end sports equipment and apparel', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440011', 'DESIGNERJEWELRY', 'Designer Jewelry', 'RETAIL', 'ACTIVE', 'Exclusive jewelry and accessories', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert catalogs for each business
INSERT INTO catalog (id, code, name, type, status, business_id, description, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('880e8400-e29b-41d4-a716-446655440013', 'LUX_SPRING_2025', 'Spring Collection 2025', 'SEASONAL', 'ACTIVE', '880e8400-e29b-41d4-a716-446655440010', 'Luxury spring fashion collection', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440014', 'SPORT_SS_2025', 'Sports Summer 2025', 'SEASONAL', 'ACTIVE', '880e8400-e29b-41d4-a716-446655440011', 'Summer sports collection', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440015', 'KIDS_BASIC_2025', 'Kids Basics 2025', 'REGULAR', 'ACTIVE', '880e8400-e29b-41d4-a716-446655440012', 'Essential children''s wear', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440012', 'LUX_FW_2025', 'Fall/Winter 2025', 'SEASONAL', 'ACTIVE', '990e8400-e29b-41d4-a716-446655440009', 'Fall/Winter luxury collection', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440013', 'PREMIUM_SPORTS_2025', 'Premium Sports 2025', 'REGULAR', 'ACTIVE', '990e8400-e29b-41d4-a716-446655440010', 'Premium sports collection', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440014', 'JEWELRY_2025', 'Jewelry Collection 2025', 'REGULAR', 'ACTIVE', '990e8400-e29b-41d4-a716-446655440011', 'Designer jewelry collection', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert categories
INSERT INTO category (id, business_id, catalog_id, parent_id, name, code, type, status, description, level, created_date, last_modified_date, created_by, last_modified_by)
VALUES
-- Luxury Fashion Categories
('880e8400-e29b-41d4-a716-446655440016', '880e8400-e29b-41d4-a716-446655440010', '880e8400-e29b-41d4-a716-446655440013', NULL, 'Women''s Wear', 'LUX_WOMEN', 'MAIN', 'ACTIVE', 'Luxury women''s clothing', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440017', '880e8400-e29b-41d4-a716-446655440010', '880e8400-e29b-41d4-a716-446655440013', '880e8400-e29b-41d4-a716-446655440016', 'Evening Dresses', 'LUX_WOMEN_DRESS', 'SUB', 'ACTIVE', 'Formal evening wear', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440018', '880e8400-e29b-41d4-a716-446655440010', '880e8400-e29b-41d4-a716-446655440013', '880e8400-e29b-41d4-a716-446655440016', 'Accessories', 'LUX_WOMEN_ACC', 'SUB', 'ACTIVE', 'Women''s accessories', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Sports Wear Categories
('880e8400-e29b-41d4-a716-446655440019', '880e8400-e29b-41d4-a716-446655440011', '880e8400-e29b-41d4-a716-446655440014', NULL, 'Athletic Wear', 'SPORT_ATHLETIC', 'MAIN', 'ACTIVE', 'Athletic clothing', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440020', '880e8400-e29b-41d4-a716-446655440011', '880e8400-e29b-41d4-a716-446655440014', '880e8400-e29b-41d4-a716-446655440019', 'Running Gear', 'SPORT_RUNNING', 'SUB', 'ACTIVE', 'Running specific clothing', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Kids Fashion Categories
('880e8400-e29b-41d4-a716-446655440021', '880e8400-e29b-41d4-a716-446655440012', '880e8400-e29b-41d4-a716-446655440015', NULL, 'Boys Wear', 'KIDS_BOYS', 'MAIN', 'ACTIVE', 'Boys clothing', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440022', '880e8400-e29b-41d4-a716-446655440012', '880e8400-e29b-41d4-a716-446655440015', NULL, 'Girls Wear', 'KIDS_GIRLS', 'MAIN', 'ACTIVE', 'Girls clothing', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Luxury Brand Categories
('990e8400-e29b-41d4-a716-446655440015', '990e8400-e29b-41d4-a716-446655440009', '990e8400-e29b-41d4-a716-446655440012', NULL, 'Luxury Apparel', 'LUX_APPAREL', 'MAIN', 'ACTIVE', 'Premium clothing collection', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440016', '990e8400-e29b-41d4-a716-446655440009', '990e8400-e29b-41d4-a716-446655440012', '990e8400-e29b-41d4-a716-446655440015', 'Designer Suits', 'LUX_SUITS', 'SUB', 'ACTIVE', 'Premium suits collection', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Premium Sports Categories
('990e8400-e29b-41d4-a716-446655440017', '990e8400-e29b-41d4-a716-446655440010', '990e8400-e29b-41d4-a716-446655440013', NULL, 'Premium Equipment', 'PREMIUM_EQUIP', 'MAIN', 'ACTIVE', 'High-end sports equipment', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440018', '990e8400-e29b-41d4-a716-446655440010', '990e8400-e29b-41d4-a716-446655440013', '990e8400-e29b-41d4-a716-446655440017', 'Golf Equipment', 'PREMIUM_GOLF', 'SUB', 'ACTIVE', 'Premium golf equipment', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert feature templates
INSERT INTO feature_template (
    id,
    template_type,
    name,
    description,
    feature_type,
    data_type,
    input_type,
    validation_pattern,
    min_value,
    max_value,
    allowed_values,
    default_value,
    unit_id,
    required,
    filterable,
    hidden,
    multi_valued,
    searchable,
    comparable,
    visible,
    editable,
    created_date,
    last_modified_date,
    created_by,
    last_modified_by
)
VALUES
-- Size template
('770e8400-e29b-41d4-a716-446655440020', 'CATEGORY', 'Size', 'Size specification', 'SIZE', 'STRING', 'ENUM', NULL, NULL, NULL, '["XS","S","M","L","XL"]', 'M', NULL, true, true, false, false, true, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Color template
('770e8400-e29b-41d4-a716-446655440021', 'CATEGORY', 'Color', 'Color specification', 'COLOR', 'STRING', 'ENUM', NULL, NULL, NULL, '["Black","White","Red","Blue","Green"]', 'Black', NULL, true, true, false, false, true, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Material template
('770e8400-e29b-41d4-a716-446655440022', 'CATEGORY', 'Material', 'Material specification', 'MATERIAL', 'STRING', 'ENUM', NULL, NULL, NULL, '["Cotton","Silk","Wool","Polyester"]', 'Cotton', NULL, true, true, false, false, true, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Weight template
('770e8400-e29b-41d4-a716-446655440023', 'CATEGORY', 'Weight', 'Weight specification', 'WEIGHT', 'NUMBER', 'NUMBER', NULL, '0.1', '100.0', NULL, '0.5', '770e8400-e29b-41d4-a716-446655440002', true, true, false, false, true, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert category feature templates
INSERT INTO category_feature_template (
    id,
    code,
    attribute_type,
    inherited,
    mandatory,
    metadata,
    category_id,
    created_date,
    last_modified_date,
    created_by,
    last_modified_by
)
VALUES
-- Size template category specifics
('770e8400-e29b-41d4-a716-446655440020', 'SIZE_TEMPLATE', 'MEASUREMENT', false, true, '{}', '880e8400-e29b-41d4-a716-446655440016', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Color template category specifics
('770e8400-e29b-41d4-a716-446655440021', 'COLOR_TEMPLATE', 'ENUM', false, true, '{}', '880e8400-e29b-41d4-a716-446655440016', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Material template category specifics
('770e8400-e29b-41d4-a716-446655440022', 'MATERIAL_TEMPLATE', 'ENUM', false, true, '{}', '880e8400-e29b-41d4-a716-446655440016', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Weight template category specifics
('770e8400-e29b-41d4-a716-446655440023', 'WEIGHT_TEMPLATE', 'MEASUREMENT', false, true, '{}', '880e8400-e29b-41d4-a716-446655440016', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert products
INSERT INTO product (id, name, description, code, product_type, status, business_id, catalog_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
-- Luxury Evening Dresses
('880e8400-e29b-41d4-a716-446655440034', 'Silk Evening Gown', 'Elegant silk evening gown', 'LUX_DRESS_001', 'CLOTHING', 'ACTIVE', '880e8400-e29b-41d4-a716-446655440010', '880e8400-e29b-41d4-a716-446655440013', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440035', 'Velvet Cocktail Dress', 'Sophisticated cocktail dress', 'LUX_DRESS_002', 'CLOTHING', 'ACTIVE', '880e8400-e29b-41d4-a716-446655440010', '880e8400-e29b-41d4-a716-446655440013', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Sports Running Gear
('880e8400-e29b-41d4-a716-446655440036', 'Pro Runner Jacket', 'Professional running jacket', 'SPORT_RUN_001', 'CLOTHING', 'ACTIVE', '880e8400-e29b-41d4-a716-446655440011', '880e8400-e29b-41d4-a716-446655440014', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Designer Suits
('990e8400-e29b-41d4-a716-446655440025', 'Cashmere Blend Suit', 'Premium cashmere blend suit', 'LUX_SUIT_001', 'CLOTHING', 'ACTIVE', '990e8400-e29b-41d4-a716-446655440010', '990e8400-e29b-41d4-a716-446655440013', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Golf Equipment
('990e8400-e29b-41d4-a716-446655440027', 'Pro Golf Driver', 'Professional golf driver', 'GOLF_001', 'EQUIPMENT', 'ACTIVE', '990e8400-e29b-41d4-a716-446655440010', '990e8400-e29b-41d4-a716-446655440013', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert product features
INSERT INTO product_feature (
    id, 
    code, 
    name, 
    description, 
    product_id,
    template_id,
    feature_type,
    data_type,
    input_type,
    attribute_type,
    validation_pattern,
    min_value, 
    max_value, 
    allowed_values,
    default_value,
    visible,
    editable,
    searchable,
    comparable,
    required,
    multi_valued,
    unit_id,
    metadata,
    created_date,
    last_modified_date,
    created_by,
    last_modified_by
)
VALUES
-- Features for Silk Evening Gown (product_id: 880e8400-e29b-41d4-a716-446655440034)
('880e8400-e29b-41d4-a716-446655440038', 'DRESS_SIZE_001', 'Size', 'Dress size', 
 '880e8400-e29b-41d4-a716-446655440034', '770e8400-e29b-41d4-a716-446655440015', 'SIZE',
 'STRING', 'ENUM', 'MEASUREMENT', NULL, '32', '44', '["32","34","36","38","40","42","44"]', '36',
 true, true, true, true, false, false, '990e8400-e29b-41d4-a716-446655440001', NULL,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

('880e8400-e29b-41d4-a716-446655440039', 'DRESS_COLOR_001', 'Color', 'Dress color',
 '880e8400-e29b-41d4-a716-446655440034', '770e8400-e29b-41d4-a716-446655440016', 'COLOR',
 'STRING', 'ENUM', 'ENUM', NULL, NULL, NULL, '["Red","Black","Blue","White"]', 'Black',
 true, true, true, true, false, false, NULL, NULL,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

('880e8400-e29b-41d4-a716-446655440040', 'DRESS_MATERIAL_001', 'Material', 'Dress material',
 '880e8400-e29b-41d4-a716-446655440034', '770e8400-e29b-41d4-a716-446655440017', 'MATERIAL',
 'STRING', 'ENUM', 'ENUM', NULL, NULL, NULL, '["Cotton","Silk","Wool","Polyester"]', 'Silk',
 true, true, true, true, false, false, NULL, NULL,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Features for Pro Runner Jacket (product_id: 880e8400-e29b-41d4-a716-446655440036)
('880e8400-e29b-41d4-a716-446655440041', 'RUN_SIZE_001', 'Size', 'Jacket size',
 '880e8400-e29b-41d4-a716-446655440036', '770e8400-e29b-41d4-a716-446655440015', 'SIZE',
 'STRING', 'ENUM', 'ENUM', NULL, 'XS', 'XXL', '["XS","S","M","L","XL","XXL"]', 'M',
 true, true, true, true, false, false, NULL, NULL,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

('880e8400-e29b-41d4-a716-446655440042', 'RUN_COLOR_001', 'Color', 'Jacket color',
 '880e8400-e29b-41d4-a716-446655440036', '770e8400-e29b-41d4-a716-446655440016', 'COLOR',
 'STRING', 'ENUM', 'ENUM', NULL, NULL, NULL, '["Black","Blue","Neon Yellow","Grey"]', 'Black',
 true, true, true, true, false, false, NULL, NULL,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

('880e8400-e29b-41d4-a716-446655440043', 'RUN_MATERIAL_001', 'Material', 'Jacket material',
 '880e8400-e29b-41d4-a716-446655440036', '770e8400-e29b-41d4-a716-446655440017', 'MATERIAL',
 'STRING', 'ENUM', 'ENUM', NULL, NULL, NULL, '["Polyester","Spandex","Mesh"]', 'Polyester',
 true, true, true, true, false, false, NULL, NULL,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Features for Cashmere Suit (product_id: 990e8400-e29b-41d4-a716-446655440025)
('990e8400-e29b-41d4-a716-446655440028', 'SUIT_SIZE_001', 'Size', 'Suit size',
 '990e8400-e29b-41d4-a716-446655440025', '770e8400-e29b-41d4-a716-446655440015', 'SIZE',
 'STRING', 'ENUM', 'MEASUREMENT', NULL, '36', '48', '["36","38","40","42","44","46","48"]', '40',
 true, true, true, true, false, false, NULL, NULL,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

('990e8400-e29b-41d4-a716-446655440029', 'SUIT_MATERIAL_001', 'Material', 'Suit material',
 '990e8400-e29b-41d4-a716-446655440025', '770e8400-e29b-41d4-a716-446655440017', 'MATERIAL',
 'STRING', 'ENUM', 'ENUM', NULL, NULL, NULL, '["Wool","Cashmere","Silk","Linen"]', 'Cashmere',
 true, true, true, true, false, false, NULL, NULL,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

-- Features for Pro Golf Driver (product_id: 990e8400-e29b-41d4-a716-446655440027)
('990e8400-e29b-41d4-a716-446655440030', 'GOLF_WEIGHT_001', 'Weight', 'Club weight',
 '990e8400-e29b-41d4-a716-446655440027', '770e8400-e29b-41d4-a716-446655440018', 'WEIGHT',
 'STRING', 'TEXT', 'MEASUREMENT', NULL, '0.2', '0.5', NULL, '13',
 true, true, true, true, false, false, '990e8400-e29b-41d4-a716-446655440004', NULL,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert product feature values
INSERT INTO product_feature_value (
    id,
    product_id,
    feature_id,
    template_id,
    type,
    unit,
    unit_of_measure,
    status,
    validation_status,
    validation_pattern,
    validation_message,
    attribute_values,
    metadata,
    created_date,
    last_modified_date,
    created_by,
    last_modified_by
)
VALUES
-- Values for Silk Evening Gown
('880e8400-e29b-41d4-a716-446655440044', 
 '880e8400-e29b-41d4-a716-446655440034',
 '880e8400-e29b-41d4-a716-446655440038',
 '770e8400-e29b-41d4-a716-446655440020',
 'SIZE', NULL, NULL, 'ACTIVE', 'VALID', NULL, NULL,
 '{"size": {"value": 36, "unit": "EU"}}',
 '{"lastUpdated": "2025-01-07T10:52:15+05:30"}',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

('880e8400-e29b-41d4-a716-446655440045',
 '880e8400-e29b-41d4-a716-446655440034',
 '880e8400-e29b-41d4-a716-446655440039',
 '770e8400-e29b-41d4-a716-446655440021',
 'COLOR', NULL, NULL, 'ACTIVE', 'VALID', NULL, NULL,
 '{"color": {"primary": "Black", "secondary": null}}',
 '{"lastUpdated": "2025-01-07T10:52:15+05:30"}',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),

('880e8400-e29b-41d4-a716-446655440046',
 '880e8400-e29b-41d4-a716-446655440034',
 '880e8400-e29b-41d4-a716-446655440040',
 '770e8400-e29b-41d4-a716-446655440022',
 'MATERIAL', NULL, NULL, 'ACTIVE', 'VALID', NULL, NULL,
 '{"material": {"composition": {"silk": 100}, "care": {"dryClean": true, "temperature": "30C"}, "origin": "Italy"}}',
 '{"lastUpdated": "2025-01-07T10:52:15+05:30"}',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');
