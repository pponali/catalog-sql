package com.scaler.controller;

import com.scaler.dto.MerchantDTO;
import com.scaler.service.MerchantService;
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
public class MerchantController {
    
    private final MerchantService merchantService;

    @PostMapping
    public ResponseEntity<MerchantDTO> createBusiness(@Valid @RequestBody MerchantDTO MerchantDTO) {
        MerchantDTO createdMerchant = merchantService.createBusiness(MerchantDTO);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdMerchant.getId())
            .toUri();
        return ResponseEntity.created(location).body(createdMerchant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MerchantDTO> updateBusiness(
            @PathVariable UUID id,
            @Valid @RequestBody MerchantDTO MerchantDTO) {
        return ResponseEntity.ok(merchantService.updateBusiness(id, MerchantDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantDTO> getMerchant(@PathVariable UUID id) {
        return ResponseEntity.ok(merchantService.getMerchant(id));
    }

    @GetMapping
    public ResponseEntity<List<MerchantDTO>> getAllBusinesses() {
        return ResponseEntity.ok(merchantService.getAllBusinesses());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable UUID id) {
        merchantService.deleteBusiness(id);
        return ResponseEntity.noContent().build();
    }
}
