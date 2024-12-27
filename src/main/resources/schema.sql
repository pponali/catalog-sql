-- Drop tables in reverse order of dependencies
DROP TABLE IF EXISTS enum_value_translation CASCADE;
DROP TABLE IF EXISTS enum_value CASCADE;
DROP TABLE IF EXISTS classification_attribute_value CASCADE;
DROP TABLE IF EXISTS product_feature_value CASCADE;
DROP TABLE IF EXISTS product_feature CASCADE;
DROP TABLE IF EXISTS product_categories CASCADE;
DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS category_feature_template CASCADE;
DROP TABLE IF EXISTS class_attribute_assignments CASCADE;
DROP TABLE IF EXISTS classification_attribute CASCADE;
DROP TABLE IF EXISTS classification_class_metadata CASCADE;
DROP TABLE IF EXISTS classification_class CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS unit_of_measure CASCADE;
DROP TABLE IF EXISTS feature_value_event CASCADE;

-- Sequences
CREATE SEQUENCE IF NOT EXISTS category_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS product_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS product_feature_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS category_feature_template_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS classification_attribute_seq START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS class_attribute_assignment_seq START WITH 1000;

-- Unit of Measure table (no dependencies)
CREATE TABLE IF NOT EXISTS unit_of_measure (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    base_unit VARCHAR(50),
    conversion_factor NUMERIC,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Category table (base table)
CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY DEFAULT nextval('category_seq'),
    code VARCHAR(255),
    name VARCHAR(255),
    description TEXT,
    parent_id BIGINT,
    allow_multiple_categories BOOLEAN DEFAULT false,
    inherit_features BOOLEAN DEFAULT true,
    active BOOLEAN DEFAULT true,
    sequence INTEGER,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    dtype VARCHAR(31) NOT NULL,
    FOREIGN KEY (parent_id) REFERENCES category(id)
);

-- Classification Class table (extends Category)
CREATE TABLE IF NOT EXISTS classification_class (
    id BIGINT PRIMARY KEY,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (id) REFERENCES category(id)
);

-- Classification Class Metadata table
CREATE TABLE IF NOT EXISTS classification_class_metadata (
    class_id BIGINT,
    key VARCHAR(255) NOT NULL,
    value TEXT,
    PRIMARY KEY (class_id, key),
    FOREIGN KEY (class_id) REFERENCES category(id)
);

-- Classification Attribute table
CREATE TABLE IF NOT EXISTS classification_attribute (
    id BIGINT PRIMARY KEY DEFAULT nextval('classification_attribute_seq'),
    code VARCHAR(255),
    name VARCHAR(255),
    description TEXT,
    attribute_type VARCHAR(50),
    validation_pattern VARCHAR(255),
    min_value NUMERIC,
    max_value NUMERIC,
    unit VARCHAR(50),
    visible BOOLEAN,
    editable BOOLEAN,
    searchable BOOLEAN,
    comparable BOOLEAN,
    mandatory BOOLEAN,
    multi_valued BOOLEAN,
    metadata JSONB,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Class Attribute Assignment table
CREATE TABLE IF NOT EXISTS class_attribute_assignments (
    id BIGINT PRIMARY KEY DEFAULT nextval('class_attribute_assignment_seq'),
    classification_class_id BIGINT,
    classification_attribute_id BIGINT,
    unit VARCHAR(50),
    attribute_type VARCHAR(50) NOT NULL,
    mandatory BOOLEAN DEFAULT false,
    multi_valued BOOLEAN DEFAULT false,
    sequence INTEGER,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (classification_class_id) REFERENCES category(id),
    FOREIGN KEY (classification_attribute_id) REFERENCES classification_attribute(id)
);

-- Category Feature Template table
CREATE TABLE IF NOT EXISTS category_feature_template (
    id BIGINT PRIMARY KEY DEFAULT nextval('category_feature_template_seq'),
    category_id BIGINT,
    code VARCHAR(255),
    name VARCHAR(255),
    description TEXT,
    feature_type VARCHAR(50),
    attribute_type VARCHAR(50),
    validation_pattern VARCHAR(255),
    min_value NUMERIC,
    max_value NUMERIC,
    allowed_values TEXT,
    unit VARCHAR(50),
    visible BOOLEAN,
    editable BOOLEAN,
    searchable BOOLEAN,
    comparable BOOLEAN,
    mandatory BOOLEAN,
    multi_valued BOOLEAN,
    metadata JSONB,
    display_order INTEGER,
    is_required BOOLEAN,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- Product table
CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY DEFAULT nextval('product_seq'),
    code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    product_type VARCHAR(50),
    status VARCHAR(50),
    metadata JSONB,
    sku VARCHAR(255) UNIQUE,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Product Categories (Many-to-Many relationship table)
CREATE TABLE IF NOT EXISTS product_categories (
    product_id BIGINT,
    category_id BIGINT,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- Product Feature table
CREATE TABLE IF NOT EXISTS product_feature (
    id BIGINT PRIMARY KEY DEFAULT nextval('product_feature_seq'),
    product_id BIGINT NOT NULL,
    template_id BIGINT NOT NULL,
    code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    feature_type VARCHAR(50),
    attribute_type VARCHAR(50),
    validation_pattern VARCHAR(255),
    min_value VARCHAR(255),
    max_value VARCHAR(255),
    allowed_values TEXT,
    default_value VARCHAR(255),
    unit_id BIGINT,
    metadata JSONB,
    required BOOLEAN NOT NULL DEFAULT false,
    visible BOOLEAN DEFAULT true,
    editable BOOLEAN DEFAULT true,
    searchable BOOLEAN DEFAULT false,
    comparable BOOLEAN DEFAULT false,
    multi_valued BOOLEAN DEFAULT false,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (template_id) REFERENCES category_feature_template(id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (unit_id) REFERENCES unit_of_measure(id)
);

-- Product Feature Value table
CREATE TABLE IF NOT EXISTS product_feature_value (
    id BIGINT PRIMARY KEY,
    product_id BIGINT,
    feature_id BIGINT,
    template_id BIGINT,
    type VARCHAR(50),
    unit VARCHAR(50),
    unit_of_measure VARCHAR(50),
    status VARCHAR(50),
    validation_status VARCHAR(50),
    validation_pattern VARCHAR(255),
    validation_message TEXT,
    attribute_values JSONB,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (feature_id) REFERENCES product_feature(id)
);

-- Classification Attribute Value table
CREATE TABLE IF NOT EXISTS classification_attribute_value (
    id BIGINT PRIMARY KEY,
    assignment_id BIGINT,
    value TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (assignment_id) REFERENCES class_attribute_assignments(id)
);

-- Enum Value table
CREATE TABLE IF NOT EXISTS enum_value (
    id BIGINT PRIMARY KEY,
    code VARCHAR(255),
    sort_order INTEGER,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP
);

-- Enum Value Translation table
CREATE TABLE IF NOT EXISTS enum_value_translation (
    id BIGINT PRIMARY KEY,
    enum_value_id BIGINT,
    language_code VARCHAR(10),
    value VARCHAR(255),
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (enum_value_id) REFERENCES enum_value(id)
);

-- Feature Value Event table
CREATE TABLE IF NOT EXISTS feature_value_event (
    id BIGINT PRIMARY KEY,
    feature_id BIGINT,
    product_id BIGINT,
    old_value TEXT,
    new_value TEXT,
    event_type VARCHAR(50),
    metadata TEXT,
    timestamp TIMESTAMP,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    FOREIGN KEY (feature_id) REFERENCES product_feature(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);
