-- Insert units for clothing and accessories
INSERT INTO unit (id, code, name, type, symbol, description, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('770e8400-e29b-41d4-a716-446655440000', 'SIZE_EU', 'EU Size', 'SIZE', 'EU', 'European clothing size', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440001', 'SIZE_US', 'US Size', 'SIZE', 'US', 'US clothing size', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440002', 'CM', 'Centimeter', 'LENGTH', 'cm', 'Length measurement', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert base units for clothing
INSERT INTO unit_of_measure (id, code, name, description, unit_id, type, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('770e8400-e29b-41d4-a716-446655440003', 'SIZE_EU', 'EU Size', 'European size standard', '770e8400-e29b-41d4-a716-446655440000', 'SIZE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440004', 'SIZE_US', 'US Size', 'US size standard', '770e8400-e29b-41d4-a716-446655440001', 'SIZE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert business for fashion retail
INSERT INTO business (id, code, name, description, type, status, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('770e8400-e29b-41d4-a716-446655440005', 'FashionNLifeStyle','Fashion Retail Co', 'Fashion and accessories retail business', 'RETAIL', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert catalog for the fashion business
INSERT INTO catalog (id, code, name, description, type, status, business_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('770e8400-e29b-41d4-a716-446655440006', 'summer collection','Summer Collection 2025', 'Summer fashion collection', 'SEASONAL', 'ACTIVE', '770e8400-e29b-41d4-a716-446655440005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert categories for fashion items
INSERT INTO category (id, business_id, code, name, description, type, status, catalog_id, parent_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('770e8400-e29b-41d4-a716-446655440007', '770e8400-e29b-41d4-a716-446655440005','Clothing','Clothing', 'All clothing items', 'MAIN', 'ACTIVE', '770e8400-e29b-41d4-a716-446655440006', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440008', '770e8400-e29b-41d4-a716-446655440005','Accessories','Accessories', 'Fashion accessories', 'MAIN', 'ACTIVE', '770e8400-e29b-41d4-a716-446655440006', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440009', '770e8400-e29b-41d4-a716-446655440005','Dresses','Dresses', 'Women''s dresses', 'SUB', 'ACTIVE', '770e8400-e29b-41d4-a716-446655440006', '770e8400-e29b-41d4-a716-446655440007', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440010', '770e8400-e29b-41d4-a716-446655440005','Bags','Bags', 'Fashion bags', 'SUB', 'ACTIVE', '770e8400-e29b-41d4-a716-446655440006', '770e8400-e29b-41d4-a716-446655440008', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert base feature templates
INSERT INTO feature_template (id, name, description, feature_type, data_type, input_type, required, searchable, filterable, comparable, hidden, default_value, unit_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
-- Size template
('770e8400-e29b-41d4-a716-446655440011', 'Size', 'Generic size feature', 'SIZE', 'STRING', 'ENUM', true, true, true, true, false, 'M', '770e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Color template
('770e8400-e29b-41d4-a716-446655440012', 'Color', 'Generic color feature', 'COLOR', 'STRING', 'ENUM', true, true, true, true, false, 'Black', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Material template
('770e8400-e29b-41d4-a716-446655440013', 'Material', 'Generic material feature', 'MATERIAL', 'STRING', 'ENUM', true, true, true, true, false, 'Cotton', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Dimensions template
('770e8400-e29b-41d4-a716-446655440014', 'Dimensions', 'Generic dimensions feature', 'DIMENSIONS', 'STRING', 'TEXT', true, false, false, false, false, NULL, '770e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert category feature templates
INSERT INTO category_feature_template (id, name, description, code, feature_type, template_id, validation_pattern, min_value, max_value, allowed_values, attribute_type, comparable, visible, searchable, editable, multi_valued, default_value, unit_id, required, category_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
-- Size template for clothing
('770e8400-e29b-41d4-a716-446655440015', 'Clothing Size', 'Clothing size specification', 'SIZE_CLOTHING', 'SIZE', '770e8400-e29b-41d4-a716-446655440011', NULL, NULL, NULL, 'XS,S,M,L,XL', 'ENUM', true, true, true, true, false, 'M', '770e8400-e29b-41d4-a716-446655440000', true, '770e8400-e29b-41d4-a716-446655440007', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Color template for clothing
('770e8400-e29b-41d4-a716-446655440016', 'Clothing Color', 'Product color specification', 'COLOR_CLOTHING', 'COLOR', '770e8400-e29b-41d4-a716-446655440012', NULL, NULL, NULL, 'White,Black,Red,Blue,Green', 'ENUM', true, true, true, true, false, 'Black', NULL, true, '770e8400-e29b-41d4-a716-446655440007', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Material template for clothing
('770e8400-e29b-41d4-a716-446655440017', 'Clothing Material', 'Fabric material specification', 'MATERIAL_CLOTHING', 'MATERIAL', '770e8400-e29b-41d4-a716-446655440013', NULL, NULL, NULL, 'Cotton,Silk,Polyester,Leather', 'ENUM', true, true, true, true, false, 'Cotton', NULL, true, '770e8400-e29b-41d4-a716-446655440007', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Dimensions template for accessories
('770e8400-e29b-41d4-a716-446655440018', 'Accessory Dimensions', 'Product dimensions specification', 'DIMENSIONS_ACCESSORY', 'DIMENSIONS', '770e8400-e29b-41d4-a716-446655440014', NULL, NULL, NULL, NULL, 'TEXT', true, true, false, true, false, NULL, '770e8400-e29b-41d4-a716-446655440002', true, '770e8400-e29b-41d4-a716-446655440008', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert products
INSERT INTO product (id, name, description, code, product_type, status, business_id, catalog_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('770e8400-e29b-41d4-a716-446655440019', 'Summer Floral Dress', 'Beautiful floral print summer dress', 'DRESS001', 'CLOTHING', 'ACTIVE', '770e8400-e29b-41d4-a716-446655440005', '770e8400-e29b-41d4-a716-446655440006', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440020', 'Leather Tote Bag', 'Elegant leather tote bag', 'BAG001', 'ACCESSORY', 'ACTIVE', '770e8400-e29b-41d4-a716-446655440005', '770e8400-e29b-41d4-a716-446655440006', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert product features
INSERT INTO product_feature (id, code, name, description, feature_type,  product_id, template_id, unit_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
-- Features for Summer Floral Dress
('770e8400-e29b-41d4-a716-446655440021', 'Size','Size', 'Dress size', 'SIZE',  '770e8400-e29b-41d4-a716-446655440019', '770e8400-e29b-41d4-a716-446655440015', '770e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440022', 'Color','Color', 'Dress color', 'COLOR', '770e8400-e29b-41d4-a716-446655440019', '770e8400-e29b-41d4-a716-446655440016', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440023', 'Material','Material', 'Dress material', 'MATERIAL', '770e8400-e29b-41d4-a716-446655440019', '770e8400-e29b-41d4-a716-446655440017', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Features for Leather Tote Bag
('770e8400-e29b-41d4-a716-446655440024', 'Color','Color', 'Bag color', 'COLOR',  '770e8400-e29b-41d4-a716-446655440020', '770e8400-e29b-41d4-a716-446655440016', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440025', 'Material','Material', 'Bag material', 'MATERIAL', '770e8400-e29b-41d4-a716-446655440020', '770e8400-e29b-41d4-a716-446655440017', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440026', 'Dimensions','Dimensions', 'Bag dimensions', 'DIMENSIONS', '770e8400-e29b-41d4-a716-446655440020', '770e8400-e29b-41d4-a716-446655440018', '770e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert product feature values
INSERT INTO product_feature_value (id, feature_id, value, created_date, last_modified_date, created_by, last_modified_by)
VALUES
-- Values for Summer Floral Dress
('770e8400-e29b-41d4-a716-446655440027', '770e8400-e29b-41d4-a716-446655440021', 'M', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440028', '770e8400-e29b-41d4-a716-446655440022', 'Blue', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440029', '770e8400-e29b-41d4-a716-446655440023', 'Cotton', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Values for Leather Tote Bag
('770e8400-e29b-41d4-a716-446655440030', '770e8400-e29b-41d4-a716-446655440024', 'Brown', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440031', '770e8400-e29b-41d4-a716-446655440025', 'Leather', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440032', '770e8400-e29b-41d4-a716-446655440026', '30x40x10 cm', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');
