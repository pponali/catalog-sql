-- Root Categories
INSERT INTO category (id, code, name, description, parent_id, dtype, created_date, last_modified_date)
VALUES 
-- Root category for appliances
(1, 'MSH1230', 'Home Appliances', 'Home and Kitchen Appliances', NULL, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Root category for apparel
(2, 'apparelCCWEE', 'Yeswee Apparel', 'Fashion and Clothing', NULL, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Root category for BigBasket
(3, 'BIGBASKET', 'BigBasket', 'Fresh Groceries and Daily Essentials', NULL, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Root category for 1mg
(4, '1MG', '1mg', 'Healthcare and Wellness', NULL, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Root category for Tanishq
(5, 'TANISHQ', 'Tanishq', 'Premium Jewelry', NULL, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Root category for Electronics
(6, 'ELECTRONICS', 'Electronics', 'Electronics and Gadgets', NULL, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Sub Categories
INSERT INTO category (id, code, name, description, parent_id, dtype, created_date, last_modified_date)
VALUES 
-- Air Conditioner category under Home Appliances
(10, 'MPH11111L4', 'Split AC NEW', 'Split Air Conditioners', 1, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- More specific categories under Yeswee Apparel
(11, 'MPWEAR001', 'Western Wear', 'Western Style Clothing', 2, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'MPWEAR002', 'Ethnic Wear', 'Traditional and Ethnic Clothing', 2, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- BigBasket subcategories
(20, 'BB_FRUITS', 'Fresh Fruits', 'Fresh and Seasonal Fruits', 3, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(21, 'BB_VEG', 'Fresh Vegetables', 'Fresh and Organic Vegetables', 3, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(22, 'BB_DAIRY', 'Dairy & Eggs', 'Fresh Dairy Products and Eggs', 3, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 1mg subcategories
(30, '1MG_MEDICINES', 'Medicines', 'Prescription and OTC Medicines', 4, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(31, '1MG_WELLNESS', 'Health & Wellness', 'Wellness and Nutrition Products', 4, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(32, '1MG_PERSONAL', 'Personal Care', 'Personal Care Products', 4, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Tanishq subcategories
(40, 'TANQ_GOLD', 'Gold Jewelry', '22K Gold Jewelry Collection', 5, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(41, 'TANQ_DIAMOND', 'Diamond Jewelry', 'Premium Diamond Collection', 5, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(42, 'TANQ_WEDDING', 'Wedding Collection', 'Bridal Jewelry Collection', 5, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Classification Classes (extending Category)
INSERT INTO category (id, code, name, description, parent_id, dtype, created_date, last_modified_date)
VALUES 
(100, 'apparelCCWEE', 'Yeswee', 'Apparel Classification Class', 2, 'ClassificationClass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(101, 'ACCC001', 'AC Features', 'Air Conditioner Features', 10, 'ClassificationClass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(102, 'BB_ORGANIC', 'Organic Products', 'Organic Certification Class', 3, 'ClassificationClass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(103, 'BB_FRESH', 'Fresh Products', 'Fresh Product Features', 3, 'ClassificationClass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(104, '1MG_PHARMA', 'Pharmaceutical', 'Medicine Classifications', 4, 'ClassificationClass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(105, '1MG_AYUR', 'Ayurvedic', 'Ayurvedic Medicine Class', 4, 'ClassificationClass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(106, 'TANQ_PRECIOUS', 'Precious Metals', 'Precious Metals Class', 5, 'ClassificationClass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(107, 'TANQ_GEMS', 'Gemstones', 'Gemstones Classification', 5, 'ClassificationClass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Classification Classes
INSERT INTO classification_class (id, allow_multiple_categories, inherit_features, active, sequence)
VALUES 
(100, true, true, true, 1),
(101, true, true, true, 2),
(102, true, true, true, 3),
(103, true, true, true, 4),
(104, true, true, true, 5),
(105, true, true, true, 6),
(106, true, true, true, 7),
(107, true, true, true, 8);

-- Classification Class Metadata
INSERT INTO classification_class_metadata (class_id, key, value)
VALUES 
(100, 'department', 'fashion'),
(100, 'target_gender', 'unisex'),
(101, 'appliance_type', 'cooling'),
(102, 'certification_type', 'organic'),
(102, 'quality_check', 'required'),
(103, 'storage_type', 'refrigerated'),
(104, 'prescription_required', 'true'),
(104, 'storage_condition', 'temperature_controlled'),
(106, 'certification_required', 'hallmark'),
(106, 'value_addition', 'making_charges');

-- Classification Attributes
INSERT INTO classification_attribute (id, code, name, description, attribute_type, validation_pattern, min_value, max_value, unit, visible, editable, searchable, comparable, mandatory, multi_valued, metadata, created_date, last_modified_date)
VALUES 
(1, 'unisexapparelwee', 'Stylewee', 'Style category for unisex apparel', 'ENUM', NULL, NULL, NULL, NULL, true, true, true, true, false, false, '{"validation": {"enum_values": ["casual", "formal", "sports"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'sizewee', 'Size', 'Size specification', 'ENUM', NULL, NULL, NULL, NULL, true, true, true, true, true, false, '{"validation": {"enum_values": ["XS", "S", "M", "L", "XL"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'cooling_capacity', 'Cooling Capacity', 'Cooling capacity in tons', 'NUMERIC', NULL, 0.8, 2.0, 'ton', true, true, true, true, true, false, '{"validation": {"min": 0.8, "max": 2.0}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'energy_rating', 'Energy Rating', 'Energy efficiency rating', 'ENUM', NULL, NULL, NULL, NULL, true, true, true, true, true, false, '{"validation": {"enum_values": ["3_star", "4_star", "5_star"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'organic_cert', 'Organic Certification', 'Organic certification details', 'STRING', '^[A-Z0-9-]+$', NULL, NULL, NULL, true, true, true, false, true, false, '{"validation": {"pattern": "^[A-Z0-9-]+$"}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'nutritional_info', 'Nutritional Info', 'Product nutrition facts', 'JSON', NULL, NULL, NULL, NULL, true, true, false, false, true, false, '{"validation": {"required_fields": ["calories", "protein", "carbs", "fat"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'shelf_life', 'Shelf Life', 'Product shelf life', 'NUMERIC', NULL, 1, 365, 'days', true, true, true, true, true, false, '{"validation": {"min": 1, "max": 365}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'medicine_type', 'Medicine Type', 'Type of medicine', 'ENUM', NULL, NULL, NULL, NULL, true, true, true, true, true, false, '{"validation": {"enum_values": ["allopathy", "ayurvedic", "homeopathy"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'composition', 'Composition', 'Medicine composition', 'JSON', NULL, NULL, NULL, NULL, true, true, false, false, true, false, '{"validation": {"required_fields": ["active_ingredients", "strength"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'dosage_form', 'Dosage Form', 'Form of medicine', 'ENUM', NULL, NULL, NULL, NULL, true, true, true, true, true, false, '{"validation": {"enum_values": ["tablet", "capsule", "syrup", "injection"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'metal_purity', 'Metal Purity', 'Purity of metal', 'STRING', '^[0-9]{2,3}K$', NULL, NULL, NULL, true, true, true, true, true, false, '{"validation": {"pattern": "^[0-9]{2,3}K$"}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'stone_details', 'Stone Details', 'Precious stone details', 'JSON', NULL, NULL, NULL, NULL, true, true, false, false, true, false, '{"validation": {"required_fields": ["stone_type", "carat", "clarity"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 'certification', 'Certification', 'Jewelry certification', 'STRING', '^[A-Z]{2,4}-[0-9]{6}$', NULL, NULL, NULL, true, true, true, false, true, false, '{"validation": {"pattern": "^[A-Z]{2,4}-[0-9]{6}$"}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Class Attribute Assignments
INSERT INTO class_attribute_assignment (id, classification_class_id, classification_attribute_id, mandatory, visible, searchable, created_date, last_modified_date)
VALUES 
(1, 100, 1, false, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 100, 2, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 101, 3, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 101, 4, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 102, 5, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 102, 6, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 103, 7, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 104, 8, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 104, 9, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 105, 10, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 106, 11, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 106, 12, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 107, 13, true, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Enum Values
INSERT INTO enum_value (id, code, sort_order, created_date, last_modified_date)
VALUES 
(1, 'caseincludedeyewearyes', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'caseincludedeyewearno', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'size_s', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'size_m', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'size_l', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'rating_3star', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'rating_5star', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Enum Value Translations
INSERT INTO enum_value_translation (id, enum_value_id, language_code, value, created_date, last_modified_date)
VALUES 
(1, 1, 'en', 'Yes', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 'en', 'No', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, 'en', 'Small', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 4, 'en', 'Medium', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 5, 'en', 'Large', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 6, 'en', '3 Star', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 7, 'en', '5 Star', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Classification Attribute Values
INSERT INTO classification_attribute_value (id, assignment_id, value, created_date, last_modified_date)
VALUES 
(1, 1, '{"value": "Yes", "colorHexCode": "09090_#"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, '{"value": "No", "colorHexCode": "08080_#"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 2, '{"value": "S", "measurements": {"chest": "36", "length": "28"}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 2, '{"value": "M", "measurements": {"chest": "38", "length": "29"}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 3, '{"value": "1.5", "unit": "ton"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 4, '{"value": "5 Star", "efficiency": "high"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Category Feature Templates
INSERT INTO category_feature_template (
    id, category_id, code, name, description, 
    feature_type, attribute_type, validation_pattern, min_value, max_value,
    allowed_values, unit, visible, editable, searchable,
    comparable, mandatory, multi_valued, metadata,
    display_order, is_required, created_date, last_modified_date
)
VALUES 
(1, 1, 'STYLE', 'Style', 'Clothing style', 
    'string', 'ENUM', NULL, NULL, NULL,
    '["casual", "formal", "sports"]', NULL, true, true, true,
    true, true, false, '{"validation": {"enum_values": ["casual", "formal", "sports"]}}',
    1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
(2, 1, 'SIZE', 'Size', 'Clothing size',
    'string', 'ENUM', NULL, NULL, NULL,
    '["XS", "S", "M", "L", "XL"]', NULL, true, true, true,
    true, true, false, '{"validation": {"enum_values": ["XS", "S", "M", "L", "XL"]}}',
    2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
(3, 2, 'WEIGHT', 'Weight', 'Product weight',
    'number', 'NUMERIC', NULL, 0.1, 100.0,
    NULL, 'ton', true, true, true,
    true, true, false, '{"validation": {"min": 0.1, "max": 100.0}}',
    1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
(4, 2, 'RATING', 'Rating', 'Product rating',
    'string', 'ENUM', NULL, NULL, NULL,
    '["3_star", "4_star", "5_star"]', NULL, true, true, true,
    true, false, false, '{"validation": {"enum_values": ["3_star", "4_star", "5_star"]}}',
    2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
(5, 3, 'CERTIFICATION', 'Certification', 'Organic certification',
    'string', 'STRING', '^[A-Z0-9-]+$', NULL, NULL,
    NULL, NULL, true, true, true,
    false, true, false, '{"validation": {"pattern": "^[A-Z0-9-]+$"}}',
    1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
(6, 3, 'NUTRITION', 'Nutrition', 'Nutritional information',
    'json', 'JSON', NULL, NULL, NULL,
    NULL, NULL, true, true, false,
    false, true, false, '{"validation": {"required_fields": ["calories", "protein", "carbs", "fat"]}}',
    2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
(7, 4, 'SHELF_LIFE', 'Shelf Life', 'Product shelf life',
    'number', 'NUMERIC', NULL, 1, 365,
    NULL, 'days', true, true, true,
    true, true, false, '{"validation": {"min": 1, "max": 365}}',
    1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
(8, 5, 'MEDICINE_TYPE', 'Medicine Type', 'Type of medicine',
    'string', 'ENUM', NULL, NULL, NULL,
    '["allopathy", "ayurvedic", "homeopathy"]', NULL, true, true, true,
    true, true, false, '{"validation": {"enum_values": ["allopathy", "ayurvedic", "homeopathy"]}}',
    1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
(9, 5, 'COMPOSITION', 'Composition', 'Medicine composition',
    'json', 'JSON', NULL, NULL, NULL,
    NULL, NULL, true, true, false,
    false, true, false, '{"validation": {"required_fields": ["active_ingredients", "strength"]}}',
    2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
(10, 5, 'FORM', 'Form', 'Medicine form',
    'string', 'ENUM', NULL, NULL, NULL,
    '["tablet", "capsule", "syrup", "injection"]', NULL, true, true, true,
    true, true, false, '{"validation": {"enum_values": ["tablet", "capsule", "syrup", "injection"]}}',
    3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Products
INSERT INTO product (id, code, name, description, product_type, status, metadata, sku, created_date, last_modified_date)
VALUES 
(1003, 'SHIRT_001', 'Men''s Formal Shirt', 'Classic formal shirt for men', 'APPAREL', 'ACTIVE', '{"brand": "ABC", "color": "white"}', 'SKU001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1004, 'TSHIRT_001', 'Men''s Sports T-Shirt', 'Comfortable sports t-shirt', 'APPAREL', 'ACTIVE', '{"brand": "XYZ", "color": "blue"}', 'SKU002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1005, 'FOOD_001', 'Organic Quinoa', 'Premium organic quinoa', 'FOOD', 'ACTIVE', '{"origin": "Peru", "packaging": "1kg"}', 'SKU003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1006, 'FOOD_002', 'Organic Chia Seeds', 'Premium organic chia seeds', 'FOOD', 'ACTIVE', '{"origin": "Mexico", "packaging": "500g"}', 'SKU004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1007, 'MED_001', 'Paracetamol', 'Pain relief medication', 'MEDICINE', 'ACTIVE', '{"manufacturer": "PharmaX", "dosage": "500mg"}', 'SKU005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Product Categories
INSERT INTO product_categories (product_id, category_id)
VALUES 
(1003, 11), -- Shirt in Apparel
(1004, 11), -- T-Shirt in Apparel
(1005, 20), -- Apples in Food & Beverages
(1006, 22), -- Milk in Dairy
(1007, 30); -- Medicine in Healthcare

-- Product Features
INSERT INTO product_feature (id, product_id, template_id, code, name, description, feature_type, validation_pattern, min_value, max_value, allowed_values, metadata, required, created_date, last_modified_date)
VALUES 
(1, 1003, 1, 'STYLE_SHIRT', 'Style', 'Shirt style', 'ENUM', NULL, NULL, NULL, '["casual", "formal"]', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1003, 2, 'SIZE_SHIRT', 'Size', 'Shirt size', 'ENUM', NULL, NULL, NULL, '["S", "M", "L", "XL"]', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 1004, 3, 'STYLE_TSHIRT', 'Style', 'T-Shirt style', 'ENUM', NULL, NULL, NULL, '["sports", "casual"]', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 1004, 4, 'SIZE_TSHIRT', 'Size', 'T-Shirt size', 'ENUM', NULL, NULL, NULL, '["S", "M", "L", "XL"]', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 1005, 5, 'ORGANIC_CERT', 'Organic Certification', 'Organic certification details', 'STRING', NULL, NULL, NULL, NULL, NULL, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 1005, 6, 'NUTRITION', 'Nutrition Facts', 'Nutritional information', 'JSON', NULL, NULL, NULL, NULL, NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 1006, 7, 'SHELF_LIFE', 'Shelf Life', 'Product shelf life', 'NUMBER', NULL, '1', '30', NULL, NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 1007, 8, 'MED_TYPE', 'Medicine Type', 'Type of medicine', 'ENUM', NULL, NULL, NULL, '["allopathy", "ayurvedic", "homeopathy"]', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 1007, 9, 'COMPOSITION', 'Composition', 'Medicine composition', 'JSON', NULL, NULL, NULL, NULL, NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 1007, 10, 'FORM', 'Form', 'Medicine form', 'ENUM', NULL, NULL, NULL, '["tablet", "capsule", "syrup", "injection"]', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Product Feature Values
INSERT INTO product_feature_value (id, product_id, feature_id, template_id, type, unit, unit_of_measure, status, validation_status, validation_pattern, validation_message, attribute_values, created_at, updated_at, created_by, updated_by)
VALUES 
(1, 1003, 1, 1, 'string', NULL, NULL, 'active', 'valid', NULL, NULL, '{"value": "formal"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(2, 1003, 2, 2, 'string', NULL, NULL, 'active', 'valid', NULL, NULL, '{"value": "M"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(3, 1004, 3, 3, 'number', 'ton', NULL, 'active', 'valid', NULL, NULL, '{"value": 1.5}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(4, 1004, 4, 4, 'string', NULL, NULL, 'active', 'valid', NULL, NULL, '{"value": "5_star"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(5, 1005, 5, 5, 'string', NULL, NULL, 'active', 'valid', NULL, NULL, '{"value": "USDA-ORG-001"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(6, 1005, 6, 6, 'json', NULL, NULL, 'active', 'valid', NULL, NULL, '{"calories": 52, "protein": 0.3, "carbs": 14, "fat": 0.2}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(7, 1006, 7, 7, 'number', 'days', NULL, 'active', 'valid', NULL, NULL, '{"value": 7}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(8, 1007, 8, 8, 'string', NULL, NULL, 'active', 'valid', NULL, NULL, '{"value": "allopathy"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(9, 1007, 9, 9, 'json', NULL, NULL, 'active', 'valid', NULL, NULL, '{"active_ingredients": ["paracetamol"], "strength": "500mg"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
(10, 1007, 10, 10, 'string', NULL, NULL, 'active', 'valid', NULL, NULL, '{"value": "tablet"}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');
