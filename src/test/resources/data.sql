-- Insert test categories
INSERT INTO category (code, name, description, parent_id, created_date, last_modified_date)
VALUES 
    ('MSH1230', 'Home Appliances', 'Home and Kitchen Appliances', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('apparelCCWEE', 'Yeswee Apparel', 'Fashion and Clothing', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('BIGBASKET', 'BigBasket', 'Fresh Groceries and Daily Essentials', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('1MG', '1mg', 'Healthcare and Wellness', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('TANISHQ', 'Tanishq', 'Premium Jewelry', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert test classification classes
INSERT INTO classification_class (code, name, description, category_id, created_date, last_modified_date)
VALUES 
    ('ELECTRONICS', 'Electronics', 'Electronic Devices', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('CLOTHING', 'Clothing', 'Fashion Items', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('GROCERY', 'Grocery', 'Food Items', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('HEALTHCARE', 'Healthcare', 'Medical Items', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('JEWELRY', 'Jewelry', 'Jewelry Items', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert test classification attributes
INSERT INTO classification_attribute (code, name, description, attribute_type, validation_pattern, min_value, max_value, unit, created_date, last_modified_date)
VALUES 
    ('VOLTAGE', 'Voltage', 'Operating Voltage', 'NUMERIC', NULL, 100, 240, 'V', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('SIZE', 'Size', 'Product Size', 'STRING', '^(XS|S|M|L|XL|XXL)$', NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('WEIGHT', 'Weight', 'Product Weight', 'NUMERIC', NULL, 0, 1000, 'g', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('DOSAGE', 'Dosage', 'Medicine Dosage', 'STRING', NULL, NULL, NULL, 'mg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('PURITY', 'Purity', 'Gold Purity', 'NUMERIC', NULL, 14, 24, 'K', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert test class attribute assignments
INSERT INTO class_attribute_assignment (classification_class_id, classification_attribute_id, created_date, last_modified_date)
VALUES 
    (1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert test category feature templates
INSERT INTO category_feature_template (code, name, description, attribute_type, validation_pattern, min_value, max_value, unit, created_date, last_modified_date)
VALUES 
    ('POWER', 'Power Rating', 'Power Consumption', 'NUMERIC', NULL, 0, 2000, 'W', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('COLOR', 'Color', 'Product Color', 'STRING', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('EXPIRY', 'Expiry Date', 'Product Expiry', 'STRING', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('BRAND', 'Brand', 'Product Brand', 'STRING', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MATERIAL', 'Material', 'Product Material', 'STRING', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert test products
INSERT INTO product (code, name, description, category_id, created_date, last_modified_date)
VALUES 
    ('TV123', 'Smart TV', '55-inch Smart TV', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('SHIRT456', 'Cotton Shirt', 'Casual Cotton Shirt', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('RICE789', 'Basmati Rice', 'Premium Basmati Rice', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MED101', 'Paracetamol', 'Pain Relief Medicine', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('RING202', 'Gold Ring', '22K Gold Ring', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert test product features
INSERT INTO product_feature (code, name, description, product_id, template_id, created_date, last_modified_date)
VALUES 
    ('TV_POWER', 'TV Power', 'TV Power Rating', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('SHIRT_COLOR', 'Shirt Color', 'Shirt Color', 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('RICE_EXPIRY', 'Rice Expiry', 'Rice Expiry Date', 3, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('MED_BRAND', 'Medicine Brand', 'Medicine Brand Name', 4, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('RING_MATERIAL', 'Ring Material', 'Ring Material Type', 5, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Product Feature Values
INSERT INTO product_feature_value (
    id, product_id, feature_id, template_id,
    type, unit_id, unit_of_measure, status,
    validation_status, validation_pattern,
    validation_message, attribute_values,
    created_by, created_date,
    last_modified_by, last_modified_date
)
VALUES 
(101, 1, 1, 1, 
 'ENUM', 1, 'SIZE', 'ACTIVE',
 'VALID', '^(S|M|L|XL)$', NULL,
 '{"value": "M"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

(102, 1, 2, 2,
 'ENUM', 2, 'STYLE', 'ACTIVE',
 'VALID', '^(casual|formal)$', NULL,
 '{"value": "formal"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

(103, 2, 3, 3,
 'NUMERIC', 8, 'CAPACITY', 'ACTIVE',
 'VALID', '^[0-9]+(\.[0-9]{1,2})?$', NULL,
 '{"value": 1.5}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

(104, 2, 4, 4,
 'ENUM', 2, 'ENERGY_RATING', 'ACTIVE',
 'VALID', '^(3|4|5)$', NULL,
 '{"value": "5"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP);

-- Units
INSERT INTO unit_of_measure (id, code, name, description, base_unit, conversion_factor, created_date, last_modified_date)
VALUES 
(1, 'TEST_SIZE', 'Test Size', 'Test size measurements', NULL, 1.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'TEST_STYLE', 'Test Style', 'Test style type', NULL, 1.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'TEST_WEIGHT', 'Test Weight', 'Test weight measurements', 'kg', 1000.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert test categories
INSERT INTO category (id, code, name, description, parent_id, dtype, created_date, last_modified_date)
VALUES 
-- Root category
(1, 'TEST_ROOT', 'Test Category', 'Test Root Category', NULL, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Sub categories
(2, 'TEST_SUB_1', 'Test Sub Category 1', 'Test Sub Category 1', 1, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'TEST_SUB_2', 'Test Sub Category 2', 'Test Sub Category 2', 1, 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Test Category Feature Templates
INSERT INTO category_feature_template (
    id, code, name, description,
    feature_type, validation_pattern,
    min_value, max_value, allowed_values,
    metadata, is_required,
    created_date, last_modified_date,
    category_id
)
VALUES 
(1, 'TEST_TEMPLATE_1', 'Test Template 1', 'Test feature template 1',
 'ENUM', '^(A|B|C)$', '0', '100', '["A", "B", "C"]',
 '{"group": "test", "tooltip": "Test tooltip"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),

(2, 'TEST_TEMPLATE_2', 'Test Template 2', 'Test feature template 2',
 'NUMERIC', '^[0-9]+(\.[0-9]{1,2})?$', '0', '1000', NULL,
 '{"group": "test", "tooltip": "Test numeric tooltip"}', true,
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);

-- Test Products
INSERT INTO product (
    id, code, name, description, product_type,
    status, metadata, sku,
    created_date, last_modified_date
)
VALUES 
(1, 'TEST_PROD_1', 'Test Product 1', 'Test product description 1',
 'TEST_TYPE', 'ACTIVE',
 '{"test_key": "test_value"}',
 'TEST-SKU-001',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 'TEST_PROD_2', 'Test Product 2', 'Test product description 2',
 'TEST_TYPE', 'ACTIVE',
 '{"test_key": "test_value_2"}',
 'TEST-SKU-002',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Test Product Features
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
(1, 1, 1, 'TEST_FEATURE_1', 'Test Feature 1', 'Test feature description 1',
 'ENUM', '^(A|B|C)$', '0', '100',
 '["A", "B", "C"]',
 '{"group": "test", "tooltip": "Test tooltip", "display_order": 1}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'STRING', true, true, true, true, false, 'A',
 1),

(2, 1, 2, 'TEST_FEATURE_2', 'Test Feature 2', 'Test feature description 2',
 'NUMERIC', '^[0-9]+(\.[0-9]{1,2})?$', '0', '1000',
 NULL,
 '{"group": "test", "tooltip": "Test numeric tooltip", "display_order": 2}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'DECIMAL', true, true, true, true, false, '10.5',
 3);

-- Test Product Feature Values
INSERT INTO product_feature_value (
    id, product_id, feature_id, template_id,
    type, unit, unit_of_measure, status,
    validation_status, validation_pattern,
    validation_message, attribute_values,
    created_by, created_date,
    last_modified_by, last_modified_date
)
VALUES 
(201, 1, 1, 1, 
 'ENUM', NULL, NULL, 'ACTIVE',
 'VALID', '^(A|B|C)$', NULL,
 '{"value": "A"}',
 'test_system', CURRENT_TIMESTAMP,
 'test_system', CURRENT_TIMESTAMP),

(202, 1, 2, 2, 
 'NUMERIC', NULL, NULL, 'ACTIVE',
 'VALID', '^[0-9]+(\.[0-9]{1,2})?$', NULL,
 '{"value": 42.5}',
 'test_system', CURRENT_TIMESTAMP,
 'test_system', CURRENT_TIMESTAMP);

-- Test Product Categories
INSERT INTO product_categories (product_id, category_id)
VALUES 
(1, 2), -- Test Product 1 in Test Sub Category 1
(2, 3); -- Test Product 2 in Test Sub Category 2
