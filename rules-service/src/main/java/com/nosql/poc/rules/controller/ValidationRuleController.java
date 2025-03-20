package com.nosql.poc.rules.controller;

import com.nosql.poc.rules.dto.ApiResponse;
import com.nosql.poc.rules.model.SimpleValidationRule;
import com.nosql.poc.rules.repository.ValidationRuleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/validation-rules")
@RequiredArgsConstructor
@Tag(name = "Validation Rules", description = "API endpoints for managing validation rules")
public class ValidationRuleController {

    private final ValidationRuleRepository ruleRepository;

    @GetMapping
    @Operation(summary = "Get all validation rules", description = "Retrieve a list of all validation rules")
    public ResponseEntity<List<SimpleValidationRule>> getAllRules() {
        log.info("Getting all validation rules");
        return ResponseEntity.ok(ruleRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get validation rule by ID", description = "Retrieve a validation rule by its unique ID")
    public ResponseEntity<SimpleValidationRule> getRuleById(
            @Parameter(description = "Rule ID", required = true) @PathVariable String id) {
        log.info("Getting validation rule with ID: {}", id);
        return ResponseEntity.ok(ruleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Validation rule not found")));
    }

    @GetMapping("/by-rule-id/{ruleId}")
    @Operation(summary = "Get validation rule by rule ID", description = "Retrieve a validation rule by its business rule ID")
    public ResponseEntity<SimpleValidationRule> getRuleByRuleId(
            @Parameter(description = "Business Rule ID", required = true) @PathVariable String ruleId) {
        log.info("Getting validation rule with rule ID: {}", ruleId);
        return ResponseEntity.ok(ruleRepository.findByRuleId(ruleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Validation rule not found")));
    }

    @GetMapping("/by-entity-type/{entityType}")
    @Operation(summary = "Get validation rules by entity type", description = "Retrieve all validation rules for a specific entity type")
    public ResponseEntity<List<SimpleValidationRule>> getRulesByEntityType(
            @Parameter(description = "Entity type (PRODUCT, PARTNER, etc.)", required = true) @PathVariable String entityType) {
        log.info("Getting validation rules with entity type: {}", entityType);
        return ResponseEntity.ok(ruleRepository.findByEntityType(entityType));
    }

    @GetMapping("/by-category/{category}")
    @Operation(summary = "Get validation rules by category", description = "Retrieve all validation rules for a specific category")
    public ResponseEntity<List<SimpleValidationRule>> getRulesByCategory(
            @Parameter(description = "Business category", required = true) @PathVariable String category) {
        log.info("Getting validation rules with category: {}", category);
        return ResponseEntity.ok(ruleRepository.findByCategory(category));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active validation rules", description = "Retrieve all active validation rules")
    public ResponseEntity<List<SimpleValidationRule>> getActiveRules() {
        log.info("Getting all active validation rules");
        return ResponseEntity.ok(ruleRepository.findByActiveTrue());
    }

    @PostMapping
    @Operation(summary = "Create validation rule", description = "Create a new validation rule")
    public ResponseEntity<ApiResponse<SimpleValidationRule>> createRule(@RequestBody SimpleValidationRule rule) {
        log.info("Creating new validation rule: {}", rule.getName());
        
        // Set timestamps and generate ID if needed
        LocalDateTime now = LocalDateTime.now();
        if (rule.getId() == null) {
            rule.setId(UUID.randomUUID().toString());
        }
        if (rule.getRuleId() == null) {
            rule.setRuleId("VR" + System.currentTimeMillis());
        }
        rule.setCreatedAt(now);
        rule.setUpdatedAt(now);
        
        SimpleValidationRule saved = ruleRepository.save(rule);
        
        ApiResponse<SimpleValidationRule> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Validation rule created successfully");
        response.setData(saved);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update validation rule", description = "Update an existing validation rule by ID")
    public ResponseEntity<ApiResponse<SimpleValidationRule>> updateRule(
            @Parameter(description = "Rule ID", required = true) @PathVariable String id,
            @RequestBody SimpleValidationRule rule) {
        log.info("Updating validation rule with ID: {}", id);
        
        return ruleRepository.findById(id)
                .map(existingRule -> {
                    // Preserve original ID and creation timestamp
                    rule.setId(existingRule.getId());
                    if (existingRule.getCreatedAt() != null) {
                        rule.setCreatedAt(existingRule.getCreatedAt());
                    }
                    rule.setUpdatedAt(LocalDateTime.now());
                    
                    SimpleValidationRule updated = ruleRepository.save(rule);
                    
                    ApiResponse<SimpleValidationRule> response = new ApiResponse<>();
                    response.setSuccess(true);
                    response.setMessage("Validation rule updated successfully");
                    response.setData(updated);
                    
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Validation rule not found"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete validation rule", description = "Delete a validation rule by ID")
    public ResponseEntity<ApiResponse<Void>> deleteRule(
            @Parameter(description = "Rule ID", required = true) @PathVariable String id) {
        log.info("Deleting validation rule with ID: {}", id);
        
        return ruleRepository.findById(id)
                .map(rule -> {
                    ruleRepository.deleteById(id);
                    
                    ApiResponse<Void> response = new ApiResponse<>();
                    response.setSuccess(true);
                    response.setMessage("Validation rule deleted successfully");
                    
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Validation rule not found"));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate rule", description = "Set a validation rule as active")
    public ResponseEntity<ApiResponse<SimpleValidationRule>> activateRule(
            @Parameter(description = "Rule ID", required = true) @PathVariable String id) {
        log.info("Activating validation rule with ID: {}", id);
        
        return ruleRepository.findById(id)
                .map(existingRule -> {
                    existingRule.setActive(true);
                    existingRule.setUpdatedAt(LocalDateTime.now());
                    
                    SimpleValidationRule updated = ruleRepository.save(existingRule);
                    
                    ApiResponse<SimpleValidationRule> response = new ApiResponse<>();
                    response.setSuccess(true);
                    response.setMessage("Validation rule activated successfully");
                    response.setData(updated);
                    
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Validation rule not found"));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate rule", description = "Set a validation rule as inactive")
    public ResponseEntity<ApiResponse<SimpleValidationRule>> deactivateRule(
            @Parameter(description = "Rule ID", required = true) @PathVariable String id) {
        log.info("Deactivating validation rule with ID: {}", id);
        
        return ruleRepository.findById(id)
                .map(existingRule -> {
                    existingRule.setActive(false);
                    existingRule.setUpdatedAt(LocalDateTime.now());
                    
                    SimpleValidationRule updated = ruleRepository.save(existingRule);
                    
                    ApiResponse<SimpleValidationRule> response = new ApiResponse<>();
                    response.setSuccess(true);
                    response.setMessage("Validation rule deactivated successfully");
                    response.setData(updated);
                    
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Validation rule not found"));
    }
}