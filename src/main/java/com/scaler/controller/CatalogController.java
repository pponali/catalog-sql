package com.scaler.controller;

import com.scaler.dto.CatalogDTO;
import com.scaler.service.CatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalogs")
@RequiredArgsConstructor
public class CatalogController {
    
    private final CatalogService catalogService;

    @PostMapping
    public ResponseEntity<CatalogDTO> createCatalog(@Valid @RequestBody CatalogDTO catalogDTO) {
        CatalogDTO createdCatalog = catalogService.createCatalog(catalogDTO);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdCatalog.getId())
            .toUri();
        return ResponseEntity.created(location).body(createdCatalog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogDTO> updateCatalog(
            @PathVariable UUID id,
            @Valid @RequestBody CatalogDTO catalogDTO) {
        return ResponseEntity.ok(catalogService.updateCatalog(id, catalogDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogDTO> getCatalog(@PathVariable UUID id) {
        return ResponseEntity.ok(catalogService.getCatalog(id));
    }

    @GetMapping
    public ResponseEntity<List<CatalogDTO>> getAllCatalogs(
            @RequestParam(required = false) UUID businessId) {
        if (businessId != null) {
            return ResponseEntity.ok(catalogService.getCatalogsByBusiness(businessId));
        }
        return ResponseEntity.ok(catalogService.getAllCatalogs());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalog(@PathVariable UUID id) {
        catalogService.deleteCatalog(id);
        return ResponseEntity.noContent().build();
    }
}
