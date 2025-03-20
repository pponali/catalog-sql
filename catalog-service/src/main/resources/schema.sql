-- Drop tables in reverse order of dependencies
DROP TABLE IF EXISTS site_catalog CASCADE;
DROP TABLE IF EXISTS enum_value_translation CASCADE;
DROP TABLE IF EXISTS enum_value CASCADE;
DROP TABLE IF EXISTS product_feature_value CASCADE;
DROP TABLE IF EXISTS product_feature CASCADE;
DROP TABLE IF EXISTS product_categories CASCADE;
DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS category_feature_template CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS catalog_migration_product CASCADE;
DROP TABLE IF EXISTS catalog_migration_category CASCADE;
DROP TABLE IF EXISTS catalog_migration_error CASCADE;
DROP TABLE IF EXISTS catalog_migration_warning CASCADE;
DROP TABLE IF EXISTS catalog_migration CASCADE;
DROP TABLE IF EXISTS catalog CASCADE;
DROP TABLE IF EXISTS site CASCADE;
DROP TABLE IF EXISTS Merchant CASCADE;
DROP TABLE IF EXISTS unit_of_measure CASCADE;
DROP TABLE IF EXISTS feature_value_event CASCADE;
DROP TABLE IF EXISTS unit CASCADE;
DROP TABLE IF EXISTS validation_rule CASCADE;

-- Merchant table
CREATE TABLE IF NOT EXISTS Merchant (
    id UUID PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    description TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Site table
CREATE TABLE IF NOT EXISTS site (
    id UUID PRIMARY KEY,
    business_id UUID NOT NULL REFERENCES merchant(id),
    name VARCHAR(255) NOT NULL,
    domain VARCHAR(255) NOT NULL,
    locale VARCHAR(10) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Catalog table
CREATE TABLE IF NOT EXISTS catalog (
    id UUID PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(100),
    status VARCHAR(50),
    business_id UUID NOT NULL REFERENCES business(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Site Catalog junction table
CREATE TABLE IF NOT EXISTS site_catalog (
    site_id UUID REFERENCES site(id),
    catalog_id UUID REFERENCES catalog(id),
    is_default BOOLEAN NOT NULL DEFAULT false,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    PRIMARY KEY (site_id, catalog_id)
);

-- Validation Rule table
CREATE TABLE IF NOT EXISTS validation_rule (
    id UUID PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    feature_id BIGINT,
    rule_type VARCHAR(50) NOT NULL,
    rule_expression TEXT NOT NULL,
    validation_pattern VARCHAR(255),
    min_value VARCHAR(255),
    max_value VARCHAR(255),
    allowed_values TEXT,
    priority INTEGER,
    active BOOLEAN DEFAULT true,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Unit table
CREATE TABLE IF NOT EXISTS unit (
    id UUID PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL UNIQUE,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    description TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Unit of Measure table
CREATE TABLE IF NOT EXISTS unit_of_measure (
    id UUID PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    active  BOOLEAN DEFAULT true,
    display_symbol VARCHAR(10),
    metadata VARCHAR(255),
    description TEXT,
    unit_id UUID REFERENCES unit(id),
    conversion_factor DECIMAL(19,4),
    base_unit_id UUID REFERENCES unit_of_measure(id),
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Category table
CREATE TABLE IF NOT EXISTS category (
    id UUID PRIMARY KEY,
    type VARCHAR(50),
    business_id UUID NOT NULL REFERENCES business(id),
    catalog_id UUID NOT NULL REFERENCES catalog(id),
    parent_id UUID REFERENCES category(id),
    name VARCHAR(255) NOT NULL,
    inherit_features BOOLEAN DEFAULT false,
    description TEXT,
    code VARCHAR(255) NOT NULL UNIQUE,
    level INTEGER,
    status VARCHAR(50),
    metadata JSONB,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- catalog_category table
CREATE TABLE IF NOT EXISTS catalog_category (
    catalog_id UUID REFERENCES catalog(id),
    category_id UUID REFERENCES category(id),
    primary_category BOOLEAN DEFAULT false,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    PRIMARY KEY (catalog_id, category_id)
);

-- Product table
CREATE TABLE IF NOT EXISTS product (
    id UUID PRIMARY KEY,
    business_id UUID NOT NULL REFERENCES business(id),
    catalog_id UUID NOT NULL REFERENCES catalog(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    code VARCHAR(255) NOT NULL UNIQUE,
    product_type VARCHAR(50),
    status VARCHAR(50),
    metadata JSONB,
    sku VARCHAR(255),
    price DECIMAL(19,4),
    unit_of_measure_id UUID REFERENCES unit_of_measure(id),
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Product Categories junction table
CREATE TABLE IF NOT EXISTS product_categories (
    product_id UUID REFERENCES product(id),
    category_id UUID REFERENCES category(id),
    primary_category BOOLEAN DEFAULT false,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    PRIMARY KEY (product_id, category_id)
);

-- Feature Template table (parent table)
CREATE TABLE IF NOT EXISTS feature_template (
    id UUID PRIMARY KEY,
    template_type VARCHAR(50) NOT NULL,  -- Discriminator column
    name VARCHAR(255) NOT NULL,
    description TEXT,
    feature_type VARCHAR(50) NOT NULL,
    data_type VARCHAR(50) NOT NULL,
    input_type VARCHAR(50) NOT NULL,
    validation_pattern TEXT,
    min_value VARCHAR(255),
    max_value VARCHAR(255),
    allowed_values TEXT,
    default_value TEXT,
    unit_id UUID REFERENCES unit(id),
    required BOOLEAN DEFAULT false,
    filterable BOOLEAN DEFAULT true,
    hidden BOOLEAN DEFAULT false,
    multi_valued BOOLEAN DEFAULT false,
    searchable BOOLEAN DEFAULT true,
    comparable BOOLEAN DEFAULT true,
    visible BOOLEAN DEFAULT true,
    editable BOOLEAN DEFAULT true,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Category Feature Template table (child table)
CREATE TABLE IF NOT EXISTS category_feature_template (
    id UUID PRIMARY KEY REFERENCES feature_template(id),
    code VARCHAR(255) NOT NULL,
    attribute_type VARCHAR(50),
    inherited BOOLEAN DEFAULT false,
    mandatory BOOLEAN DEFAULT false,
    metadata JSONB DEFAULT '{}',
    category_id UUID NOT NULL REFERENCES category(id),
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    UNIQUE(code, category_id)
);

-- Product Feature table
CREATE TABLE IF NOT EXISTS product_feature (
    id UUID PRIMARY KEY,
    product_id UUID REFERENCES product(id),
    template_id UUID REFERENCES category_feature_template(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    code VARCHAR(255) NOT NULL,
    feature_type VARCHAR(50) NOT NULL,
    data_type VARCHAR(50) NOT NULL,
    input_type VARCHAR(50) NOT NULL,
    validation_pattern TEXT,
    min_value VARCHAR(255),
    max_value VARCHAR(255),
    allowed_values TEXT,
    attribute_type VARCHAR(50),
    comparable BOOLEAN DEFAULT false,
    visible BOOLEAN DEFAULT true,
    searchable BOOLEAN DEFAULT false,
    editable BOOLEAN DEFAULT true,
    multi_valued BOOLEAN DEFAULT false,
    default_value TEXT,
    unit_of_measure_id UUID REFERENCES unit_of_measure(id),
    metadata JSONB DEFAULT '{}',
    required BOOLEAN DEFAULT false,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    UNIQUE (code, product_id)
);

-- Product Feature Value table
CREATE TABLE IF NOT EXISTS product_feature_value (
    id UUID PRIMARY KEY,
    type VARCHAR(50),
    product_id UUID REFERENCES product(id),
    attribute_values JSONB,
    metadata JSONB,
    status  VARCHAR(50),
    template_id UUID REFERENCES category_feature_template(id),
    feature_id UUID REFERENCES product_feature(id),
    unit VARCHAR(255),
    unit_of_measure VARCHAR(255),
    validation_message VARCHAR(255),
    validation_pattern VARCHAR(255),
    validation_status VARCHAR(50),
    value TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Enum Value table
CREATE TABLE IF NOT EXISTS enum_value (
    id UUID PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL,
    description TEXT,
    sort_order INTEGER DEFAULT 0,
    active BOOLEAN DEFAULT true,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Enum Value Translation table
CREATE TABLE IF NOT EXISTS enum_value_translation (
    id UUID PRIMARY KEY,
    enum_value_id UUID REFERENCES enum_value(id),
    language_code VARCHAR(10),
    value VARCHAR(255),
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    UNIQUE (enum_value_id, language_code)
);

-- Feature Value Event table
CREATE TABLE IF NOT EXISTS feature_value_event (
    id UUID PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    feature_id UUID,
    old_value TEXT,
    new_value TEXT,
    event_timestamp TIMESTAMP NOT NULL,
    user_id VARCHAR(255),
    source VARCHAR(255),
    metadata JSONB
);

-- Catalog Migration Table
CREATE TABLE IF NOT EXISTS catalog_migration (
    id UUID PRIMARY KEY,
    source_catalog_id UUID NOT NULL,
    target_catalog_id UUID NOT NULL,
    migration_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    message TEXT,
    categories_migrated INT DEFAULT 0,
    products_migrated INT DEFAULT 0,
    migration_notes TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    performed_by VARCHAR(255),
    FOREIGN KEY (source_catalog_id) REFERENCES catalog(id),
    FOREIGN KEY (target_catalog_id) REFERENCES catalog(id)
);

-- Catalog Migration Warnings Table
CREATE TABLE IF NOT EXISTS catalog_migration_warning (
    migration_id UUID NOT NULL,
    warning TEXT NOT NULL,
    FOREIGN KEY (migration_id) REFERENCES catalog_migration(id)
);

-- Catalog Migration Errors Table
CREATE TABLE IF NOT EXISTS catalog_migration_error (
    migration_id UUID NOT NULL,
    error TEXT NOT NULL,
    FOREIGN KEY (migration_id) REFERENCES catalog_migration(id)
);

-- Migrated Category IDs Table
CREATE TABLE IF NOT EXISTS catalog_migration_category (
    migration_id UUID NOT NULL,
    category_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (migration_id) REFERENCES catalog_migration(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- Migrated Product IDs Table
CREATE TABLE IF NOT EXISTS catalog_migration_product (
    migration_id UUID NOT NULL,
    product_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (migration_id) REFERENCES catalog_migration(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);
