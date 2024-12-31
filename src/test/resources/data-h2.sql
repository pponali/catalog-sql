-- Units (no dependencies)
INSERT INTO unit (id, code, name, description, created_date, last_modified_date)
VALUES 
(1, 'SIZE', 'Size', 'Size measurements', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'STYLE', 'Style', 'Style type', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Root Categories
INSERT INTO category (id, code, name, description, parent_id, created_date, last_modified_date)
VALUES 
(1, 'MSH1230', 'Home Appliances', 'Home and Kitchen Appliances', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'apparelCCWEE', 'Yeswee Apparel', 'Fashion and Clothing', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Products
INSERT INTO product (id, code, name, description, category_id, created_date, last_modified_date)
VALUES 
(1, 'FORMAL_SHIRT_001', 'Men''s Formal Shirt', 'Classic formal shirt for men', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'LAPTOP_TEST_001', 'Test Laptop', 'Test laptop with JSON specs', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Product Features
INSERT INTO product_feature (id, code, name, description, product_id, template_id, created_date, last_modified_date)
VALUES 
(1, 'SIZE_SHIRT', 'Size', 'Shirt size', 1, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'TECH_SPECS', 'Technical Specifications', 'Laptop technical specifications', 2, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Product Feature Values
INSERT INTO product_feature_value (
    id, product_id, feature_id, type,
    attribute_values,
    created_date,
    last_modified_date
)
VALUES 
(1, 1, 1, 'ENUM',
 '{"value": "M"}',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 2, 'JSON',
 '{"processor": {"brand": "AMD", "model": "Ryzen 7 5800H", "cores": 8},
   "memory": {"ram": "32GB", "type": "DDR4"},
   "storage": {"type": "SSD", "capacity": "1TB"},
   "display": {"size": "14.0", "resolution": "1920x1080"}}',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
