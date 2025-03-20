package com.nosql.poc.validation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.nosql.poc.validation.ValidationService;
import com.nosql.poc.validation.model.EnhancedValidationRule;
import com.nosql.poc.validation.model.SimpleValidationRule;
import com.nosql.poc.validation.model.ValidationResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/validation")
@RequiredArgsConstructor
@Tag(name = "Validation Controller", description = "API for validating products and categories using simple and enhanced validation rules")
public class ValidationController {

    private final ValidationService validationService;

    @Operation(summary = "Validate entity using simple rules", 
             description = "Validates an entity against basic validation rules")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entity validated",
                     content = @Content(schema = @Schema(implementation = ValidationResult.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/entity/{entityType}")
    public ResponseEntity<ValidationResult> validateEntity(
            @Parameter(description = "Type of entity to validate (PRODUCT, CATEGORY, etc.)")
            @PathVariable String entityType, 
            @Parameter(description = "Entity data to validate")
            @RequestBody JsonNode entity) {
        log.info("Validating {} entity", entityType);
        ValidationResult result = validationService.validateEntity(entityType, entity);
        return ResponseEntity.ok(result);
    }
    
    @Operation(summary = "Validate entity using enhanced rules", 
             description = "Validates an entity against enhanced validation rules with cross-field validation, conditionals, etc.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entity validated",
                     content = @Content(schema = @Schema(implementation = ValidationResult.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/entity/{entityType}/enhanced")
    public ResponseEntity<ValidationResult> validateEntityEnhanced(
            @Parameter(description = "Type of entity to validate (PRODUCT, CATEGORY, etc.)")
            @PathVariable String entityType, 
            @Parameter(description = "Entity data to validate")
            @RequestBody JsonNode entity) {
        log.info("Enhanced validation for {} entity", entityType);
        ValidationResult result = validationService.validateEntityEnhanced(entityType, entity);
        return ResponseEntity.ok(result);
    }
    
    @Operation(summary = "Validate product using simple rules",
             description = "Validates a product against basic validation rules")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product validated",
                     content = @Content(schema = @Schema(implementation = ValidationResult.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/product")
    public ResponseEntity<ValidationResult> validateProduct(
            @Parameter(description = "Product data to validate")
            @RequestBody Map<String, Object> productData) {
        log.info("Validating product");
        ValidationResult result = validationService.validateProduct(productData);
        return ResponseEntity.ok(result);
    }
    
    @Operation(summary = "Validate product using enhanced rules",
             description = "Validates a product against enhanced validation rules with cross-field validation, conditionals, etc.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product validated",
                     content = @Content(schema = @Schema(implementation = ValidationResult.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/product/enhanced")
    public ResponseEntity<ValidationResult> validateProductEnhanced(
            @Parameter(description = "Product data to validate")
            @RequestBody Map<String, Object> productData) {
        log.info("Enhanced validation for product");
        ValidationResult result = validationService.validateProductEnhanced(productData);
        return ResponseEntity.ok(result);
    }
    
    @Operation(summary = "Validate category using simple rules",
             description = "Validates a category against basic validation rules")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category validated",
                     content = @Content(schema = @Schema(implementation = ValidationResult.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/category")
    public ResponseEntity<ValidationResult> validateCategory(
            @Parameter(description = "Category data to validate")
            @RequestBody Map<String, Object> categoryData) {
        log.info("Validating category");
        ValidationResult result = validationService.validateCategory(categoryData);
        return ResponseEntity.ok(result);
    }
    
    @Operation(summary = "Validate category using enhanced rules",
             description = "Validates a category against enhanced validation rules with cross-field validation, conditionals, etc.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category validated",
                     content = @Content(schema = @Schema(implementation = ValidationResult.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/category/enhanced")
    public ResponseEntity<ValidationResult> validateCategoryEnhanced(
            @Parameter(description = "Category data to validate")
            @RequestBody Map<String, Object> categoryData) {
        log.info("Enhanced validation for category");
        ValidationResult result = validationService.validateCategoryEnhanced(categoryData);
        return ResponseEntity.ok(result);
    }
    
    @Operation(summary = "Get all simple validation rules",
             description = "Returns all simple validation rules in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rules retrieved successfully",
                     content = @Content(schema = @Schema(implementation = SimpleValidationRule.class)))
    })
    @GetMapping("/rules")
    public ResponseEntity<List<SimpleValidationRule>> getAllRules() {
        return ResponseEntity.ok(validationService.getAllRules());
    }
    
    @Operation(summary = "Get all enhanced validation rules",
             description = "Returns all enhanced validation rules in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rules retrieved successfully",
                     content = @Content(schema = @Schema(implementation = EnhancedValidationRule.class)))
    })
    @GetMapping("/rules/enhanced")
    public ResponseEntity<List<EnhancedValidationRule>> getAllEnhancedRules() {
        return ResponseEntity.ok(validationService.getAllEnhancedRules());
    }
    
    @Operation(summary = "Get simple validation rules by entity type",
             description = "Returns simple validation rules for a specific entity type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rules retrieved successfully",
                     content = @Content(schema = @Schema(implementation = SimpleValidationRule.class)))
    })
    @GetMapping("/rules/entity/{entityType}")
    public ResponseEntity<List<SimpleValidationRule>> getRulesByEntityType(
            @Parameter(description = "Entity type (PRODUCT, CATEGORY, etc.)")
            @PathVariable String entityType) {
        return ResponseEntity.ok(validationService.getRulesByEntityType(entityType));
    }
    
    @Operation(summary = "Get enhanced validation rules by entity type",
             description = "Returns enhanced validation rules for a specific entity type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rules retrieved successfully",
                     content = @Content(schema = @Schema(implementation = EnhancedValidationRule.class)))
    })
    @GetMapping("/rules/enhanced/entity/{entityType}")
    public ResponseEntity<List<EnhancedValidationRule>> getEnhancedRulesByEntityType(
            @Parameter(description = "Entity type (PRODUCT, CATEGORY, etc.)")
            @PathVariable String entityType) {
        return ResponseEntity.ok(validationService.getEnhancedRulesByEntityType(entityType));
    }
    
    @Operation(summary = "Get active simple validation rules",
             description = "Returns all active simple validation rules")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rules retrieved successfully",
                     content = @Content(schema = @Schema(implementation = SimpleValidationRule.class)))
    })
    @GetMapping("/rules/active")
    public ResponseEntity<List<SimpleValidationRule>> getActiveRules() {
        return ResponseEntity.ok(validationService.getActiveRules());
    }
    
    @Operation(summary = "Get active enhanced validation rules",
             description = "Returns all active enhanced validation rules")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rules retrieved successfully",
                     content = @Content(schema = @Schema(implementation = EnhancedValidationRule.class)))
    })
    @GetMapping("/rules/enhanced/active")
    public ResponseEntity<List<EnhancedValidationRule>> getActiveEnhancedRules() {
        return ResponseEntity.ok(validationService.getActiveEnhancedRules());
    }
    
    @Operation(summary = "Create sample validation rules",
             description = "Creates sample enhanced validation rules for products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sample rules created successfully",
                     content = @Content(schema = @Schema(implementation = EnhancedValidationRule.class)))
    })
    @PostMapping("/rules/sample/create")
    public ResponseEntity<List<EnhancedValidationRule>> createSampleRules() {
        return ResponseEntity.ok(validationService.createSampleProductRules());
    }
}