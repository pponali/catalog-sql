package com.nosql.poc.partner.controller;

import com.nosql.poc.partner.dto.ApiResponse;
import com.nosql.poc.partner.model.SimplePartner;
import com.nosql.poc.partner.repository.PartnerRepository;
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

@Slf4j
@RestController
@RequestMapping("/partners")
@RequiredArgsConstructor
@Tag(name = "Partner Management", description = "API endpoints for managing partners")
public class PartnerController {

    private final PartnerRepository partnerRepository;

    @GetMapping
    @Operation(summary = "Get all partners", description = "Retrieve a list of all partners")
    public ResponseEntity<List<SimplePartner>> getAllPartners() {
        log.info("Getting all partners");
        return ResponseEntity.ok(partnerRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get partner by ID", description = "Retrieve a partner by their unique ID")
    public ResponseEntity<SimplePartner> getPartnerById(
            @Parameter(description = "Partner ID", required = true) @PathVariable String id) {
        log.info("Getting partner with ID: {}", id);
        return ResponseEntity.ok(partnerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found")));
    }

    @GetMapping("/by-partner-id/{partnerId}")
    @Operation(summary = "Get partner by partner ID", description = "Retrieve a partner by their business partner ID")
    public ResponseEntity<SimplePartner> getPartnerByPartnerId(
            @Parameter(description = "Business Partner ID", required = true) @PathVariable String partnerId) {
        log.info("Getting partner with partner ID: {}", partnerId);
        return ResponseEntity.ok(partnerRepository.findByPartnerId(partnerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found")));
    }

    @GetMapping("/by-type/{type}")
    @Operation(summary = "Get partners by type", description = "Retrieve all partners of a specific type")
    public ResponseEntity<List<SimplePartner>> getPartnersByType(
            @Parameter(description = "Partner type (SUPPLIER, DISTRIBUTOR, etc.)", required = true) @PathVariable String type) {
        log.info("Getting partners with type: {}", type);
        return ResponseEntity.ok(partnerRepository.findByType(type));
    }

    @GetMapping("/by-category/{category}")
    @Operation(summary = "Get partners by category", description = "Retrieve all partners in a specific category")
    public ResponseEntity<List<SimplePartner>> getPartnersByCategory(
            @Parameter(description = "Partner category", required = true) @PathVariable String category) {
        log.info("Getting partners with category: {}", category);
        return ResponseEntity.ok(partnerRepository.findByCategory(category));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active partners", description = "Retrieve all active partners")
    public ResponseEntity<List<SimplePartner>> getActivePartners() {
        log.info("Getting all active partners");
        return ResponseEntity.ok(partnerRepository.findActivePartners());
    }

    @GetMapping("/active/by-type/{type}")
    @Operation(summary = "Get active partners by type", description = "Retrieve all active partners of a specific type")
    public ResponseEntity<List<SimplePartner>> getActivePartnersByType(
            @Parameter(description = "Partner type", required = true) @PathVariable String type) {
        log.info("Getting active partners with type: {}", type);
        return ResponseEntity.ok(partnerRepository.findActivePartnersByType(type));
    }

    @PostMapping
    @Operation(summary = "Create partner", description = "Create a new partner")
    public ResponseEntity<ApiResponse<SimplePartner>> createPartner(@RequestBody SimplePartner partner) {
        log.info("Creating new partner: {}", partner.getName());
        
        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        partner.setCreatedAt(now);
        partner.setUpdatedAt(now);
        
        SimplePartner saved = partnerRepository.save(partner);
        
        ApiResponse<SimplePartner> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Partner created successfully");
        response.setData(saved);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update partner", description = "Update an existing partner by ID")
    public ResponseEntity<ApiResponse<SimplePartner>> updatePartner(
            @Parameter(description = "Partner ID", required = true) @PathVariable String id,
            @RequestBody SimplePartner partner) {
        log.info("Updating partner with ID: {}", id);
        
        return partnerRepository.findById(id)
                .map(existingPartner -> {
                    // Preserve original ID and creation timestamp
                    partner.setId(existingPartner.getId());
                    if (existingPartner.getCreatedAt() != null) {
                        partner.setCreatedAt(existingPartner.getCreatedAt());
                    }
                    partner.setUpdatedAt(LocalDateTime.now());
                    
                    SimplePartner updated = partnerRepository.save(partner);
                    
                    ApiResponse<SimplePartner> response = new ApiResponse<>();
                    response.setSuccess(true);
                    response.setMessage("Partner updated successfully");
                    response.setData(updated);
                    
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete partner", description = "Delete a partner by ID")
    public ResponseEntity<ApiResponse<Void>> deletePartner(
            @Parameter(description = "Partner ID", required = true) @PathVariable String id) {
        log.info("Deleting partner with ID: {}", id);
        
        return partnerRepository.findById(id)
                .map(partner -> {
                    partnerRepository.deleteById(id);
                    
                    ApiResponse<Void> response = new ApiResponse<>();
                    response.setSuccess(true);
                    response.setMessage("Partner deleted successfully");
                    
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));
    }
}