-- Main Categories
INSERT INTO categories (code, name, platform, created_at, created_by, updated_at, updated_by)
VALUES 
('TATA_JEWELRY', 'Tata Jewelry', 'TATA_DIGITAL', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('TATA_1MG', 'Tata 1mg', 'TATA_DIGITAL', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('TATA_CLIQ_FASHION', 'Tata CLiQ Fashion', 'TATA_DIGITAL', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('TATA_CROMA', 'Croma Digital', 'TATA_DIGITAL', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

-- Classification Classes
INSERT INTO classification_classes (code, name, created_at, created_by, updated_at, updated_by)
VALUES 
-- Jewelry Classes
('JEWELRY_RING', 'Rings', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('JEWELRY_NECKLACE', 'Necklaces', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('JEWELRY_EARRING', 'Earrings', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),

-- Medicine Classes
('MEDICINE_PRESCRIPTION', 'Prescription Medicines', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('MEDICINE_OTC', 'Over The Counter', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('MEDICINE_AYURVEDIC', 'Ayurvedic Products', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),

-- Fashion Classes
('FASHION_APPAREL', 'Apparel', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('FASHION_FOOTWEAR', 'Footwear', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('FASHION_ACCESSORIES', 'Accessories', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),

-- Electronics Classes
('ELECTRONICS_MOBILE', 'Mobile Phones', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('ELECTRONICS_LAPTOP', 'Laptops', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('ELECTRONICS_TV', 'Televisions', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

-- Classification Attributes
INSERT INTO classification_attributes (code, name, attribute_type, created_at, created_by, updated_at, updated_by)
VALUES 
-- Common Attributes
('PRODUCT_SKU', 'SKU', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('PRODUCT_NAME', 'Name', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('PRODUCT_DESCRIPTION', 'Description', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('PRODUCT_PRICE', 'Price', 'number', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('PRODUCT_BRAND', 'Brand', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),

-- Jewelry Specific Attributes
('JEWELRY_METAL_TYPE', 'Metal Type', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('JEWELRY_PURITY', 'Purity', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('JEWELRY_STONE_TYPE', 'Stone Type', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('JEWELRY_WEIGHT', 'Weight (g)', 'number', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('JEWELRY_SIZE', 'Size', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),

-- Medicine Specific Attributes
('MEDICINE_COMPOSITION', 'Composition', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('MEDICINE_DOSAGE', 'Dosage', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('MEDICINE_EXPIRY', 'Expiry Date', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('MEDICINE_PRESCRIPTION_REQUIRED', 'Prescription Required', 'boolean', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('MEDICINE_STORAGE', 'Storage Instructions', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),

-- Fashion Specific Attributes
('FASHION_SIZE', 'Size', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('FASHION_COLOR', 'Color', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('FASHION_MATERIAL', 'Material', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('FASHION_GENDER', 'Gender', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('FASHION_CARE_INSTRUCTIONS', 'Care Instructions', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),

-- Electronics Specific Attributes
('ELECTRONICS_MODEL', 'Model', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('ELECTRONICS_WARRANTY', 'Warranty Period', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('ELECTRONICS_SPECS', 'Specifications', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('ELECTRONICS_COLOR', 'Color', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
('ELECTRONICS_POWER', 'Power Rating', 'string', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');
