# Sample Data Files

This directory contains CSV files with sample data for the catalog system.

## Sample Products

The `sample_products.csv` file contains sample product data that can be imported into the system. The file includes various apparel products with their attributes based on the classification attributes defined in the `DevMDD_Classification_Attributes.csv` file.

### File Structure

The CSV file has the following columns:

- `product_id`: Unique identifier for the product
- `product_code`: Product code (used for lookups)
- `product_name`: Display name of the product
- `description`: Product description
- `product_type`: Type of product (SIMPLE, CONFIGURABLE, BUNDLE, VIRTUAL, VARIANT)
- `status`: Product status (ACTIVE, INACTIVE)
- `price`: Product price
- `sku`: Stock keeping unit
- `category_code`: Code of the category the product belongs to
- `merchant_code`: Code of the merchant selling the product
- `catalog_code`: Code of the catalog the product belongs to
- Additional columns for product attributes (based on classification attributes)

### Importing Sample Products

The sample products can be imported using the `SampleProductImporter` utility class. This class reads the CSV file and creates Product entities with the appropriate attributes.

To import the sample products, you can use the `/api/sample-data/import-products` API endpoint provided by the `SampleDataController`.

Example:

```bash
curl -X POST http://localhost:8080/api/sample-data/import-products
```

### Adding New Sample Products

To add new sample products, simply add new rows to the CSV file following the same format. Make sure to:

1. Use unique product_id and product_code values
2. Use valid category_code, merchant_code, and catalog_code values that exist in the system
3. Include appropriate values for the required attributes based on the product category

## Validation Rules

The `validation_rules.csv` and `validation_rule_category_mappings.csv` files contain validation rules for product attributes. These rules are used to validate product data during import and update operations.

### Validation Rules Structure

The validation_rules.csv file has the following columns:

- `rule_id`: Unique identifier for the rule
- `attribute_id`: ID of the attribute being validated
- `rule_type`: Type of validation rule (REQUIRED, LENGTH, PATTERN, RANGE, ENUM)
- `rule_value`: Value used for validation (depends on rule_type)
- `error_message`: Message to display when validation fails

### Validation Rule Category Mappings Structure

The validation_rule_category_mappings.csv file maps validation rules to categories:

- `rule_id`: ID of the validation rule
- `category_code`: Code of the category the rule applies to
- `display_order`: Order to display the attribute in forms
- `required`: Whether the attribute is required for the category
- `filterable`: Whether the attribute can be used for filtering
- `searchable`: Whether the attribute can be searched
- `comparable`: Whether the attribute can be used for comparison
- `visible`: Whether the attribute is visible
- `editable`: Whether the attribute can be edited
