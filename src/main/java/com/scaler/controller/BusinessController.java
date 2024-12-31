package com.scaler.controller;

import com.scaler.dto.BusinessDTO;
import com.scaler.service.BusinessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/businesses")
@RequiredArgsConstructor
public class BusinessController {
    
    private final BusinessService businessService;

    @PostMapping
    public ResponseEntity<BusinessDTO> createBusiness(@Valid @RequestBody BusinessDTO businessDTO) {
        BusinessDTO createdBusiness = businessService.createBusiness(businessDTO);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdBusiness.getId())
            .toUri();
        return ResponseEntity.created(location).body(createdBusiness);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessDTO> updateBusiness(
            @PathVariable UUID id,
            @Valid @RequestBody BusinessDTO businessDTO) {
        return ResponseEntity.ok(businessService.updateBusiness(id, businessDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessDTO> getBusiness(@PathVariable UUID id) {
        return ResponseEntity.ok(businessService.getBusiness(id));
    }

    @GetMapping
    public ResponseEntity<List<BusinessDTO>> getAllBusinesses() {
        return ResponseEntity.ok(businessService.getAllBusinesses());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable UUID id) {
        businessService.deleteBusiness(id);
        return ResponseEntity.noContent().build();
    }
}
