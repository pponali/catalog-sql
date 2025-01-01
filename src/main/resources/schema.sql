-- Drop tables in reverse order of dependencies
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
DROP TABLE IF EXISTS enum_value_translation CASCADE;
DROP TABLE IF EXISTS enum_value CASCADE;
DROP TABLE IF EXISTS feature_value_event CASCADE;
DROP TABLE IF EXISTS validation_rule CASCADE;
DROP TABLE IF EXISTS catalog_migration_product CASCADE;
DROP TABLE IF EXISTS catalog_migration_category CASCADE;
DROP TABLE IF EXISTS catalog_migration_error CASCADE;
DROP TABLE IF EXISTS catalog_migration_warning CASCADE;
DROP TABLE IF EXISTS catalog_migration CASCADE;

-- Unit table
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

-- Unit of Measure table
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

-- Business table
CREATE TABLE IF NOT EXISTS business (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

-- Site table
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

-- Catalog table
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

-- Category table
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

-- Product table
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

-- Product Category Assignment table
CREATE TABLE IF NOT EXISTS product_category_assignment (
    product_id UUID REFERENCES product(id),
    category_id UUID REFERENCES category(id),
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50) NOT NULL DEFAULT 'system',
    last_modified_by VARCHAR(50) NOT NULL DEFAULT 'system',
    PRIMARY KEY (product_id, category_id)
);

-- Product Catalog Assignment table
CREATE TABLE IF NOT EXISTS product_catalog_assignment (
    product_id UUID REFERENCES product(id),
    catalog_id UUID REFERENCES catalog(id),
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50) NOT NULL DEFAULT 'system',
    last_modified_by VARCHAR(50) NOT NULL DEFAULT 'system',
    PRIMARY KEY (product_id, catalog_id)
);

-- Site Catalog Assignment table
CREATE TABLE IF NOT EXISTS site_catalog_assignment (
    site_id UUID REFERENCES site(id),
    catalog_id UUID REFERENCES catalog(id),
    is_default BOOLEAN DEFAULT false,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50) NOT NULL DEFAULT 'system',
    last_modified_by VARCHAR(50) NOT NULL DEFAULT 'system',
    PRIMARY KEY (site_id, catalog_id)
);

-- Category Feature Template table
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

-- Product Feature table
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

-- Product Feature Value table
CREATE TABLE IF NOT EXISTS product_feature_value (
    id UUID PRIMARY KEY,
    feature_id UUID REFERENCES product_feature(id),
    value TEXT NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

-- Validation Rule table
CREATE TABLE IF NOT EXISTS validation_rule (
    id UUID PRIMARY KEY,
    feature_id UUID REFERENCES product_feature(id),
    rule_type VARCHAR(50) NOT NULL,
    rule_value TEXT NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

-- Enum Value table
CREATE TABLE IF NOT EXISTS enum_value (
    id UUID PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

-- Enum Value Translation table
CREATE TABLE IF NOT EXISTS enum_value_translation (
    id UUID PRIMARY KEY,
    enum_value_id UUID REFERENCES enum_value(id),
    locale VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

-- Feature Value Event table
CREATE TABLE IF NOT EXISTS feature_value_event (
    id UUID PRIMARY KEY,
    feature_id UUID REFERENCES product_feature(id),
    event_type VARCHAR(50) NOT NULL,
    event_data TEXT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

-- Catalog Migration table
CREATE TABLE IF NOT EXISTS catalog_migration (
    id UUID PRIMARY KEY,
    source_catalog_id UUID REFERENCES catalog(id),
    target_catalog_id UUID REFERENCES catalog(id),
    status VARCHAR(50) NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

-- Catalog Migration Category table
CREATE TABLE IF NOT EXISTS catalog_migration_category (
    id UUID PRIMARY KEY,
    migration_id UUID REFERENCES catalog_migration(id),
    source_category_id UUID REFERENCES category(id),
    target_category_id UUID REFERENCES category(id),
    status VARCHAR(50) NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

-- Catalog Migration Product table
CREATE TABLE IF NOT EXISTS catalog_migration_product (
    id UUID PRIMARY KEY,
    migration_id UUID REFERENCES catalog_migration(id),
    source_product_id UUID REFERENCES product(id),
    target_product_id UUID REFERENCES product(id),
    status VARCHAR(50) NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

-- Catalog Migration Error table
CREATE TABLE IF NOT EXISTS catalog_migration_error (
    id UUID PRIMARY KEY,
    migration_id UUID REFERENCES catalog_migration(id),
    error_type VARCHAR(50) NOT NULL,
    error_message TEXT NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);

-- Catalog Migration Warning table
CREATE TABLE IF NOT EXISTS catalog_migration_warning (
    id UUID PRIMARY KEY,
    migration_id UUID REFERENCES catalog_migration(id),
    warning_type VARCHAR(50) NOT NULL,
    warning_message TEXT NOT NULL,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50)
);
