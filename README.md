# Catalog SQL DB POC

A Spring Boot application with PostgreSQL database integration, featuring Spring DevTools, Actuator, OpenAPI documentation, and JPA.

## Prerequisites

- Java 21
- Maven
- PostgreSQL database

## Setup

1. Create a PostgreSQL database named `catalogdb`
2. Update database credentials in `src/main/resources/application.yaml` if needed
3. Build the project: `mvn clean install`
4. Run the application: `mvn spring-boot:run`

## Core Features

- Spring Boot 3.2.1
- Spring Data JPA
- PostgreSQL Integration
- Spring DevTools for development
- Spring Actuator for monitoring
- OpenAPI/Swagger Documentation
- Lombok for reducing boilerplate code

## Flexible Feature System

### Dynamic Attribute System
- Flexible attribute-based model through ClassAttributeAssignment
- Dynamic attribute addition without code changes
- Support for multiple value types:
  - String
  - Numeric
  - Boolean
  - Custom attribute types

### Feature Template System
- Comprehensive template configuration:
  - Validation patterns
  - Min/Max values
  - Allowed values
  - Units
  - Visibility control
  - Editability
  - Search capabilities
  - Multi-value support
  - Custom metadata

### Flexible Value Storage
- Multiple value type support:
  - String values
  - Numeric values
  - Boolean values
  - Custom attribute values
- Automatic type conversion and validation

### Dynamic Validation
- Custom validation patterns
- Value range validation
- Type-specific validation
- Inter-attribute dependency validation

### Complex Relationships
- Hierarchical category structure
- Attribute dependencies
- Feature template inheritance
- Multi-valued feature support

### Extension Points
- Custom metadata support
- Pluggable validation system
- Flexible attribute types
- Dynamic feature configuration

## Adding Complex Features Without Code Changes

### 1. Adding New Attributes
```http
POST /api/classification/attributes
{
  "code": "new_feature",
  "name": "New Feature",
  "type": "string",
  "validationPattern": "^[A-Z0-9]+$",
  "allowedValues": ["VAL1", "VAL2"],
  "metadata": {
    "customField": "value"
  }
}
```

### 2. Creating Feature Templates
```http
POST /api/categories/templates
{
  "code": "complex_feature",
  "attributeType": "string",
  "multiValued": true,
  "validationPattern": "custom_regex",
  "dependencies": {
    "other_feature": "required_value"
  }
}
```

### 3. Assigning Features to Classes
```http
POST /api/classes/{classCode}/assignments
{
  "attributeCode": "complex_feature",
  "mandatory": true,
  "visible": true,
  "searchable": true,
  "dependencies": {
    "dependent_feature": "value"
  }
}
```

## Feature Extension Support

1. **Dynamic Metadata**
   - Store additional configuration in JSON format
   - Flexible schema for custom properties
   - Runtime configuration changes

2. **Flexible Validation**
   - Custom validation patterns
   - Complex validation rules
   - Cross-field validation

3. **Feature Dependencies**
   - Inter-feature relationships
   - Conditional visibility
   - Value dependencies

4. **Multi-value Support**
   - Multiple values per feature
   - Type-specific value handling
   - Bulk value operations

5. **Inheritance**
   - Feature inheritance through categories
   - Template inheritance
   - Override capabilities

6. **Custom Types**
   - Support for custom attribute types
   - Extensible type system
   - Custom type validation

## API Documentation

Access the API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## Actuator Endpoints

Access actuator endpoints at:
- http://localhost:8080/actuator

## Database Schema

The database schema for this project is documented in the `Schema.pdf` file in the root directory. This document contains:
- Detailed entity relationships
- Table structures
- Field descriptions
- Data types and constraints

Please refer to `Schema.pdf` for a complete understanding of the database design before making any changes to the data model.

## Development

The application uses Spring DevTools for automatic restart during development. Any changes to the classpath will trigger an automatic restart.

## Sample Data

The application includes sample data in `src/main/resources/data.sql` that demonstrates the flexible feature system across Tata Group's diverse product categories:

### BigBasket (Grocery)
- Categories:
  - Fresh Fruits
  - Fresh Vegetables
  - Dairy Products
  - Staples
- Features:
  - Organic Certification
  - Nutritional Information (JSON)
  - Storage Instructions
  - Shelf Life
- Example Product: Royal Gala Apple
  ```json
  {
    "origin": "New Zealand",
    "certification": "FSSAI-Certified",
    "nutrition": {
      "calories": 52,
      "protein": 0.3,
      "carbohydrates": 14,
      "fiber": 2.4
    }
  }
  ```

### 1mg (Healthcare)
- Categories:
  - Medicines
  - Wellness
  - Lab Tests
- Features:
  - Prescription Required (Boolean)
  - Medicine Composition (JSON)
  - Side Effects
  - Dosage Instructions
- Example Product: Crocin Advance
  ```json
  {
    "generic_name": "Paracetamol",
    "manufacturer": "GSK",
    "precautions": "Take as directed",
    "storage": {
      "temperature": "Below 25°C",
      "humidity": "Store in dry place"
    }
  }
  ```

### Tanishq (Jewelry)
- Categories:
  - Gold Jewelry
  - Diamond Jewelry
  - Wedding Collection
- Features:
  - Metal Purity
  - Diamond Certification (JSON)
  - Making Charges
  - Stone Details (JSON)
- Example Product: Diamond Solitaire Ring
  ```json
  {
    "metal_type": "18K Gold",
    "diamond_weight": "1.00 ct",
    "certification": "GIA",
    "diamond_details": {
      "cut": "Excellent",
      "clarity": "VS1",
      "color": "D",
      "carat": "1.00"
    }
  }
  ```

### Tata CLiQ (Fashion)
- Categories:
  - Mens Fashion
  - Womens Fashion
  - Luxury
- Features:
  - Size Chart (JSON)
  - Fabric Details
  - Brand Authentication
  - Fit Type
- Example Product: Premium Cotton Shirt
  ```json
  {
    "brand": "Louis Philippe",
    "material": "100% Cotton",
    "fit": "Regular",
    "size_details": {
      "size": "UK 10",
      "measurements": {
        "chest": "38",
        "length": "26"
      }
    }
  }
  ```

### Complex Feature Support

1. **Dynamic JSON Attributes**
   - Flexible schema for each category
   - Nested attribute support
   - Array value support
   - Type validation

2. **Category-Specific Validation**
   - BigBasket: FSSAI compliance, expiry tracking
   - 1mg: Prescription validation, drug interactions
   - Tanishq: Purity certification, stone grading
   - Tata CLiQ: Size validation, brand authenticity

3. **Search Capabilities**
   - Full-text search across attributes
   - Category-specific filters
   - Range-based queries
   - Faceted search

4. **Data Relationships**
   - Product hierarchies
   - Category inheritance
   - Feature dependencies
   - Cross-category relationships

## Sample API Calls

1. Get product features:
```http
GET /api/products/1/features
```

2. Get category hierarchy:
```http
GET /api/categories/1/tree
```

3. Get class attributes:
```http
GET /api/classes/LAPTOP/attributes
```

The sample data demonstrates:
- Hierarchical categories and classifications
- Different attribute types (string, numeric)
- Feature templates with validation
- Product features with values
- Enum values with translations
