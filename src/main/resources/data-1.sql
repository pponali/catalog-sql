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
('bb1e8400-e29b-41d4-a716-446655440000', 'FashionNLifeStyle','Fashion Retail Co', 'Fashion and accessories retail business', 'RETAIL', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert catalog for the fashion business
INSERT INTO catalog (id, code, name, description, type, status, business_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('cc1e8400-e29b-41d4-a716-446655440000', 'summer collection','Summer Collection 2025', 'Summer fashion collection', 'SEASONAL', 'ACTIVE', 'bb1e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert categories for fashion items
INSERT INTO category (id, business_id, code, name, description, type, status, catalog_id, parent_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('dd1e8400-e29b-41d4-a716-446655440000', 'bb1e8400-e29b-41d4-a716-446655440000','Clothing','Clothing', 'All clothing items', 'MAIN', 'ACTIVE', 'cc1e8400-e29b-41d4-a716-446655440000', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('dd1e8400-e29b-41d4-a716-446655440001', 'bb1e8400-e29b-41d4-a716-446655440000','Accessories','Accessories', 'Fashion accessories', 'MAIN', 'ACTIVE', 'cc1e8400-e29b-41d4-a716-446655440000', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('dd1e8400-e29b-41d4-a716-446655440002', 'bb1e8400-e29b-41d4-a716-446655440000','Dresses','Dresses', 'Women''s dresses', 'SUB', 'ACTIVE', 'cc1e8400-e29b-41d4-a716-446655440000', 'dd1e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('dd1e8400-e29b-41d4-a716-446655440003', 'bb1e8400-e29b-41d4-a716-446655440000','Bags','Bags', 'Fashion bags', 'SUB', 'ACTIVE', 'cc1e8400-e29b-41d4-a716-446655440000', 'dd1e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert base feature templates
INSERT INTO feature_template (id, name, description, feature_type, data_type, allowed_values, input_type, required, searchable, filterable, comparable, hidden, default_value, unit_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
-- Size template
('ft1e8400-e29b-41d4-a716-446655440000', 'Size', 'Generic size feature', 'SIZE', 'STRING', 'XS,S,M,L,XL', 'ENUM', true, true, true, true, false, 'M', '770e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Color template
('ft1e8400-e29b-41d4-a716-446655440001', 'Color', 'Generic color feature', 'COLOR', 'STRING', 'White,Black,Red,Blue,Green', 'ENUM', true, true, true, true, false, 'Black', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Material template
('ft1e8400-e29b-41d4-a716-446655440002', 'Material', 'Generic material feature', 'MATERIAL', 'STRING', 'Cotton,Silk,Polyester,Leather', 'ENUM', true, true, true, true, false, 'Cotton', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Dimensions template
('ft1e8400-e29b-41d4-a716-446655440003', 'Dimensions', 'Generic dimensions feature', 'DIMENSIONS', 'STRING', NULL, 'TEXT', true, false, false, false, false, NULL, '770e8400-e29b-41d4-a716-446655440005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert feature templates for fashion items
INSERT INTO category_feature_template (id, name, description, feature_type, template_id, data_type, min_value, max_value, step, allowed_values, input_type, required, searchable, filterable, comparable, hidden, default_value, unit_id, inherited, category_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
-- Size template for clothing
('ff1e8400-e29b-41d4-a716-446655440000', 'Clothing Size', 'Clothing size specification', 'SIZE', 'ft1e8400-e29b-41d4-a716-446655440000', 'STRING', NULL, NULL, NULL, 'XS,S,M,L,XL', 'ENUM', true, true, true, true, false, 'M', '770e8400-e29b-41d4-a716-446655440003', true, 'dd1e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Color template for clothing
('ff1e8400-e29b-41d4-a716-446655440001', 'Clothing Color', 'Product color specification', 'COLOR', 'ft1e8400-e29b-41d4-a716-446655440001', 'STRING', NULL, NULL, NULL, 'White,Black,Red,Blue,Green', 'ENUM', true, true, true, true, false, 'Black', NULL, true, 'dd1e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Material template for clothing
('ff1e8400-e29b-41d4-a716-446655440002', 'Clothing Material', 'Fabric material specification', 'MATERIAL', 'ft1e8400-e29b-41d4-a716-446655440002', 'STRING', NULL, NULL, NULL, 'Cotton,Silk,Polyester,Leather', 'ENUM', true, true, true, true, false, 'Cotton', NULL, true, 'dd1e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Dimensions template for accessories
('ff1e8400-e29b-41d4-a716-446655440003', 'Accessory Dimensions', 'Product dimensions specification', 'DIMENSIONS', 'ft1e8400-e29b-41d4-a716-446655440003', 'STRING', NULL, NULL, NULL, NULL, 'TEXT', true, false, false, false, false, NULL, '770e8400-e29b-41d4-a716-446655440005', true, 'dd1e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert products
INSERT INTO product (id, name, description, code, type, status, business_id, catalog_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('pp1e8400-e29b-41d4-a716-446655440000', 'Summer Floral Dress', 'Floral print summer dress', 'DRESS-001', 'CLOTHING', 'ACTIVE', 'bb1e8400-e29b-41d4-a716-446655440000', 'cc1e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('pp1e8400-e29b-41d4-a716-446655440001', 'Leather Tote Bag', 'Classic leather tote bag', 'BAG-001', 'ACCESSORY', 'ACTIVE', 'bb1e8400-e29b-41d4-a716-446655440000', 'cc1e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert product categories
INSERT INTO product_category (product_id, category_id)
VALUES
('pp1e8400-e29b-41d4-a716-446655440000', 'dd1e8400-e29b-41d4-a716-446655440002'),
('pp1e8400-e29b-41d4-a716-446655440001', 'dd1e8400-e29b-41d4-a716-446655440003');

-- Insert product features
INSERT INTO product_feature (id, name, description, type, status, product_id, template_id, unit_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
-- Features for Summer Floral Dress
('pf1e8400-e29b-41d4-a716-446655440000', 'Size', 'Dress size', 'SIZE', 'ACTIVE', 'pp1e8400-e29b-41d4-a716-446655440000', 'ff1e8400-e29b-41d4-a716-446655440000', '770e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('pf1e8400-e29b-41d4-a716-446655440001', 'Color', 'Dress color', 'COLOR', 'ACTIVE', 'pp1e8400-e29b-41d4-a716-446655440000', 'ff1e8400-e29b-41d4-a716-446655440001', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('pf1e8400-e29b-41d4-a716-446655440002', 'Material', 'Dress material', 'MATERIAL', 'ACTIVE', 'pp1e8400-e29b-41d4-a716-446655440000', 'ff1e8400-e29b-41d4-a716-446655440002', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Features for Leather Tote Bag
('pf1e8400-e29b-41d4-a716-446655440003', 'Color', 'Bag color', 'COLOR', 'ACTIVE', 'pp1e8400-e29b-41d4-a716-446655440001', 'ff1e8400-e29b-41d4-a716-446655440001', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('pf1e8400-e29b-41d4-a716-446655440004', 'Material', 'Bag material', 'MATERIAL', 'ACTIVE', 'pp1e8400-e29b-41d4-a716-446655440001', 'ff1e8400-e29b-41d4-a716-446655440002', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('pf1e8400-e29b-41d4-a716-446655440005', 'Dimensions', 'Bag dimensions', 'DIMENSIONS', 'ACTIVE', 'pp1e8400-e29b-41d4-a716-446655440001', 'ff1e8400-e29b-41d4-a716-446655440003', '770e8400-e29b-41d4-a716-446655440005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert product feature values
INSERT INTO product_feature_value (id, feature_id, value, created_date, last_modified_date, created_by, last_modified_by)
VALUES
-- Values for Summer Floral Dress
('pfv1e8400-e29b-41d4-a716-446655440000', 'pf1e8400-e29b-41d4-a716-446655440000', 'M', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('pfv1e8400-e29b-41d4-a716-446655440001', 'pf1e8400-e29b-41d4-a716-446655440001', 'Blue', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('pfv1e8400-e29b-41d4-a716-446655440002', 'pf1e8400-e29b-41d4-a716-446655440002', 'Cotton', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
-- Values for Leather Tote Bag
('pfv1e8400-e29b-41d4-a716-446655440003', 'pf1e8400-e29b-41d4-a716-446655440003', 'Black', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('pfv1e8400-e29b-41d4-a716-446655440004', 'pf1e8400-e29b-41d4-a716-446655440004', 'Leather', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('pfv1e8400-e29b-41d4-a716-446655440005', 'pf1e8400-e29b-41d4-a716-446655440005', '40x30x15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');
