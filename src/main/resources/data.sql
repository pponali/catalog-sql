-- Drop and recreate sequences
DROP SEQUENCE IF EXISTS category_seq CASCADE;
DROP SEQUENCE IF EXISTS product_seq CASCADE;
DROP SEQUENCE IF EXISTS product_feature_seq CASCADE;
DROP SEQUENCE IF EXISTS category_feature_template_seq CASCADE;
DROP SEQUENCE IF EXISTS classification_attribute_seq CASCADE;
DROP SEQUENCE IF EXISTS class_attribute_assignment_seq CASCADE;
DROP SEQUENCE IF EXISTS unit_seq CASCADE;

CREATE SEQUENCE IF NOT EXISTS category_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS product_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS product_feature_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS category_feature_template_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS classification_attribute_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS class_attribute_assignment_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS unit_seq START WITH 1000;

-- Units (no dependencies)
INSERT INTO unit (id, code, name, description, created_date, last_modified_date)
VALUES 
(1, 'SIZE', 'Size', 'Size measurements', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'STYLE', 'Style', 'Style type', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'DAYS', 'Days', 'Time duration in days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'CERTIFICATION', 'Certification', 'Certification type', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'NUTRITION', 'Nutrition', 'Nutritional measurements', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'MEDICINE', 'Medicine', 'Medicine measurements', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Root Categories
INSERT INTO category (id, code, name, description, parent_id, dtype, created_date, last_modified_date)
VALUES 
(1, 'MSH1230', 'Home Appliances', 'Home and Kitchen Appliances', NULL, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'apparelCCWEE', 'Yeswee Apparel', 'Fashion and Clothing', NULL, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'BIGBASKET', 'BigBasket', 'Fresh Groceries and Daily Essentials', NULL, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, '1MG', '1mg', 'Healthcare and Wellness', NULL, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'TANISHQ', 'Tanishq', 'Premium Jewelry', NULL, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'ELECTRONICS', 'Electronics', 'Electronics and Gadgets', NULL, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Sub Categories
INSERT INTO category (id, code, name, description, parent_id, dtype, created_date, last_modified_date)
VALUES 
(10, 'MPH11111L4', 'Split AC NEW', 'Split Air Conditioners', 1, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'MPWEAR001', 'Western Wear', 'Western Style Clothing', 2, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'MPWEAR002', 'Ethnic Wear', 'Traditional and Ethnic Clothing', 2, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(20, 'BB_FRUITS', 'Fresh Fruits', 'Fresh and Seasonal Fruits', 3, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(21, 'BB_VEG', 'Fresh Vegetables', 'Fresh and Organic Vegetables', 3, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(22, 'BB_DAIRY', 'Dairy & Eggs', 'Fresh Dairy Products and Eggs', 3, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(30, '1MG_MEDICINES', 'Medicines', 'Prescription and OTC Medicines', 4, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(31, '1MG_WELLNESS', 'Health & Wellness', 'Wellness and Nutrition Products', 4, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(32, '1MG_PERSONAL', 'Personal Care', 'Personal Care Products', 4, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(40, 'TANQ_GOLD', 'Gold Jewelry', '22K Gold Jewelry Collection', 5, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(41, 'TANQ_DIAMOND', 'Diamond Jewelry', 'Premium Diamond Collection', 5, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(42, 'TANQ_WEDDING', 'Wedding Collection', 'Bridal Jewelry Collection', 5, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Products
INSERT INTO product (
    id, code, name, description, product_type,
    status, metadata, sku,
    created_date, last_modified_date
)
VALUES 
(1003, 'FORMAL_SHIRT_001', 'Men''s Formal Shirt', 'Classic formal shirt for men',
 'APPAREL', 'ACTIVE',
 '{"brand": "ClassicWear", "department": "mens", "category": "formal_wear"}',
 'SHIRT-F-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(1004, 'CASUAL_TSHIRT_001', 'Men''s Casual T-Shirt', 'Comfortable casual t-shirt',
 'APPAREL', 'ACTIVE',
 '{"brand": "ComfortWear", "department": "mens", "category": "casual_wear"}',
 'TSHIRT-C-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(1005, 'FOOD_001', 'Organic Quinoa', 'Premium organic quinoa', 
 'FOOD', 'ACTIVE', 
 '{"origin": "Peru", "packaging": "1kg"}', 
 'SKU003', 
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(1006, 'FOOD_002', 'Organic Chia Seeds', 'Premium organic chia seeds', 
 'FOOD', 'ACTIVE', 
 '{"origin": "Mexico", "packaging": "500g"}', 
 'SKU004', 
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(1007, 'MED_001', 'Paracetamol', 'Pain relief medication', 
 'MEDICINE', 'ACTIVE', 
 '{"manufacturer": "PharmaX", "dosage": "500mg"}', 
 'SKU005', 
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Category Feature Templates
INSERT INTO category_feature_template (
    id, code, name, description,
    feature_type, validation_pattern,
    min_value, max_value, allowed_values,
    metadata, is_required,
    created_date, last_modified_date,
    category_id
)
VALUES 
(100, 'SIZE_TEMPLATE', 'Size Template', 'Template for size features',
 'ENUM', '^(S|M|L|XL)$', '0', '100', '["S", "M", "L", "XL"]',
 '{"group": "basic", "tooltip": "Select size"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 11),

(101, 'STYLE_TEMPLATE', 'Style Template', 'Template for style features',
 'ENUM', '^(casual|formal|sports)$', '0', '100', '["casual", "formal", "sports"]',
 '{"group": "basic", "tooltip": "Select style"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 11);

-- Product Features
INSERT INTO product_feature (
    id, product_id, template_id, code, name, description,
    feature_type, validation_pattern, min_value, max_value,
    allowed_values, metadata, required,
    created_date, last_modified_date,
    attribute_type, visible, editable, searchable,
    comparable, multi_valued, default_value,
    unit_id, values
)
VALUES 
(1, 1003, 100, 'SIZE_SHIRT', 'Size', 'Shirt size',
 'ENUM', '^(S|M|L|XL)$', '0', '100',
 '["S", "M", "L", "XL"]',
 '{"group": "basic", "tooltip": "Select your size", "display_order": 1}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'STRING', true, true, true, true, false, 'M',
 1, '["S", "M", "L", "XL"]'),

(2, 1003, 101, 'STYLE_SHIRT', 'Style', 'Shirt style',
 'ENUM', '^(casual|formal)$', '0', '100',
 '["casual", "formal"]',
 '{"group": "basic", "tooltip": "Select style", "display_order": 2}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'STRING', true, true, true, true, false, 'casual',
 2, '["casual", "formal"]'),

(3, 1004, 100, 'SIZE_TSHIRT', 'Size', 'T-Shirt size',
 'ENUM', '^(S|M|L|XL)$', '0', '100',
 '["S", "M", "L", "XL"]',
 '{"group": "basic", "tooltip": "Select your size", "display_order": 1}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'STRING', true, true, true, true, false, 'L',
 1, '["S", "M", "L", "XL"]'),

(4, 1004, 101, 'STYLE_TSHIRT', 'Style', 'T-Shirt style',
 'ENUM', '^(sports|casual)$', '0', '100',
 '["sports", "casual"]',
 '{"group": "basic", "tooltip": "Select style", "display_order": 2}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'STRING', true, true, true, true, false, 'sports',
 2, '["sports", "casual"]');

-- Product Categories
INSERT INTO product_categories (product_id, category_id)
VALUES 
(1003, 11), -- Shirt in Western Wear
(1004, 11), -- T-Shirt in Western Wear
(1005, 20), -- Quinoa in Fresh Foods
(1006, 22), -- Chia Seeds in Fresh Foods
(1007, 30); -- Paracetamol in Medicines

-- Product Feature Values
INSERT INTO product_feature_value (
    id, product_id, feature_id, template_id,
    type, unit, unit_of_measure, status,
    validation_status, validation_pattern,
    validation_message, attribute_values,
    created_by, created_date,
    last_modified_by, last_modified_date
)
VALUES 
-- Size values for Formal Shirt (product_id: 1003)
(1, 1003, 1, 100, 
 'ENUM', 'SIZE', NULL, 'ACTIVE',
 'VALID', '^(S|M|L|XL)$', NULL,
 '{"value": "M"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

-- Style values for Formal Shirt (product_id: 1003)
(2, 1003, 2, 101,
 'ENUM', 'STYLE', NULL, 'ACTIVE',
 'VALID', '^(casual|formal)$', NULL,
 '{"value": "formal"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

-- Size values for T-Shirt (product_id: 1004)
(3, 1004, 3, 100,
 'ENUM', 'SIZE', NULL, 'ACTIVE',
 'VALID', '^(S|M|L|XL)$', NULL,
 '{"value": "L"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

-- Style values for T-Shirt (product_id: 1004)
(4, 1004, 4, 101,
 'ENUM', 'STYLE', NULL, 'ACTIVE',
 'VALID', '^(sports|casual)$', NULL,
 '{"value": "casual"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP);
