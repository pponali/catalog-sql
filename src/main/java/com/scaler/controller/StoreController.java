package com.scaler.controller;

import com.scaler.dto.SiteDTO;
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
@RequestMapping("/api/v1/sites")
@RequiredArgsConstructor
public class StoreController {
    
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<SiteDTO> createSite(@Valid @RequestBody SiteDTO siteDTO) {
        SiteDTO createdSite = storeService.createSite(siteDTO);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdSite.getId())
            .toUri();
        return ResponseEntity.created(location).body(createdSite);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SiteDTO> updateSite(
            @PathVariable UUID id,
            @Valid @RequestBody SiteDTO siteDTO) {
        return ResponseEntity.ok(storeService.updateSite(id, siteDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SiteDTO> getSite(@PathVariable UUID id) {
        return ResponseEntity.ok(storeService.getSite(id));
    }

    @GetMapping
    public ResponseEntity<List<SiteDTO>> getAllSites(
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
