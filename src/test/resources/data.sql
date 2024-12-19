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

-- Insert test product feature values
INSERT INTO product_feature_value (feature_id, string_value, numeric_value, boolean_value, attribute_value, unit, created_date, last_modified_date)
VALUES 
    (1, NULL, 150, NULL, NULL, 'W', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Blue', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, '2024-12-31', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 'Generic', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 'Gold', NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
