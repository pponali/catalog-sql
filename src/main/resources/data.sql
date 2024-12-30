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
(6, 'MEDICINE', 'Medicine', 'Medicine measurements', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'WEIGHT', 'Weight', 'Weight measurements', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'VOLUME', 'Volume', 'Volume measurements', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'LENGTH', 'Length', 'Length measurements', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'TEMPERATURE', 'Temperature', 'Temperature measurements', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

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
-- Home Appliances Sub-categories
(10, 'MPH11111L4', 'Split AC', 'Split Air Conditioners', 1, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'MPH11111L5', 'Refrigerators', 'Home Refrigerators', 1, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'MPH11111L6', 'Washing Machines', 'Washing Machines', 1, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Apparel Sub-categories
(20, 'MPWEAR001', 'Western Wear', 'Western Style Clothing', 2, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(21, 'MPWEAR002', 'Ethnic Wear', 'Traditional and Ethnic Clothing', 2, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(22, 'MPWEAR003', 'Sports Wear', 'Athletic and Sports Clothing', 2, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Groceries Sub-categories
(30, 'BB_FRUITS', 'Fresh Fruits', 'Fresh and Seasonal Fruits', 3, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(31, 'BB_VEG', 'Fresh Vegetables', 'Fresh and Organic Vegetables', 3, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(32, 'BB_DAIRY', 'Dairy & Eggs', 'Fresh Dairy Products and Eggs', 3, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(33, 'BB_STAPLES', 'Staples', 'Daily Essential Staples', 3, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Healthcare Sub-categories
(40, '1MG_MEDICINES', 'Medicines', 'Prescription and OTC Medicines', 4, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(41, '1MG_WELLNESS', 'Health & Wellness', 'Wellness and Nutrition Products', 4, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(42, '1MG_PERSONAL', 'Personal Care', 'Personal Care Products', 4, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(43, '1MG_DEVICES', 'Medical Devices', 'Healthcare Devices', 4, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Jewelry Sub-categories
(50, 'TANQ_GOLD', 'Gold Jewelry', '22K Gold Jewelry Collection', 5, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(51, 'TANQ_DIAMOND', 'Diamond Jewelry', 'Premium Diamond Collection', 5, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(52, 'TANQ_PLATINUM', 'Platinum Jewelry', 'Premium Platinum Collection', 5, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Electronics Sub-categories
(60, 'ELEC_MOBILE', 'Mobile Phones', 'Smartphones and Mobile Phones', 6, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(61, 'ELEC_LAPTOP', 'Laptops', 'Laptops and Notebooks', 6, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(62, 'ELEC_AUDIO', 'Audio Devices', 'Headphones and Speakers', 6, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

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
-- Apparel Templates
(100, 'SIZE_TEMPLATE', 'Size Template', 'Template for size features',
 'ENUM', '^(S|M|L|XL)$', '0', '100', '["S", "M", "L", "XL"]',
 '{"group": "basic", "tooltip": "Select size"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 20),

(101, 'STYLE_TEMPLATE', 'Style Template', 'Template for style features',
 'ENUM', '^(casual|formal|sports)$', '0', '100', '["casual", "formal", "sports"]',
 '{"group": "basic", "tooltip": "Select style"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 20),

-- Electronics Templates
(102, 'STORAGE_TEMPLATE', 'Storage Template', 'Template for storage capacity',
 'ENUM', '^(64GB|128GB|256GB|512GB)$', '0', '1000', '["64GB", "128GB", "256GB", "512GB"]',
 '{"group": "specifications", "tooltip": "Select storage capacity"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 60),

(103, 'COLOR_TEMPLATE', 'Color Template', 'Template for color selection',
 'ENUM', '^(Black|White|Gold|Silver)$', '0', '100', '["Black", "White", "Gold", "Silver"]',
 '{"group": "appearance", "tooltip": "Select color"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 60),

-- Appliance Templates
(104, 'CAPACITY_TEMPLATE', 'Capacity Template', 'Template for appliance capacity',
 'NUMERIC', '^[0-9]+$', '0', '1000', NULL,
 '{"group": "specifications", "tooltip": "Enter capacity"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 10),

(105, 'ENERGY_RATING_TEMPLATE', 'Energy Rating Template', 'Template for energy efficiency',
 'ENUM', '^(1|2|3|4|5)$', '1', '5', '["1", "2", "3", "4", "5"]',
 '{"group": "efficiency", "tooltip": "Select energy rating"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 10),

-- Grocery Templates
(106, 'WEIGHT_TEMPLATE', 'Weight Template', 'Template for product weight',
 'NUMERIC', '^[0-9]+(\.[0-9]{1,2})?$', '0', '1000', NULL,
 '{"group": "measurements", "tooltip": "Enter weight in kg"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 30),

(107, 'EXPIRY_TEMPLATE', 'Expiry Template', 'Template for expiry date',
 'DATE', NULL, NULL, NULL, NULL,
 '{"group": "quality", "tooltip": "Select expiry date"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 30),

-- Jewelry Templates
(201, 'GOLD_PURITY', 'Gold Purity', 'Gold purity in Karats',
 'NUMERIC', '^(18|22|24)$', '18', '24', NULL,
 '{"group": "specifications", "tooltip": "Select gold purity in Karats"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5),

(202, 'DIAMOND_CERT', 'Diamond Certification', 'Diamond certification type',
 'ENUM', '^(IGI|GIA|HRD)$', NULL, NULL, '["IGI", "GIA", "HRD"]',
 '{"group": "certification", "tooltip": "Select certification type"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5),

-- Medicine Templates
(301, 'PRESCRIPTION_REQ', 'Prescription Required', 'Whether prescription is required',
 'BOOLEAN', NULL, NULL, NULL, NULL,
 '{"group": "regulations", "tooltip": "Is prescription required?"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4),

(302, 'SHELF_LIFE', 'Shelf Life', 'Product shelf life in months',
 'NUMERIC', '^[0-9]+$', '12', '36', NULL,
 '{"group": "storage", "tooltip": "Enter shelf life in months"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4);

-- Products
INSERT INTO product (
    id, code, name, description, product_type,
    status, metadata, sku,
    created_date, last_modified_date
)
VALUES 
-- Apparel Products
(1001, 'FORMAL_SHIRT_001', 'Men''s Formal Shirt', 'Classic formal shirt for men',
 'APPAREL', 'ACTIVE',
 '{"brand": "ClassicWear", "department": "mens", "category": "formal_wear"}',
 'SHIRT-F-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(1002, 'CASUAL_TSHIRT_001', 'Men''s Casual T-Shirt', 'Comfortable casual t-shirt',
 'APPAREL', 'ACTIVE',
 '{"brand": "ComfortWear", "department": "mens", "category": "casual_wear"}',
 'TSHIRT-C-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Electronics Products
(1003, 'PHONE_001', 'Smartphone X', 'Latest smartphone model',
 'ELECTRONICS', 'ACTIVE',
 '{"brand": "TechPro", "series": "X", "year": "2023"}',
 'PHONE-X-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(1004, 'LAPTOP_001', 'Pro Laptop', 'High-performance laptop',
 'ELECTRONICS', 'ACTIVE',
 '{"brand": "TechPro", "series": "Pro", "year": "2023"}',
 'LAPTOP-P-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Appliance Products
(1005, 'AC_001', 'Smart AC', 'Smart split AC with inverter technology',
 'APPLIANCE', 'ACTIVE',
 '{"brand": "CoolTech", "type": "split", "technology": "inverter"}',
 'AC-S-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(1006, 'FRIDGE_001', 'Double Door Refrigerator', 'Energy efficient refrigerator',
 'APPLIANCE', 'ACTIVE',
 '{"brand": "CoolTech", "type": "double_door", "frost_free": true}',
 'FRIDGE-D-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Grocery Products
(1007, 'RICE_001', 'Basmati Rice', 'Premium basmati rice',
 'GROCERY', 'ACTIVE',
 '{"brand": "FreshField", "type": "basmati", "origin": "India"}',
 'RICE-B-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(1008, 'MILK_001', 'Full Cream Milk', 'Fresh full cream milk',
 'GROCERY', 'ACTIVE',
 '{"brand": "FreshDaily", "type": "full_cream", "pasteurized": true}',
 'MILK-F-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Jewelry Products
(2001, 'TANQ_RING_001', 'Diamond Solitaire Ring', '18K Gold Ring with VS1 Diamond',
 'JEWELRY', 'ACTIVE',
 '{"brand": "Tanishq", "collection": "Solitaire", "occasion": "Engagement"}',
 'RING-D-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2002, 'TANQ_NECKLACE_001', 'Pearl String Necklace', 'South Sea Pearls with Gold Clasp',
 'JEWELRY', 'ACTIVE',
 '{"brand": "Tanishq", "collection": "Pearl", "occasion": "Wedding"}',
 'NECK-P-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2003, 'TANQ_BANGLE_001', 'Gold Kada', '22K Gold Traditional Bangle',
 'JEWELRY', 'ACTIVE',
 '{"brand": "Tanishq", "collection": "Traditional", "occasion": "Festival"}',
 'BANG-G-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Medicine Products
(3001, '1MG_PARA_001', 'Paracetamol 500mg', 'Fever and Pain Relief Tablet',
 'MEDICINE', 'ACTIVE',
 '{"brand": "Generic", "category": "Pain Relief", "prescription_required": true}',
 'MED-P-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(3002, '1MG_AMOX_001', 'Amoxicillin 250mg', 'Antibiotic Capsule',
 'MEDICINE', 'ACTIVE',
 '{"brand": "Generic", "category": "Antibiotics", "prescription_required": true}',
 'MED-A-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(3003, '1MG_VIT_001', 'Multivitamin Complex', 'Daily Vitamin Supplement',
 'MEDICINE', 'ACTIVE',
 '{"brand": "HealthVit", "category": "Supplements", "prescription_required": false}',
 'MED-V-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Product Features
INSERT INTO product_feature (
    id, product_id, template_id, code, name, description,
    feature_type, validation_pattern, min_value, max_value,
    allowed_values, metadata, required,
    created_date, last_modified_date,
    attribute_type, visible, editable, searchable,
    comparable, multi_valued, default_value,
    unit_id
)
VALUES 
-- Features for Formal Shirt (product_id: 1001)
(1, 1001, 100, 'SIZE_SHIRT', 'Size', 'Shirt size',
 'ENUM', '^(S|M|L|XL)$', '0', '100',
 '["S", "M", "L", "XL"]',
 '{"group": "basic", "tooltip": "Select your size", "display_order": 1}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'STRING', true, true, true, true, false, 'M',
 1),

(2, 1001, 101, 'STYLE_SHIRT', 'Style', 'Shirt style',
 'ENUM', '^(casual|formal)$', '0', '100',
 '["casual", "formal"]',
 '{"group": "basic", "tooltip": "Select style", "display_order": 2}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'STRING', true, true, true, true, false, 'formal',
 2),

-- Features for Smartphone (product_id: 1003)
(3, 1003, 102, 'STORAGE_PHONE', 'Storage', 'Phone storage capacity',
 'ENUM', '^(64GB|128GB|256GB)$', '0', '1000',
 '["64GB", "128GB", "256GB"]',
 '{"group": "specifications", "tooltip": "Select storage capacity", "display_order": 1}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'STRING', true, true, true, true, false, '128GB',
 7),

(4, 1003, 103, 'COLOR_PHONE', 'Color', 'Phone color',
 'ENUM', '^(Black|White|Gold)$', '0', '100',
 '["Black", "White", "Gold"]',
 '{"group": "appearance", "tooltip": "Select color", "display_order": 2}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'STRING', true, true, true, true, false, 'Black',
 2),

-- Features for AC (product_id: 1005)
(5, 1005, 104, 'CAPACITY_AC', 'Capacity', 'AC cooling capacity',
 'NUMERIC', '^[0-9]+(\.[0-9]{1,2})?$', '0.5', '2.0',
 NULL,
 '{"group": "specifications", "tooltip": "Enter capacity in tons", "display_order": 1}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'DECIMAL', true, true, true, true, false, '1.5',
 8),

(6, 1005, 105, 'ENERGY_RATING_AC', 'Energy Rating', 'AC energy efficiency rating',
 'ENUM', '^(3|4|5)$', '1', '5',
 '["3", "4", "5"]',
 '{"group": "efficiency", "tooltip": "Select energy rating", "display_order": 2}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'STRING', true, true, true, true, false, '5',
 2),

-- Features for Rice (product_id: 1007)
(7, 1007, 106, 'WEIGHT_RICE', 'Weight', 'Package weight',
 'NUMERIC', '^[0-9]+(\.[0-9]{1,2})?$', '0.1', '25.0',
 NULL,
 '{"group": "measurements", "tooltip": "Enter weight in kg", "display_order": 1}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'DECIMAL', true, true, true, true, false, '5.0',
 7),

-- Features for Diamond Ring (product_id: 2001)
(8, 2001, 201, 'GOLD_PURITY_RING', 'Gold Purity', 'Gold purity in Karats',
 'NUMERIC', '^(18|22|24)$', '18', '24', NULL,
 '{"group": "specifications", "tooltip": "Select gold purity in Karats", "display_order": 1}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'DECIMAL', true, true, true, true, false, '18',
 1),

(9, 2001, 202, 'DIAMOND_CERT_RING', 'Diamond Certification', 'Diamond certification type',
 'ENUM', '^(IGI|GIA|HRD)$', NULL, NULL, '["IGI", "GIA", "HRD"]',
 '{"group": "certification", "tooltip": "Select certification type", "display_order": 2}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'STRING', true, true, true, true, false, 'IGI',
 2),

-- Features for Paracetamol (product_id: 3001)
(10, 3001, 301, 'PRESCRIPTION_REQ_PARA', 'Prescription Required', 'Whether prescription is required',
 'BOOLEAN', NULL, NULL, NULL, NULL,
 '{"group": "regulations", "tooltip": "Is prescription required?", "display_order": 1}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'BOOLEAN', true, true, true, true, false, true,
 NULL),

(11, 3001, 302, 'SHELF_LIFE_PARA', 'Shelf Life', 'Product shelf life in months',
 'NUMERIC', '^[0-9]+$', '12', '36', NULL,
 '{"group": "storage", "tooltip": "Enter shelf life in months", "display_order": 2}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'INTEGER', true, true, true, true, false, '24',
 NULL);

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
-- Values for Formal Shirt (product_id: 1001)
(1, 1001, 1, 100, 
 'ENUM', 'SIZE', NULL, 'ACTIVE',
 'VALID', '^(S|M|L|XL)$', NULL,
 '{"value": "M"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

(2, 1001, 2, 101,
 'ENUM', 'STYLE', NULL, 'ACTIVE',
 'VALID', '^(casual|formal)$', NULL,
 '{"value": "formal"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

-- Values for Smartphone (product_id: 1003)
(3, 1003, 3, 102,
 'ENUM', 'STORAGE', 'GB', 'ACTIVE',
 'VALID', '^(64GB|128GB|256GB)$', NULL,
 '{"value": "128GB"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

(4, 1003, 4, 103,
 'ENUM', 'COLOR', NULL, 'ACTIVE',
 'VALID', '^(Black|White|Gold)$', NULL,
 '{"value": "Black"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

-- Values for AC (product_id: 1005)
(5, 1005, 5, 104,
 'NUMERIC', 'CAPACITY', 'tons', 'ACTIVE',
 'VALID', '^[0-9]+(\.[0-9]{1,2})?$', NULL,
 '{"value": 1.5}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

(6, 1005, 6, 105,
 'ENUM', 'ENERGY_RATING', 'stars', 'ACTIVE',
 'VALID', '^(3|4|5)$', NULL,
 '{"value": "5"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

-- Values for Rice (product_id: 1007)
(7, 1007, 7, 106,
 'NUMERIC', 'WEIGHT', 'kg', 'ACTIVE',
 'VALID', '^[0-9]+(\.[0-9]{1,2})?$', NULL,
 '{"value": 5.0}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

-- Values for Diamond Ring (product_id: 2001)
(8, 2001, 8, 201,
 'NUMERIC', 'GOLD_PURITY', NULL, 'ACTIVE',
 'VALID', '^(18|22|24)$', NULL,
 '{"value": 18.0}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

(9, 2001, 9, 202,
 'ENUM', 'DIAMOND_CERT', NULL, 'ACTIVE',
 'VALID', '^(IGI|GIA|HRD)$', NULL,
 '{"value": "IGI"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

-- Values for Paracetamol (product_id: 3001)
(10, 3001, 10, 301,
 'BOOLEAN', NULL, NULL, 'ACTIVE',
 'VALID', NULL, NULL,
 '{"value": true}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

(11, 3001, 11, 302,
 'NUMERIC', 'SHELF_LIFE', 'months', 'ACTIVE',
 'VALID', '^[0-9]+$', NULL,
 '{"value": 24}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP);

-- Product Categories
INSERT INTO product_categories (product_id, category_id)
VALUES 
(1001, 20), -- Formal Shirt in Western Wear
(1002, 20), -- T-Shirt in Western Wear
(1003, 60), -- Smartphone in Mobile Phones
(1004, 61), -- Laptop in Laptops
(1005, 10), -- AC in Split AC
(1006, 11), -- Fridge in Refrigerators
(1007, 33), -- Rice in Staples
(1008, 32), -- Milk in Dairy & Eggs
(2001, 51), -- Diamond Ring in Diamond Jewelry
(2002, 50), -- Pearl Necklace in Gold Jewelry
(2003, 50), -- Gold Kada in Gold Jewelry
(3001, 40), -- Paracetamol in Medicines
(3002, 40), -- Amoxicillin in Medicines
(3003, 41); -- Multivitamin in Health & Wellness
