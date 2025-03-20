package com.nosql.poc.rules.controller;

import com.nosql.poc.rules.dto.ApiResponse;
import com.nosql.poc.rules.model.SimpleBusinessRule;
import com.nosql.poc.rules.repository.BusinessRuleRepository;
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
@RequestMapping("/business-rules")
@RequiredArgsConstructor
@Tag(name = "Business Rules", description = "API endpoints for managing business rules")
public class BusinessRuleController {

    private final BusinessRuleRepository ruleRepository;

    @GetMapping
    @Operation(summary = "Get all business rules", description = "Retrieve a list of all business rules")
    public ResponseEntity<List<SimpleBusinessRule>> getAllRules() {
        log.info("Getting all business rules");
        return ResponseEntity.ok(ruleRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get business rule by ID", description = "Retrieve a business rule by its unique ID")
    public ResponseEntity<SimpleBusinessRule> getRuleById(
            @Parameter(description = "Rule ID", required = true) @PathVariable String id) {
        log.info("Getting business rule with ID: {}", id);
        return ResponseEntity.ok(ruleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Business rule not found")));
    }

    @GetMapping("/by-rule-id/{ruleId}")
    @Operation(summary = "Get business rule by rule ID", description = "Retrieve a business rule by its business rule ID")
    public ResponseEntity<SimpleBusinessRule> getRuleByRuleId(
            @Parameter(description = "Business Rule ID", required = true) @PathVariable String ruleId) {
        log.info("Getting business rule with rule ID: {}", ruleId);
        return ResponseEntity.ok(ruleRepository.findByRuleId(ruleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Business rule not found")));
    }

    @GetMapping("/by-rule-type/{ruleType}")
    @Operation(summary = "Get business rules by rule type", description = "Retrieve all business rules of a specific type")
    public ResponseEntity<List<SimpleBusinessRule>> getRulesByRuleType(
            @Parameter(description = "Rule type (DISCOUNT, PROMOTION, etc.)", required = true) @PathVariable String ruleType) {
        log.info("Getting business rules with rule type: {}", ruleType);
        return ResponseEntity.ok(ruleRepository.findByRuleType(ruleType));
    }

    @GetMapping("/by-entity-type/{entityType}")
    @Operation(summary = "Get business rules by entity type", description = "Retrieve all business rules for a specific entity type")
    public ResponseEntity<List<SimpleBusinessRule>> getRulesByEntityType(
            @Parameter(description = "Entity type (PRODUCT, ORDER, etc.)", required = true) @PathVariable String entityType) {
        log.info("Getting business rules with entity type: {}", entityType);
        return ResponseEntity.ok(ruleRepository.findByEntityType(entityType));
    }

    @GetMapping("/by-category/{category}")
    @Operation(summary = "Get business rules by category", description = "Retrieve all business rules for a specific category")
    public ResponseEntity<List<SimpleBusinessRule>> getRulesByCategory(
            @Parameter(description = "Business category", required = true) @PathVariable String category) {
        log.info("Getting business rules with category: {}", category);
        return ResponseEntity.ok(ruleRepository.findByCategory(category));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active business rules", description = "Retrieve all active business rules")
    public ResponseEntity<List<SimpleBusinessRule>> getActiveRules() {
        log.info("Getting all active business rules");
        return ResponseEntity.ok(ruleRepository.findByActiveTrue());
    }

    @GetMapping("/active-now")
    @Operation(summary = "Get currently active business rules", description = "Retrieve all business rules active at the current date/time")
    public ResponseEntity<List<SimpleBusinessRule>> getCurrentlyActiveRules() {
        log.info("Getting all business rules active at the current time");
        LocalDateTime now = LocalDateTime.now();
        return ResponseEntity.ok(ruleRepository.findByStartDateBeforeAndEndDateAfterAndActiveTrue(now, now));
    }

    @PostMapping
    @Operation(summary = "Create business rule", description = "Create a new business rule")
    public ResponseEntity<ApiResponse<SimpleBusinessRule>> createRule(@RequestBody SimpleBusinessRule rule) {
        log.info("Creating new business rule: {}", rule.getName());
        
        // Set timestamps and generate ID if needed
        LocalDateTime now = LocalDateTime.now();
        if (rule.getId() == null) {
            rule.setId(UUID.randomUUID().toString());
        }
        if (rule.getRuleId() == null) {
            rule.setRuleId("BR" + System.currentTimeMillis());
        }
        if (rule.getStartDate() == null) {
            rule.setStartDate(now);
        }
        if (rule.getEndDate() == null) {
            rule.setEndDate(now.plusYears(1));
        }
        rule.setCreatedAt(now);
        rule.setUpdatedAt(now);
        
        SimpleBusinessRule saved = ruleRepository.save(rule);
        
        ApiResponse<SimpleBusinessRule> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Business rule created successfully");
        response.setData(saved);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update business rule", description = "Update an existing business rule by ID")
    public ResponseEntity<ApiResponse<SimpleBusinessRule>> updateRule(
            @Parameter(description = "Rule ID", required = true) @PathVariable String id,
            @RequestBody SimpleBusinessRule rule) {
        log.info("Updating business rule with ID: {}", id);
        
        return ruleRepository.findById(id)
                .map(existingRule -> {
                    // Preserve original ID and creation timestamp
                    rule.setId(existingRule.getId());
                    if (existingRule.getCreatedAt() != null) {
                        rule.setCreatedAt(existingRule.getCreatedAt());
                    }
                    rule.setUpdatedAt(LocalDateTime.now());
                    
                    SimpleBusinessRule updated = ruleRepository.save(rule);
                    
                    ApiResponse<SimpleBusinessRule> response = new ApiResponse<>();
                    response.setSuccess(true);
                    response.setMessage("Business rule updated successfully");
                    response.setData(updated);
                    
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Business rule not found"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete business rule", description = "Delete a business rule by ID")
    public ResponseEntity<ApiResponse<Void>> deleteRule(
            @Parameter(description = "Rule ID", required = true) @PathVariable String id) {
        log.info("Deleting business rule with ID: {}", id);
        
        return ruleRepository.findById(id)
                .map(rule -> {
                    ruleRepository.deleteById(id);
                    
                    ApiResponse<Void> response = new ApiResponse<>();
                    response.setSuccess(true);
                    response.setMessage("Business rule deleted successfully");
                    
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Business rule not found"));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate rule", description = "Set a business rule as active")
    public ResponseEntity<ApiResponse<SimpleBusinessRule>> activateRule(
            @Parameter(description = "Rule ID", required = true) @PathVariable String id) {
        log.info("Activating business rule with ID: {}", id);
        
        return ruleRepository.findById(id)
                .map(existingRule -> {
                    existingRule.setActive(true);
                    existingRule.setUpdatedAt(LocalDateTime.now());
                    
                    SimpleBusinessRule updated = ruleRepository.save(existingRule);
                    
                    ApiResponse<SimpleBusinessRule> response = new ApiResponse<>();
                    response.setSuccess(true);
                    response.setMessage("Business rule activated successfully");
                    response.setData(updated);
                    
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Business rule not found"));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate rule", description = "Set a business rule as inactive")
    public ResponseEntity<ApiResponse<SimpleBusinessRule>> deactivateRule(
            @Parameter(description = "Rule ID", required = true) @PathVariable String id) {
        log.info("Deactivating business rule with ID: {}", id);
        
        return ruleRepository.findById(id)
                .map(existingRule -> {
                    existingRule.setActive(false);
                    existingRule.setUpdatedAt(LocalDateTime.now());
                    
                    SimpleBusinessRule updated = ruleRepository.save(existingRule);
                    
                    ApiResponse<SimpleBusinessRule> response = new ApiResponse<>();
                    response.setSuccess(true);
                    response.setMessage("Business rule deactivated successfully");
                    response.setData(updated);
                    
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Business rule not found"));
    }
}