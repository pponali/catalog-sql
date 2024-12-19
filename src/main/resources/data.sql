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
(5, 'TANISHQ', 'Tanishq', 'Premium Jewelry', NULL, 'Category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

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

-- Classification Class specific attributes
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
INSERT INTO category_feature_template (id, category_id, code, name, description, attribute_type, validation_pattern, min_value, max_value, unit, visible, editable, searchable, comparable, mandatory, multi_valued, metadata, created_date, last_modified_date)
VALUES 
(1, 11, 'style', 'Style', 'Apparel style', 'ENUM', NULL, NULL, NULL, NULL, true, true, true, true, false, false, '{"validation": {"enum_values": ["casual", "formal", "sports"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 11, 'size', 'Size', 'Apparel size', 'ENUM', NULL, NULL, NULL, NULL, true, true, true, true, true, false, '{"validation": {"enum_values": ["XS", "S", "M", "L", "XL"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 10, 'cooling_capacity', 'Cooling Capacity', 'AC cooling capacity', 'NUMERIC', NULL, 0.8, 2.0, 'ton', true, true, true, true, true, false, '{"validation": {"min": 0.8, "max": 2.0}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 10, 'energy_rating', 'Energy Rating', 'Energy efficiency', 'ENUM', NULL, NULL, NULL, NULL, true, true, true, true, true, false, '{"validation": {"enum_values": ["3_star", "4_star", "5_star"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 20, 'organic_cert', 'Organic Certification', 'Organic certification', 'STRING', '^[A-Z0-9-]+$', NULL, NULL, NULL, true, true, true, false, true, false, '{"validation": {"pattern": "^[A-Z0-9-]+$"}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 20, 'nutritional_info', 'Nutritional Info', 'Nutrition facts', 'JSON', NULL, NULL, NULL, NULL, true, true, false, false, true, false, '{"validation": {"required_fields": ["calories", "protein", "carbs", "fat"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 22, 'shelf_life', 'Shelf Life', 'Product shelf life', 'NUMERIC', NULL, 1, 365, 'days', true, true, true, true, true, false, '{"validation": {"min": 1, "max": 365}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 30, 'medicine_type', 'Medicine Type', 'Type of medicine', 'ENUM', NULL, NULL, NULL, NULL, true, true, true, true, true, false, '{"validation": {"enum_values": ["allopathy", "ayurvedic", "homeopathy"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 30, 'composition', 'Composition', 'Medicine composition', 'JSON', NULL, NULL, NULL, NULL, true, true, false, false, true, false, '{"validation": {"required_fields": ["active_ingredients", "strength"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 31, 'dosage_form', 'Dosage Form', 'Medicine form', 'ENUM', NULL, NULL, NULL, NULL, true, true, true, true, true, false, '{"validation": {"enum_values": ["tablet", "capsule", "syrup", "injection"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 41, 'metal_purity', 'Metal Purity', 'Gold purity', 'STRING', '^[0-9]{2,3}K$', NULL, NULL, NULL, true, true, true, true, true, false, '{"validation": {"pattern": "^[0-9]{2,3}K$"}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 41, 'stone_details', 'Stone Details', 'Diamond details', 'JSON', NULL, NULL, NULL, NULL, true, true, false, false, true, false, '{"validation": {"required_fields": ["stone_type", "carat", "clarity"]}}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Sample Products
INSERT INTO product (id, sku, name, description, created_date, last_modified_date)
VALUES 
(1001, 'AC15TON001', 'Premium 1.5 Ton Split AC', '5 Star energy efficient split AC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1002, 'AC2TON002', '2 Ton Window AC', '3 Star energy rated window AC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1003, 'SHIRT001', 'Cotton Formal Shirt', 'White cotton formal shirt', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1004, 'TSHIRT001', 'Sports T-Shirt', 'Moisture-wicking sports t-shirt', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1005, 'BB_APPLE001', 'Organic Royal Gala Apples', 'Premium imported organic apples', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1006, 'BB_MILK001', 'Organic Fresh Milk', 'Farm fresh organic milk', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1007, '1MG_MED001', 'Crocin Advance', 'Paracetamol 500mg tablets', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Product Categories
INSERT INTO product_categories (product_id, category_id)
VALUES 
(1001, 10), -- AC in Electronics
(1002, 10), -- AC in Electronics
(1003, 11), -- Shirt in Apparel
(1004, 11), -- T-Shirt in Apparel
(1005, 20), -- Apples in Food & Beverages
(1006, 22), -- Milk in Dairy
(1007, 30); -- Medicine in Healthcare

-- Product Features
INSERT INTO product_feature (id, product_id, template_id, created_date, last_modified_date)
VALUES 
(1, 1001, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1001, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 1002, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 1002, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 1003, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 1003, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 1004, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 1004, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 1005, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 1005, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 1006, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 1007, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 1007, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Product Feature Values
INSERT INTO product_feature_value (id, feature_id, string_value, numeric_value, boolean_value, attribute_value, unit, created_date, last_modified_date)
VALUES 
(1, 1, NULL, 1.5, NULL, NULL, 'ton', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, NULL, NULL, NULL, '{"value": "5_star", "efficiency": "high"}', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, NULL, 2.0, NULL, NULL, 'ton', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 4, NULL, NULL, NULL, '{"value": "3_star", "efficiency": "medium"}', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 5, 'USDA-ORG-001', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 6, NULL, NULL, NULL, '{"calories": 52, "protein": 0.3, "carbs": 14, "fat": 0.2}', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 7, NULL, 7, NULL, NULL, 'days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 8, NULL, NULL, NULL, '{"value": "allopathy"}', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 9, NULL, NULL, NULL, '{"active_ingredients": ["paracetamol"], "strength": "500mg"}', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 10, NULL, NULL, NULL, '{"value": "tablet"}', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 11, '22K', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 12, NULL, NULL, NULL, '{"stone_type": "diamond", "carat": 1.2, "clarity": "VS1"}', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 13, NULL, NULL, NULL, '{"value": "allopathy"}', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
