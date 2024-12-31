-- Units (no dependencies)
INSERT INTO unit (id, code, name, description, created_date, last_modified_date)
VALUES 
(1, 'SIZE', 'Size', 'Size measurements', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'STYLE', 'Style', 'Style type', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Root Categories
INSERT INTO category (id, code, name, description, parent_id, created_date, last_modified_date)
VALUES 
(1, 'ELECTRONICS', 'Electronics', 'Electronic items', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'APPAREL', 'Apparel', 'Clothing items', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'GROCERY', 'Grocery', 'Grocery items', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'JEWELRY', 'Jewelry', 'Jewelry items', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'MEDICINE', 'Medicine', 'Medicine items', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Products
INSERT INTO product (
    id, code, name, description,
    product_type, status, metadata, sku,
    created_by, created_date,
    last_modified_by, last_modified_date,
    category_id
)
VALUES 
-- Electronics Products
(1003, 'PHONE_001', 'Smartphone X', 'Latest smartphone model',
 'ELECTRONICS', 'ACTIVE',
 '{"brand": "TechPro", "series": "X", "year": "2023"}',
 'PHONE-X-001',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP,
 1),

(1004, 'LAPTOP_001', 'Pro Laptop', 'High-performance laptop',
 'ELECTRONICS', 'ACTIVE',
 '{"brand": "TechPro", "series": "Pro", "year": "2023"}',
 'LAPTOP-P-001',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP,
 1),

-- Apparel Products
(1001, 'FORMAL_SHIRT_001', 'Men''s Formal Shirt', 'Classic formal shirt for men',
 'APPAREL', 'ACTIVE',
 '{"brand": "ClassicWear", "department": "mens", "category": "formal_wear"}',
 'SHIRT-F-001',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP,
 2),

(1002, 'TSHIRT_001', 'Men''s Casual T-Shirt', 'Comfortable cotton t-shirt',
 'APPAREL', 'ACTIVE',
 '{"brand": "ComfortWear", "department": "mens", "category": "casual_wear"}',
 'TSHIRT-C-001',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP,
 2);

-- Product Features
INSERT INTO product_feature (
    id, code, name, description,
    feature_type, validation_pattern, min_value, max_value,
    allowed_values, metadata, required,
    created_date, last_modified_date,
    attribute_type, visible, editable, searchable,
    comparable, multi_valued, default_value,
    unit_id
)
VALUES 
(1, 'SIZE_CHART', 'Size Chart', 'Size specifications',
 'JSON', NULL, NULL, NULL,
 NULL,
 '{"group": "basic", "tooltip": "Size specifications", "display_order": 1}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'JSON', true, true, true, true, false, NULL,
 NULL),

(2, 'MATERIAL_COMPOSITION', 'Material Composition', 'Fabric details',
 'JSON', NULL, NULL, NULL,
 NULL,
 '{"group": "basic", "tooltip": "Fabric details", "display_order": 2}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'JSON', true, true, true, true, false, NULL,
 NULL),

(3, 'COLOR_INFORMATION', 'Color Information', 'Color and design details',
 'JSON', NULL, NULL, NULL,
 NULL,
 '{"group": "basic", "tooltip": "Color and design details", "display_order": 3}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'JSON', true, true, true, true, false, NULL,
 NULL),

(24, 'TECH_SPECS_PHONE', 'Technical Specifications', 'Detailed technical specifications',
 'JSON', NULL, NULL, NULL,
 NULL,
 '{"group": "tech", "tooltip": "Technical specifications", "display_order": 1}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'JSON', true, true, true, true, false, NULL,
 NULL),

(25, 'TECH_SPECS_LAPTOP', 'Technical Specifications', 'Detailed technical specifications',
 'JSON', NULL, NULL, NULL,
 NULL,
 '{"group": "tech", "tooltip": "Technical specifications", "display_order": 1}',
 true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
 'JSON', true, true, true, true, false, NULL,
 NULL);

-- Product Feature Values
INSERT INTO product_feature_value (
    id, product_id, feature_id, feature_value,
    created_by, created_date,
    last_modified_by, last_modified_date
)
VALUES 
(1, 1001, 1,
 '{"sizes": ["S", "M", "L", "XL"], "measurements": {"chest": {"S": "38", "M": "40", "L": "42", "XL": "44"}, "length": {"S": "28", "M": "29", "L": "30", "XL": "31"}, "shoulders": {"S": "17", "M": "18", "L": "19", "XL": "20"}}, "fit": "Regular"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

(2, 1001, 2,
 '{"primary_material": "Cotton", "composition": {"cotton": "80%", "polyester": "20%"}, "fabric_weight": "150 GSM", "care": ["Machine wash cold", "Do not bleach", "Tumble dry low"]}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

(3, 1001, 3,
 '{"primary_color": "White", "pattern": "Solid", "additional_colors": [], "color_code": "#FFFFFF"}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

(24, 1003, 24,
 '{"processor": "Snapdragon 8 Gen 2", "ram": "8GB", "screen": {"size": "6.1", "type": "AMOLED", "resolution": "2532x1170"}, "camera": {"main": "48MP", "ultra_wide": "12MP", "telephoto": "12MP"}, "battery": {"capacity": "3500mAh", "fast_charging": true}, "connectivity": ["5G", "WiFi 6E", "Bluetooth 5.3", "NFC"]}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP),

(25, 1004, 25,
 '{"processor": {"brand": "Intel", "model": "i7-13700H", "cores": 14, "base_clock": "3.7GHz"}, "memory": {"ram": "16GB", "type": "DDR5", "speed": "5200MHz"}, "display": {"size": "14", "resolution": "2880x1800", "refresh_rate": 90}, "graphics": {"type": "Integrated", "model": "Intel Iris Xe"}, "ports": ["2x Thunderbolt 4", "1x USB-A", "HDMI 2.0", "3.5mm Audio"], "wireless": ["WiFi 6E", "Bluetooth 5.2"]}',
 'system', CURRENT_TIMESTAMP,
 'system', CURRENT_TIMESTAMP);
