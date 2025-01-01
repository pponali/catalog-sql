-- Drop all tables in the correct order
DROP TABLE IF EXISTS product_feature_value CASCADE;
DROP TABLE IF EXISTS product_feature CASCADE;
DROP TABLE IF EXISTS category_feature_template CASCADE;
DROP TABLE IF EXISTS site_catalog_assignment CASCADE;
DROP TABLE IF EXISTS product_catalog_assignment CASCADE;
DROP TABLE IF EXISTS product_category_assignment CASCADE;
DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS catalog CASCADE;
DROP TABLE IF EXISTS site CASCADE;
DROP TABLE IF EXISTS business CASCADE;
DROP TABLE IF EXISTS unit_of_measure CASCADE;
DROP TABLE IF EXISTS unit CASCADE;

-- Drop all sequences
DROP SEQUENCE IF EXISTS category_seq CASCADE;
DROP SEQUENCE IF EXISTS product_seq CASCADE;
DROP SEQUENCE IF EXISTS business_seq CASCADE;
DROP SEQUENCE IF EXISTS catalog_seq CASCADE;
DROP SEQUENCE IF EXISTS site_seq CASCADE;
DROP SEQUENCE IF EXISTS unit_seq CASCADE;
DROP SEQUENCE IF EXISTS unit_of_measure_seq CASCADE;

-- Create sequences
CREATE SEQUENCE IF NOT EXISTS category_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS product_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS business_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS catalog_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS site_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS unit_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS unit_of_measure_seq START WITH 1000;

-- Create base tables
CREATE TABLE IF NOT EXISTS unit (
    id UUID PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS unit_of_measure (
    id UUID PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    base_unit_id UUID REFERENCES unit(id),
    conversion_factor DECIMAL(10,3),
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS business (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS site (
    id UUID PRIMARY KEY,
    business_id UUID REFERENCES business(id),
    name VARCHAR(100) NOT NULL,
    domain VARCHAR(100),
    locale VARCHAR(10),
    currency VARCHAR(3),
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS catalog (
    id UUID PRIMARY KEY,
    business_id UUID REFERENCES business(id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS category (
    id UUID PRIMARY KEY,
    parent_id UUID REFERENCES category(id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS product (
    id UUID PRIMARY KEY,
    business_id UUID REFERENCES business(id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    sku VARCHAR(50),
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS product_category_assignment (
    product_id UUID REFERENCES product(id),
    category_id UUID REFERENCES category(id),
    created_date TIMESTAMP,
    PRIMARY KEY (product_id, category_id)
);

CREATE TABLE IF NOT EXISTS product_catalog_assignment (
    product_id UUID REFERENCES product(id),
    catalog_id UUID REFERENCES catalog(id),
    created_date TIMESTAMP,
    PRIMARY KEY (product_id, catalog_id)
);

CREATE TABLE IF NOT EXISTS site_catalog_assignment (
    site_id UUID REFERENCES site(id),
    catalog_id UUID REFERENCES catalog(id),
    is_default BOOLEAN,
    created_date TIMESTAMP,
    PRIMARY KEY (site_id, catalog_id)
);

CREATE TABLE IF NOT EXISTS category_feature_template (
    id UUID PRIMARY KEY,
    category_id UUID REFERENCES category(id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    unit_id UUID REFERENCES unit_of_measure(id),
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS product_feature (
    id UUID PRIMARY KEY,
    product_id UUID REFERENCES product(id),
    name VARCHAR(100) NOT NULL,
    value TEXT,
    unit_id UUID REFERENCES unit_of_measure(id),
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS product_feature_value (
    id UUID PRIMARY KEY,
    feature_id UUID REFERENCES product_feature(id),
    value TEXT NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

-- Insert base units
INSERT INTO unit (id, code, name, description, created_date, last_modified_date, created_by, last_modified_by)
VALUES 
('550e8400-e29b-41d4-a716-446655440000', 'KG', 'Kilogram', 'Base unit for mass', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('550e8400-e29b-41d4-a716-446655440001', 'M', 'Meter', 'Base unit for length', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('550e8400-e29b-41d4-a716-446655440002', 'L', 'Liter', 'Base unit for volume', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('550e8400-e29b-41d4-a716-446655440003', 'PC', 'Piece', 'Base unit for count', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('550e8400-e29b-41d4-a716-446655440004', 'LENGTH', 'Length', 'Length measurements', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('550e8400-e29b-41d4-a716-446655440005', 'TEMP', 'Temperature', 'Temperature measurements', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert unit measures
INSERT INTO unit_of_measure (id, code, name, description, base_unit_id, conversion_factor, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('660e8400-e29b-41d4-a716-446655440000', 'G', 'Gram', 'Gram measurement', '550e8400-e29b-41d4-a716-446655440000', 0.001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('660e8400-e29b-41d4-a716-446655440001', 'MG', 'Milligram', 'Milligram measurement', '550e8400-e29b-41d4-a716-446655440000', 0.000001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('660e8400-e29b-41d4-a716-446655440002', 'CM', 'Centimeter', 'Centimeter measurement', '550e8400-e29b-41d4-a716-446655440001', 0.01, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('660e8400-e29b-41d4-a716-446655440003', 'MM', 'Millimeter', 'Millimeter measurement', '550e8400-e29b-41d4-a716-446655440001', 0.001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('660e8400-e29b-41d4-a716-446655440004', 'ML', 'Milliliter', 'Milliliter measurement', '550e8400-e29b-41d4-a716-446655440002', 0.001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert businesses
INSERT INTO business (id, name, description, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('770e8400-e29b-41d4-a716-446655440000', 'Fashion Retail', 'Fashion retail business', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440001', 'Grocery Store', 'Grocery retail business', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440002', 'Electronics Store', 'Electronics retail business', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('770e8400-e29b-41d4-a716-446655440003', 'Jewelry Store', 'Jewelry retail business', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert sites
INSERT INTO site (id, business_id, name, domain, locale, currency, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('880e8400-e29b-41d4-a716-446655440000', '770e8400-e29b-41d4-a716-446655440000', 'Fashion US', 'fashion.us', 'en-US', 'USD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440001', 'Grocery US', 'grocery.us', 'en-US', 'USD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440002', 'Electronics US', 'electronics.us', 'en-US', 'USD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('880e8400-e29b-41d4-a716-446655440003', '770e8400-e29b-41d4-a716-446655440003', 'Jewelry US', 'jewelry.us', 'en-US', 'USD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert catalogs
INSERT INTO catalog (id, business_id, name, description, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('990e8400-e29b-41d4-a716-446655440000', '770e8400-e29b-41d4-a716-446655440000', 'Fashion Catalog', 'Fashion catalog', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440001', 'Grocery Catalog', 'Grocery catalog', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440002', 'Electronics Catalog', 'Electronics catalog', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('990e8400-e29b-41d4-a716-446655440003', '770e8400-e29b-41d4-a716-446655440003', 'Jewelry Catalog', 'Jewelry catalog', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert categories
INSERT INTO category (id, parent_id, name, description, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('aa0e8400-e29b-41d4-a716-446655440000', NULL, 'Fashion', 'Fashion category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('aa0e8400-e29b-41d4-a716-446655440001', NULL, 'Grocery', 'Grocery category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('aa0e8400-e29b-41d4-a716-446655440002', NULL, 'Electronics', 'Electronics category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('aa0e8400-e29b-41d4-a716-446655440003', NULL, 'Jewelry', 'Jewelry category', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert products
INSERT INTO product (id, business_id, name, description, sku, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('bb0e8400-e29b-41d4-a716-446655440000', '770e8400-e29b-41d4-a716-446655440000', 'Fashion Product', 'Fashion product', 'FP001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('bb0e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440001', 'Grocery Product', 'Grocery product', 'GP001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('bb0e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440002', 'Electronics Product', 'Electronics product', 'EP001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('bb0e8400-e29b-41d4-a716-446655440003', '770e8400-e29b-41d4-a716-446655440003', 'Jewelry Product', 'Jewelry product', 'JP001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert product features
INSERT INTO product_feature (id, product_id, name, value, unit_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('cc0e8400-e29b-41d4-a716-446655440000', 'bb0e8400-e29b-41d4-a716-446655440000', 'Size', 'M', '660e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('cc0e8400-e29b-41d4-a716-446655440001', 'bb0e8400-e29b-41d4-a716-446655440001', 'Weight', '1', '660e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('cc0e8400-e29b-41d4-a716-446655440002', 'bb0e8400-e29b-41d4-a716-446655440002', 'Screen Size', '15', '660e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('cc0e8400-e29b-41d4-a716-446655440003', 'bb0e8400-e29b-41d4-a716-446655440003', 'Weight', '10', '660e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert product feature values
INSERT INTO product_feature_value (id, feature_id, value, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('dd0e8400-e29b-41d4-a716-446655440000', 'cc0e8400-e29b-41d4-a716-446655440000', 'M', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('dd0e8400-e29b-41d4-a716-446655440001', 'cc0e8400-e29b-41d4-a716-446655440001', '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('dd0e8400-e29b-41d4-a716-446655440002', 'cc0e8400-e29b-41d4-a716-446655440002', '15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('dd0e8400-e29b-41d4-a716-446655440003', 'cc0e8400-e29b-41d4-a716-446655440003', '10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert category feature templates
INSERT INTO category_feature_template (id, category_id, name, description, unit_id, created_date, last_modified_date, created_by, last_modified_by)
VALUES
('ee0e8400-e29b-41d4-a716-446655440000', 'aa0e8400-e29b-41d4-a716-446655440000', 'Size', 'Product size', '660e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('ee0e8400-e29b-41d4-a716-446655440001', 'aa0e8400-e29b-41d4-a716-446655440001', 'Weight', 'Product weight', '660e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('ee0e8400-e29b-41d4-a716-446655440002', 'aa0e8400-e29b-41d4-a716-446655440002', 'Screen Size', 'Screen size', '660e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system'),
('ee0e8400-e29b-41d4-a716-446655440003', 'aa0e8400-e29b-41d4-a716-446655440003', 'Weight', 'Product weight', '660e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system');

-- Insert site catalog assignments
INSERT INTO site_catalog_assignment (site_id, catalog_id, is_default) VALUES
('880e8400-e29b-41d4-a716-446655440000', '990e8400-e29b-41d4-a716-446655440000', true),
('880e8400-e29b-41d4-a716-446655440001', '990e8400-e29b-41d4-a716-446655440001', true),
('880e8400-e29b-41d4-a716-446655440002', '990e8400-e29b-41d4-a716-446655440002', true),
('880e8400-e29b-41d4-a716-446655440003', '990e8400-e29b-41d4-a716-446655440003', true);
