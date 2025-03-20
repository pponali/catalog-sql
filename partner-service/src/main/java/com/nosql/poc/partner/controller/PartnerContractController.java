package com.nosql.poc.partner.controller;

import com.nosql.poc.partner.dto.ApiResponse;
import com.nosql.poc.partner.model.SimplePartnerContract;
import com.nosql.poc.partner.repository.PartnerContractRepository;
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
@RequestMapping("/partner-contracts")
@RequiredArgsConstructor
@Tag(name = "Partner Contract Management", description = "API endpoints for managing partner contracts")
public class PartnerContractController {

    private final PartnerContractRepository contractRepository;

    @GetMapping
    @Operation(summary = "Get all contracts", description = "Retrieve a list of all partner contracts")
    public ResponseEntity<List<SimplePartnerContract>> getAllContracts() {
        log.info("Getting all partner contracts");
        return ResponseEntity.ok(contractRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get contract by ID", description = "Retrieve a partner contract by its unique ID")
    public ResponseEntity<SimplePartnerContract> getContractById(
            @Parameter(description = "Contract ID", required = true) @PathVariable String id) {
        log.info("Getting contract with ID: {}", id);
        return ResponseEntity.ok(contractRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contract not found")));
    }

    @GetMapping("/by-partner/{partnerId}")
    @Operation(summary = "Get contracts by partner ID", description = "Retrieve all contracts for a specific partner")
    public ResponseEntity<List<SimplePartnerContract>> getContractsByPartnerId(
            @Parameter(description = "Partner ID", required = true) @PathVariable String partnerId) {
        log.info("Getting contracts for partner ID: {}", partnerId);
        return ResponseEntity.ok(contractRepository.findByPartnerId(partnerId));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active contracts", description = "Retrieve all active partner contracts")
    public ResponseEntity<List<SimplePartnerContract>> getActiveContracts() {
        log.info("Getting all active contracts");
        return ResponseEntity.ok(contractRepository.findByStatus("ACTIVE"));
    }

    @GetMapping("/active/by-partner/{partnerId}")
    @Operation(summary = "Get active contracts by partner", description = "Retrieve all active contracts for a specific partner")
    public ResponseEntity<List<SimplePartnerContract>> getActiveContractsByPartnerId(
            @Parameter(description = "Partner ID", required = true) @PathVariable String partnerId) {
        log.info("Getting active contracts for partner ID: {}", partnerId);
        return ResponseEntity.ok(contractRepository.findByPartnerIdAndStatus(partnerId, "ACTIVE"));
    }

    @PostMapping
    @Operation(summary = "Create contract", description = "Create a new partner contract")
    public ResponseEntity<ApiResponse<SimplePartnerContract>> createContract(@RequestBody SimplePartnerContract contract) {
        log.info("Creating new contract for partner ID: {}", contract.getPartnerId());
        
        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        contract.setCreatedAt(now);
        contract.setUpdatedAt(now);
        
        SimplePartnerContract saved = contractRepository.save(contract);
        
        ApiResponse<SimplePartnerContract> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Partner contract created successfully");
        response.setData(saved);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update contract", description = "Update an existing partner contract by ID")
    public ResponseEntity<ApiResponse<SimplePartnerContract>> updateContract(
            @Parameter(description = "Contract ID", required = true) @PathVariable String id,
            @RequestBody SimplePartnerContract contract) {
        log.info("Updating contract with ID: {}", id);
        
        return contractRepository.findById(id)
                .map(existingContract -> {
                    // Preserve original ID and creation timestamp
                    contract.setId(existingContract.getId());
                    if (existingContract.getCreatedAt() != null) {
                        contract.setCreatedAt(existingContract.getCreatedAt());
                    }
                    if (existingContract.getCreatedBy() != null) {
                        contract.setCreatedBy(existingContract.getCreatedBy());
                    }
                    contract.setUpdatedAt(LocalDateTime.now());
                    
                    SimplePartnerContract updated = contractRepository.save(contract);
                    
                    ApiResponse<SimplePartnerContract> response = new ApiResponse<>();
                    response.setSuccess(true);
                    response.setMessage("Partner contract updated successfully");
                    response.setData(updated);
                    
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contract not found"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete contract", description = "Delete a partner contract by ID")
    public ResponseEntity<ApiResponse<Void>> deleteContract(
            @Parameter(description = "Contract ID", required = true) @PathVariable String id) {
        log.info("Deleting contract with ID: {}", id);
        
        return contractRepository.findById(id)
                .map(contract -> {
                    contractRepository.deleteById(id);
                    
                    ApiResponse<Void> response = new ApiResponse<>();
                    response.setSuccess(true);
                    response.setMessage("Partner contract deleted successfully");
                    
                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contract not found"));
    }
}