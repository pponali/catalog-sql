-- Combined sample data from all JSON files

-- Categories from categories.json
INSERT INTO category (id, code, name, description, parent_id, type, metadata, created_date, last_modified_date)
VALUES 
-- Root Categories
(1000, 'JEWELRY', 'Jewelry', 'Fine jewelry and accessories', NULL, 'STANDARD', 
 '{"material": "precious_metals", "style": "modern"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1001, 'ELECTRONICS', 'Electronics', 'Electronic devices and accessories', NULL, 'STANDARD',
 '{"voltage": "220V", "warranty": "standard"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1002, 'FASHION', 'Fashion', 'Clothing and accessories', NULL, 'STANDARD',
 '{"season": "all", "department": "apparel"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Sub Categories
(1003, 'RINGS', 'Rings', 'All types of rings', 1000, 'STANDARD',
 '{"size_range": "5-12", "gender": "unisex"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1004, 'SMARTPHONES', 'Smartphones', 'Mobile phones and accessories', 1001, 'STANDARD',
 '{"network": "5G", "screen_size": "6-7inch"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1005, 'MENS_CLOTHING', 'Men''s Clothing', 'Men''s apparel', 1002, 'STANDARD',
 '{"size_type": "US", "fit": "regular"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1006, 'LAPTOPS', 'Laptops', 'Portable computers', 1001, 'STANDARD',
 '{"usage": "professional", "form_factor": "notebook"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Classification Classes from classification_classes.json
INSERT INTO classification_class (id, code, name, description, category_id, metadata, created_date, last_modified_date)
VALUES 
(2000, 'GOLD_JEWELRY', 'Gold Jewelry', 'Gold jewelry items', 1000,
 '{"purity": "24K", "certification": "BIS"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2001, 'PREMIUM_SMARTPHONES', 'Premium Smartphones', 'High-end mobile devices', 1004,
 '{"price_range": "premium", "processor": "flagship"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2002, 'GAMING_LAPTOPS', 'Gaming Laptops', 'High-performance gaming computers', 1006,
 '{"gpu": "dedicated", "cooling": "advanced"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2003, 'BUSINESS_LAPTOPS', 'Business Laptops', 'Professional work laptops', 1006,
 '{"security": "enterprise", "battery": "long-lasting"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2004, 'CASUAL_WEAR', 'Casual Wear', 'Casual clothing items', 1005,
 '{"style": "casual", "occasion": "everyday"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2005, 'FORMAL_WEAR', 'Formal Wear', 'Business and formal clothing', 1005,
 '{"style": "formal", "occasion": "business"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Classification Attributes from classification_attributes.json
INSERT INTO classification_attribute (id, code, name, description, attribute_type, metadata, created_date, last_modified_date)
VALUES 
(3000, 'PURITY', 'Gold Purity', 'Purity level of gold', 'string',
 '{"validValues": ["18K", "22K", "24K"], "required": true}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3001, 'PROCESSOR_TYPE', 'Processor Type', 'Type of CPU', 'string',
 '{"validValues": ["Snapdragon", "MediaTek", "Apple", "Exynos"], "required": true}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3002, 'RAM', 'RAM Memory', 'RAM capacity in GB', 'number',
 '{"minValue": 8, "maxValue": 64, "required": true}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3003, 'BATTERY_LIFE', 'Battery Life', 'Battery life in hours', 'number',
 '{"minValue": 4, "maxValue": 24, "required": true}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3004, 'SIZE', 'Clothing Size', 'Size of the clothing item', 'string',
 '{"validValues": ["XS", "S", "M", "L", "XL", "XXL"], "required": true}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3005, 'MATERIAL', 'Fabric Material', 'Material of the clothing', 'string',
 '{"validValues": ["Cotton", "Wool", "Polyester", "Linen", "Silk"], "required": true}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3006, 'SCREEN_SIZE', 'Screen Size', 'Display size in inches', 'number',
 '{"minValue": 5.5, "maxValue": 7.5, "required": true}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Products from products.json
INSERT INTO product (id, code, name, description, product_type, status, metadata, sku, created_date, last_modified_date)
VALUES 
-- Jewelry Products
(4000, 'GR001', 'Classic Gold Ring', '22K gold ring with traditional design', 'JEWELRY', 'ACTIVE',
 '{"categoryCode": "RINGS", "classificationCode": "GOLD_JEWELRY"}', 'RING-G-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Smartphone Products
(4001, 'SP001', 'Galaxy Ultra Pro', 'Premium Android smartphone with advanced features', 'ELECTRONICS', 'ACTIVE',
 '{"categoryCode": "SMARTPHONES", "classificationCode": "PREMIUM_SMARTPHONES"}', 'PHONE-S-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Laptop Products
(4002, 'GL001', 'ROG Strix Gaming Laptop', 'High-performance gaming laptop with RGB', 'ELECTRONICS', 'ACTIVE',
 '{"categoryCode": "LAPTOPS", "classificationCode": "GAMING_LAPTOPS"}', 'LAP-G-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4003, 'BL001', 'ThinkPad X1 Carbon', 'Professional business laptop', 'ELECTRONICS', 'ACTIVE',
 '{"categoryCode": "LAPTOPS", "classificationCode": "BUSINESS_LAPTOPS"}', 'LAP-B-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Clothing Products
(4004, 'CW001', 'Classic Polo Shirt', 'Comfortable cotton polo for casual wear', 'APPAREL', 'ACTIVE',
 '{"categoryCode": "MENS_CLOTHING", "classificationCode": "CASUAL_WEAR"}', 'SHIRT-C-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4005, 'FW001', 'Executive Suit', 'Premium wool business suit', 'APPAREL', 'ACTIVE',
 '{"categoryCode": "MENS_CLOTHING", "classificationCode": "FORMAL_WEAR"}', 'SUIT-F-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Medicine data from medicine_data.json
INSERT INTO category (id, code, name, description, parent_id, type, created_date, last_modified_date)
VALUES 
(5000, 'PRESCRIPTION_MEDS', 'Prescription Medicines', 'Medicines requiring prescription', 4, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5001, 'OTC_MEDS', 'Over The Counter Medicines', 'Medicines available without prescription', 4, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO classification_class (id, code, name, description, category_id, created_date, last_modified_date)
VALUES 
(6000, 'ANTIBIOTICS', 'Antibiotics', 'Antibiotic medications', 5000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6001, 'PAIN_RELIEF', 'Pain Relief', 'Pain relief medications', 5001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO product (id, code, name, description, product_type, status, metadata, sku, created_date, last_modified_date)
VALUES 
(7000, 'MED001', 'Amoxicillin 500mg Capsules', 'Antibiotic medication', 'MEDICINE', 'ACTIVE',
 '{"composition": "Amoxicillin Trihydrate", "dosage": "Capsule", "strength": "500mg"}', 'MED-A-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7001, 'MED002', 'Paracetamol 650mg Tablets', 'Pain relief medication', 'MEDICINE', 'ACTIVE',
 '{"composition": "Paracetamol", "dosage": "Tablet", "strength": "650mg"}', 'MED-P-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Jewelry data from jewelry_data.json
INSERT INTO category (id, code, name, description, parent_id, type, created_date, last_modified_date)
VALUES 
(8000, 'JEWELRY_RINGS', 'Rings', 'Fine jewelry rings', 5, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8001, 'JEWELRY_NECKLACES', 'Necklaces', 'Fine jewelry necklaces', 5, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO classification_class (id, code, name, description, category_id, created_date, last_modified_date)
VALUES 
(9000, 'DIAMOND_RINGS', 'Diamond Rings', 'Diamond studded rings', 8000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9001, 'GOLD_CHAINS', 'Gold Chains', 'Gold necklaces and chains', 8001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO product (id, code, name, description, product_type, status, metadata, sku, created_date, last_modified_date)
VALUES 
(10000, 'RING001', 'Classic Diamond Solitaire Ring', '18K White Gold Diamond Ring', 'JEWELRY', 'ACTIVE',
 '{"metal_type": "18K White Gold", "purity": "18K", "weight": "4.5", "diamond_clarity": "VS1"}', 'RING-D-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10001, 'NECKLACE001', 'Traditional Gold Chain', '22K Yellow Gold Chain', 'JEWELRY', 'ACTIVE',
 '{"metal_type": "22K Yellow Gold", "purity": "22K", "weight": "15.7"}', 'NECK-G-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Product Feature Values
INSERT INTO product_feature_value (id, product_id, feature_id, string_value, numeric_value, boolean_value,
    attribute_value, validation_status, validation_pattern, validation_message, metadata,
    created_by, created_date, last_modified_by, last_modified_date)
VALUES 
-- Gold Ring Features
(11000, 4000, 3000, '22K', NULL, NULL,
 '{"purity": "22K"}', 'VALID', '^(18K|22K|24K)$', NULL, NULL,
 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP),

-- Smartphone Features
(11001, 4001, 3001, 'Snapdragon', NULL, NULL,
 '{"processor": "Snapdragon 8 Gen 2"}', 'VALID', NULL, NULL, NULL,
 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP),
(11002, 4001, 3006, NULL, 6.8, NULL,
 '{"screen": "6.8 inch AMOLED"}', 'VALID', NULL, NULL, NULL,
 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP),

-- Gaming Laptop Features
(11003, 4002, 3002, NULL, 32, NULL,
 '{"ram": "32GB DDR5"}', 'VALID', NULL, NULL, NULL,
 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP),

-- Business Laptop Features
(11004, 4003, 3003, NULL, 18, NULL,
 '{"battery": "18 hours"}', 'VALID', NULL, NULL, NULL,
 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP),

-- Clothing Features
(11005, 4004, 3004, 'M', NULL, NULL,
 '{"size": "M"}', 'VALID', '^(XS|S|M|L|XL|XXL)$', NULL, NULL,
 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP),
(11006, 4005, 3005, 'Wool', NULL, NULL,
 '{"material": "Premium Wool"}', 'VALID', NULL, NULL, NULL,
 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP);

-- Medicine Feature Values
INSERT INTO product_feature_value (id, product_id, feature_id, string_value, numeric_value, boolean_value,
    attribute_value, validation_status, validation_pattern, validation_message, metadata,
    created_by, created_date, last_modified_by, last_modified_date)
VALUES 
-- Amoxicillin Features
(12000, 7000, NULL, NULL, NULL, true,
 '{"composition": "Amoxicillin Trihydrate", "strength": "500mg", "dosage": "1 capsule thrice daily"}',
 'VALID', NULL, NULL, '{"prescription_required": true}',
 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP),

-- Paracetamol Features
(12001, 7001, NULL, NULL, NULL, false,
 '{"composition": "Paracetamol", "strength": "650mg", "dosage": "1 tablet as needed"}',
 'VALID', NULL, NULL, '{"prescription_required": false}',
 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP);

-- Jewelry Feature Values
INSERT INTO product_feature_value (id, product_id, feature_id, string_value, numeric_value, boolean_value,
    attribute_value, validation_status, validation_pattern, validation_message, metadata,
    created_by, created_date, last_modified_by, last_modified_date)
VALUES 
-- Diamond Ring Features
(13000, 10000, NULL, '18K', 4.5, NULL,
 '{"metal_type": "18K White Gold", "diamond_clarity": "VS1"}',
 'VALID', NULL, NULL, '{"certification": "IGI"}',
 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP),

-- Gold Chain Features
(13001, 10001, NULL, '22K', 15.7, NULL,
 '{"metal_type": "22K Yellow Gold"}',
 'VALID', NULL, NULL, NULL,
 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP);
