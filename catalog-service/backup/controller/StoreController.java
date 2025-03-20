package com.scaler.controller;

import com.scaler.dto.StoreDTO;
import com.scaler.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreDTO> createStore(@Valid @RequestBody StoreDTO storeDTO) {
        StoreDTO createdStore = storeService.createStore(storeDTO);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdStore.getId())
            .toUri();
        return ResponseEntity.created(location).body(createdStore);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreDTO> updateSite(
            @PathVariable UUID id,
            @Valid @RequestBody StoreDTO storeDTO) {
        return ResponseEntity.ok(storeService.updateStore(id, storeDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDTO> getSite(@PathVariable UUID id) {
        return ResponseEntity.ok(storeService.getSite(id));
    }

    @GetMapping
    public ResponseEntity<List<StoreDTO>> getAllSites(
            @RequestParam(required = false) UUID businessId) {
        if (businessId != null) {
            return ResponseEntity.ok(storeService.getSitesByBusiness(businessId));
        }
        return ResponseEntity.ok(storeService.getAllSites());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable UUID id) {
        storeService.deleteSite(id);
        return ResponseEntity.noContent().build();
    }
}
