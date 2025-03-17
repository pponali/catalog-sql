package com.scaler.controller;

import com.scaler.util.ValidationRuleUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for updating validation rules based on classification attributes
 */
@Slf4j
@RestController
@RequestMapping("/api/validation-rules")
@RequiredArgsConstructor
public class ValidationRuleUpdateController {

    private final ValidationRuleUpdater validationRuleUpdater;

    /**
     * Update validation rules based on classification attributes
     * 
     * @param classificationAttributesPath Path to the classification attributes CSV file (optional)
     * @param validationRulesPath Path to the validation rules CSV file (optional)
     * @return Response with update statistics
     */
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateValidationRules(
            @RequestParam(value = "classificationAttributesPath", required = false) String classificationAttributesPath,
            @RequestParam(value = "validationRulesPath", required = false) String validationRulesPath) {
        
        log.info("Received request to update validation rules");
        
        // Use default paths if not provided
        String attributesPath = classificationAttributesPath != null ? 
                classificationAttributesPath : "DevMDD_Classification_Attributes.csv";
        
        String rulesPath = validationRulesPath != null ? 
                validationRulesPath : "src/main/resources/csv/validation_rules.csv";
        
        try {
            int updatedCount = validationRuleUpdater.updateValidationRules(attributesPath, rulesPath);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Validation rules updated successfully");
            response.put("updatedCount", updatedCount);
            response.put("classificationAttributesPath", attributesPath);
            response.put("validationRulesPath", rulesPath);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error updating validation rules", e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error updating validation rules: " + e.getMessage());
            response.put("classificationAttributesPath", attributesPath);
            response.put("validationRulesPath", rulesPath);
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get information about the validation rule update process
     * 
     * @return Response with information about the validation rule update process
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getValidationRuleUpdateInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("description", "This endpoint allows updating validation rules based on classification attributes");
        info.put("usage", "POST /api/validation-rules/update");
        info.put("parameters", new String[] {
            "classificationAttributesPath (optional): Path to the classification attributes CSV file",
            "validationRulesPath (optional): Path to the validation rules CSV file"
        });
        info.put("defaultClassificationAttributesPath", "DevMDD_Classification_Attributes.csv");
        info.put("defaultValidationRulesPath", "src/main/resources/csv/validation_rules.csv");
        
        return ResponseEntity.ok(info);
    }
}
