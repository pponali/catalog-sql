-- Drop tables in reverse order of dependencies
DROP TABLE IF EXISTS enum_value_translation;
DROP TABLE IF EXISTS enum_value;
DROP TABLE IF EXISTS classification_attribute_value;
DROP TABLE IF EXISTS product_feature_value;
DROP TABLE IF EXISTS product_feature;
DROP TABLE IF EXISTS product_categories;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category_feature_template;
DROP TABLE IF EXISTS class_attribute_assignment;
DROP TABLE IF EXISTS classification_attribute;
DROP TABLE IF EXISTS classification_class_metadata;
DROP TABLE IF EXISTS classification_class;
DROP TABLE IF EXISTS category;

-- Category table (base table)
CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY,
    code VARCHAR(255),
    name VARCHAR(255),
    description TEXT,
    parent_id BIGINT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    dtype VARCHAR(31) NOT NULL,
    FOREIGN KEY (parent_id) REFERENCES category(id)
);

-- Classification Class table (extends Category)
CREATE TABLE IF NOT EXISTS classification_class (
    id BIGINT PRIMARY KEY,
    allow_multiple_categories BOOLEAN DEFAULT false,
    inherit_features BOOLEAN DEFAULT true,
    active BOOLEAN DEFAULT true,
    sequence INTEGER,
    FOREIGN KEY (id) REFERENCES category(id)
);

-- Classification Class Metadata table
CREATE TABLE IF NOT EXISTS classification_class_metadata (
    class_id BIGINT,
    key VARCHAR(255) NOT NULL,
    value TEXT,
    PRIMARY KEY (class_id, key),
    FOREIGN KEY (class_id) REFERENCES classification_class(id)
);

-- Classification Attribute table
CREATE TABLE IF NOT EXISTS classification_attribute (
    id BIGINT PRIMARY KEY,
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
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP
);

-- Class Attribute Assignment table
CREATE TABLE IF NOT EXISTS class_attribute_assignment (
    id BIGINT PRIMARY KEY,
    classification_class_id BIGINT,
    classification_attribute_id BIGINT,
    mandatory BOOLEAN DEFAULT false,
    visible BOOLEAN DEFAULT true,
    searchable BOOLEAN DEFAULT false,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    FOREIGN KEY (classification_class_id) REFERENCES classification_class(id),
    FOREIGN KEY (classification_attribute_id) REFERENCES classification_attribute(id)
);

-- Category Feature Template table
CREATE TABLE IF NOT EXISTS category_feature_template (
    id BIGINT PRIMARY KEY,
    category_id BIGINT,
    code VARCHAR(255),
    name VARCHAR(255),
    description TEXT,
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
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- Product table
CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY,
    sku VARCHAR(255) UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_date TIMESTAMP,
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
    id BIGINT PRIMARY KEY,
    product_id BIGINT,
    template_id BIGINT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    FOREIGN KEY (template_id) REFERENCES category_feature_template(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Product Feature Value table
CREATE TABLE IF NOT EXISTS product_feature_value (
    id BIGINT PRIMARY KEY,
    feature_id BIGINT,
    string_value TEXT,
    numeric_value NUMERIC,
    boolean_value BOOLEAN,
    attribute_value JSONB,
    unit VARCHAR(50),
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    FOREIGN KEY (feature_id) REFERENCES product_feature(id)
);

-- Classification Attribute Value table
CREATE TABLE IF NOT EXISTS classification_attribute_value (
    id BIGINT PRIMARY KEY,
    assignment_id BIGINT,
    value TEXT,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    FOREIGN KEY (assignment_id) REFERENCES class_attribute_assignment(id)
);

-- Enum Value table
CREATE TABLE IF NOT EXISTS enum_value (
    id BIGINT PRIMARY KEY,
    code VARCHAR(255),
    sort_order INTEGER,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP
);

-- Enum Value Translation table
CREATE TABLE IF NOT EXISTS enum_value_translation (
    id BIGINT PRIMARY KEY,
    enum_value_id BIGINT,
    language_code VARCHAR(10),
    value VARCHAR(255),
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    FOREIGN KEY (enum_value_id) REFERENCES enum_value(id)
);
