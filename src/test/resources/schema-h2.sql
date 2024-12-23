-- Create tables
CREATE TABLE IF NOT EXISTS category (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    code VARCHAR(255),
    name VARCHAR(255),
    description CLOB,
    parent_id BIGINT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE category 
ADD CONSTRAINT fk_category_parent 
FOREIGN KEY (parent_id) 
REFERENCES category(id);

CREATE TABLE IF NOT EXISTS classification_class (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    code VARCHAR(255),
    name VARCHAR(255),
    description CLOB,
    category_id BIGINT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE IF NOT EXISTS classification_attribute (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    code VARCHAR(255),
    name VARCHAR(255),
    description CLOB,
    attribute_type VARCHAR(50),
    validation_pattern VARCHAR(255),
    min_value NUMERIC,
    max_value NUMERIC,
    unit VARCHAR(50),
    visible BOOLEAN DEFAULT TRUE,
    editable BOOLEAN DEFAULT TRUE,
    searchable BOOLEAN DEFAULT TRUE,
    comparable BOOLEAN DEFAULT FALSE,
    mandatory BOOLEAN DEFAULT FALSE,
    multi_valued BOOLEAN DEFAULT FALSE,
    metadata CLOB,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS class_attribute_assignment (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    classification_class_id BIGINT,
    classification_attribute_id BIGINT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (classification_class_id) REFERENCES classification_class(id),
    FOREIGN KEY (classification_attribute_id) REFERENCES classification_attribute(id)
);

CREATE TABLE IF NOT EXISTS category_feature_template (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    code VARCHAR(255),
    name VARCHAR(255),
    description CLOB,
    attribute_type VARCHAR(50),
    validation_pattern VARCHAR(255),
    min_value NUMERIC,
    max_value NUMERIC,
    unit VARCHAR(50),
    visible BOOLEAN DEFAULT TRUE,
    editable BOOLEAN DEFAULT TRUE,
    searchable BOOLEAN DEFAULT TRUE,
    comparable BOOLEAN DEFAULT FALSE,
    mandatory BOOLEAN DEFAULT FALSE,
    multi_valued BOOLEAN DEFAULT FALSE,
    metadata CLOB,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    code VARCHAR(255),
    name VARCHAR(255),
    description CLOB,
    category_id BIGINT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE IF NOT EXISTS product_feature (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    code VARCHAR(255),
    name VARCHAR(255),
    description CLOB,
    product_id BIGINT,
    template_id BIGINT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (template_id) REFERENCES category_feature_template(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE IF NOT EXISTS product_feature_value (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    feature_id BIGINT,
    string_value CLOB,
    numeric_value NUMERIC,
    boolean_value BOOLEAN,
    attribute_value CLOB,
    unit VARCHAR(50),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (feature_id) REFERENCES product_feature(id)
);
