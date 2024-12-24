package com.scaler.controller;

import com.scaler.dto.ApiResponse;
import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.model.CacheStats;
import com.scaler.model.FeatureValueEvent;
import com.scaler.model.MonitoringMetrics;
import com.scaler.service.ProductFeatureValueService;
import com.scaler.service.cache.FeatureValueCacheService;
import com.scaler.service.event.FeatureValueEventService;
import com.scaler.service.monitoring.FeatureValueMonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/feature-values")
@Tag(name = "Product Feature Value API", description = "APIs for managing product feature values")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ProductFeatureValueController {

    private final ProductFeatureValueService service;
    private final FeatureValueCacheService cacheService;
    private final FeatureValueEventService eventService;
    private final FeatureValueMonitoringService monitoringService;

    @Operation(summary = "Create a new feature value")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Feature value created successfully",
            content = @Content(schema = @Schema(implementation = ProductFeatureValueDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid input"
        )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ProductFeatureValueDTO>> createFeatureValue(
            @Valid @RequestBody ProductFeatureValueDTO dto) {
        log.info("Creating feature value: {}", dto);
        ProductFeatureValueDTO created = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Feature value created successfully"));
    }

    @Operation(summary = "Get feature value by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductFeatureValueDTO> getById(@PathVariable Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update feature value")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductFeatureValueDTO>> updateFeatureValue(
            @PathVariable @NotNull Long id,
            @Valid @RequestBody ProductFeatureValueDTO dto) {
        log.info("Updating feature value with id: {}", id);
        dto.setId(id);
        ProductFeatureValueDTO updated = service.update(dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Feature value updated successfully"));
    }

    @Operation(summary = "Delete feature value")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFeatureValue(
            @PathVariable @NotNull Long id) {
        log.info("Deleting feature value with id: {}", id);
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Feature value deleted successfully"));
    }

    @Operation(summary = "Get feature values by feature ID")
    @GetMapping("/feature/{featureId}")
    public ResponseEntity<ApiResponse<Page<ProductFeatureValueDTO>>> getFeatureValuesByFeature(
            @PathVariable @NotNull Long featureId,
            Pageable pageable) {
        log.info("Fetching feature values for feature id: {}", featureId);
        Page<ProductFeatureValueDTO> values = service.findByFeature(featureId, pageable);
        return ResponseEntity.ok(ApiResponse.success(values));
    }

    @Operation(summary = "Get value trends")
    @GetMapping("/trends/{featureId}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getValueTrends(
            @PathVariable @NotNull Long featureId,
            @RequestParam(defaultValue = "daily") String interval) {
        log.info("Fetching value trends for feature id: {} with interval: {}", featureId, interval);
        List<Map<String, Object>> trends = service.getValueTrends(featureId, interval);
        return ResponseEntity.ok(ApiResponse.success(trends));
    }

    @Operation(summary = "Bulk update feature values")
    @PutMapping("/bulk/{featureId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> bulkUpdateValues(
            @PathVariable @NotNull Long featureId,
            @RequestBody Map<String, Object> updates) {
        log.info("Performing bulk update for feature id: {}", featureId);
        Map<String, Object> results = service.bulkUpdateValues(featureId, updates);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @Operation(summary = "Get cache statistics")
    @GetMapping("/monitoring/cache-stats")
    public ResponseEntity<ApiResponse<Map<String, CacheStats>>> getCacheStats() {
        log.info("Fetching cache statistics");
        Map<String, CacheStats> stats = cacheService.getAllCacheStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @Operation(summary = "Get event history")
    @GetMapping("/monitoring/events/{featureId}")
    public ResponseEntity<ApiResponse<List<FeatureValueEvent>>> getEventHistory(
            @PathVariable @NotNull Long featureId,
            @RequestParam FeatureValueEvent.EventType eventType) {
        log.info("Fetching event history for feature id: {} and event type: {}", featureId, eventType);
        List<FeatureValueEvent> events = eventService.getEventHistory(featureId, eventType);
        return ResponseEntity.ok(ApiResponse.success(events));
    }

    @Operation(summary = "Get monitoring metrics")
    @GetMapping("/monitoring/metrics")
    public ResponseEntity<ApiResponse<MonitoringMetrics>> getMonitoringMetrics() {
        log.info("Fetching monitoring metrics");
        MonitoringMetrics metrics = monitoringService.getMetrics();
        return ResponseEntity.ok(ApiResponse.success(metrics));
    }

    @Operation(summary = "Clear cache")
    @PostMapping("/monitoring/cache/clear")
    public ResponseEntity<ApiResponse<Void>> clearCache() {
        log.info("Clearing cache");
        cacheService.evictAllCachedValues();
        return ResponseEntity.ok(ApiResponse.success(null, "Cache cleared successfully"));
    }

    @Operation(summary = "Reset monitoring metrics")
    @PostMapping("/monitoring/metrics/reset")
    public ResponseEntity<ApiResponse<Void>> resetMetrics() {
        log.info("Resetting monitoring metrics");
        monitoringService.resetMetrics();
        return ResponseEntity.ok(ApiResponse.success(null, "Metrics reset successfully"));
    }
}
