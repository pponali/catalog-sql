package com.scaler.controller;

import com.scaler.dto.SiteCatalogDTO;
import com.scaler.service.StoreCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/site-catalogs")
@RequiredArgsConstructor
public class StoreCatalogController {
    
    private final StoreCatalogService siteCatalogService;

    @PostMapping("/sites/{siteId}/catalogs/{catalogId}")
    public ResponseEntity<SiteCatalogDTO> assignCatalogToSite(
            @PathVariable UUID siteId,
            @PathVariable UUID catalogId,
            @RequestParam(defaultValue = "false") boolean isDefault) {
        return ResponseEntity.ok(siteCatalogService.assignCatalogToSite(siteId, catalogId, isDefault));
    }

    @DeleteMapping("/sites/{siteId}/catalogs/{catalogId}")
    public ResponseEntity<Void> removeCatalogFromSite(
            @PathVariable UUID siteId,
            @PathVariable UUID catalogId) {
        siteCatalogService.removeCatalogFromSite(siteId, catalogId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sites/{siteId}/catalogs")
    public ResponseEntity<List<SiteCatalogDTO>> getCatalogsBySite(@PathVariable UUID siteId) {
        return ResponseEntity.ok(siteCatalogService.getCatalogsBySite(siteId));
    }

    @GetMapping("/sites/{siteId}/catalogs/default")
    public ResponseEntity<SiteCatalogDTO> getDefaultCatalog(@PathVariable UUID siteId) {
        return ResponseEntity.ok(siteCatalogService.getDefaultCatalog(siteId));
    }

    @PutMapping("/sites/{siteId}/catalogs/{catalogId}/default")
    public ResponseEntity<Void> setDefaultCatalog(
            @PathVariable UUID siteId,
            @PathVariable UUID catalogId) {
        siteCatalogService.setDefaultCatalog(siteId, catalogId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sites/{siteId}/catalogs/{catalogId}/validate")
    public ResponseEntity<Boolean> validateCatalogAccess(
            @PathVariable UUID siteId,
            @PathVariable UUID catalogId) {
        return ResponseEntity.ok(siteCatalogService.validateCatalogAccess(siteId, catalogId));
    }
}
