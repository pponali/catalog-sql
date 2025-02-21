# Validation Service

This module provides centralized validation services for the catalog-nosql-poc project. It contains reusable validation components that can be used across different services.

## Components

### Core Components

1. `ValidationEngine` - Core validation engine that executes validation rules
2. `ValidationRuleRegistry` - Registry for managing validation rules

### Models

1. `ValidationContext` - Context information for validation execution
2. `ValidationError` - Represents a validation error
3. `ValidationResult` - Contains validation results including errors and warnings
4. `ValidationSeverity` - Enum for validation severity levels
5. `ValidationWarning` - Represents a validation warning

### Services

1. `ProductValidationService` - Product-specific validation service containing validation logic for products, prices, and inventory

### Aspects

1. `ValidationAspect` - AOP aspect for declarative validation using annotations

### Annotations

1. `@Validate` - Annotation for declarative validation

## Usage

To use the validation service in your module:

1. Add the validation-service dependency to your module's pom.xml
2. Autowire the appropriate validation service (e.g., `ProductValidationService`)
3. Use the validation methods as needed

Example:

```java
@Service
@RequiredArgsConstructor
public class YourService {
    private final ProductValidationService validationService;
    
    public void validateProduct(Product product) {
        ValidationResult result = validationService.performBasicValidation(product);
        if (result.hasErrors()) {
            throw new ValidationException("Validation failed: " + result.getErrors());
        }
    }
}
```

## Adding New Validation Rules

To add new validation rules:

1. Create a new class that implements the `ValidationRule` interface
2. Implement the validation logic in the `validate` method
3. Register the rule with the `ValidationRuleRegistry`

Example:

```java
@Component
public class YourValidationRule implements ValidationRule {
    @Override
    public void validate(Object target, ValidationContext context, ValidationResult result) {
        // Your validation logic here
    }
    
    @Override
    public boolean supports(Object target, ValidationContext context) {
        return target instanceof YourClass;
    }
}
```
