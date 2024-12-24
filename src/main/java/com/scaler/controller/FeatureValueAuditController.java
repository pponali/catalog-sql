package com.scaler.controller;

import com.scaler.dto.ApiResponse;
import com.scaler.model.FeatureValueAudit;
import com.scaler.repository.FeatureValueAuditRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/audit")
@Tag(name = "Feature Value Audit API", description = "APIs for querying audit logs")
@Validated
@Slf4j
@RequiredArgsConstructor
public class FeatureValueAuditController {

    private final FeatureValueAuditRepository repository;

    @Operation(summary = "Get audit entries by feature value ID")
    @GetMapping("/value/{valueId}")
    public ResponseEntity<ApiResponse<Page<FeatureValueAudit>>> getAuditsByValue(
            @PathVariable @NotNull Long valueId,
            Pageable pageable) {
        log.info("Fetching audit entries for value id: {}", valueId);
        Page<FeatureValueAudit> audits = repository.findByValueId(valueId, pageable);
        return ResponseEntity.ok(ApiResponse.success(audits));
    }

    @Operation(summary = "Get audit entries by feature ID")
    @GetMapping("/feature/{featureId}")
    public ResponseEntity<ApiResponse<Page<FeatureValueAudit>>> getAuditsByFeature(
            @PathVariable @NotNull Long featureId,
            Pageable pageable) {
        log.info("Fetching audit entries for feature id: {}", featureId);
        Page<FeatureValueAudit> audits = repository.findByFeatureId(featureId, pageable);
        return ResponseEntity.ok(ApiResponse.success(audits));
    }

    @Operation(summary = "Get audit entries by time range")
    @GetMapping("/time-range")
    public ResponseEntity<ApiResponse<Page<FeatureValueAudit>>> getAuditsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Pageable pageable) {
        log.info("Fetching audit entries between {} and {}", start, end);
        Page<FeatureValueAudit> audits = repository.findByTimestampBetween(start, end, pageable);
        return ResponseEntity.ok(ApiResponse.success(audits));
    }

    @Operation(summary = "Get audit entries by action type")
    @GetMapping("/action/{action}")
    public ResponseEntity<ApiResponse<Page<FeatureValueAudit>>> getAuditsByAction(
            @PathVariable FeatureValueAudit.AuditAction action,
            Pageable pageable) {
        log.info("Fetching audit entries for action: {}", action);
        Page<FeatureValueAudit> audits = repository.findByAction(action, pageable);
        return ResponseEntity.ok(ApiResponse.success(audits));
    }

    @Operation(summary = "Get audit summary by feature")
    @GetMapping("/summary/feature/{featureId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAuditSummaryByFeature(
            @PathVariable @NotNull Long featureId) {
        log.info("Fetching audit summary for feature id: {}", featureId);
        List<FeatureValueAudit> audits = repository.findByFeatureId(featureId);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalChanges", audits.size());
        
        Map<FeatureValueAudit.AuditAction, Long> actionCounts = audits.stream()
                .collect(Collectors.groupingBy(FeatureValueAudit::getAction, Collectors.counting()));
        summary.put("actionCounts", actionCounts);
        
        Map<LocalDateTime, Long> changesByDay = audits.stream()
                .collect(Collectors.groupingBy(
                    audit -> audit.getTimestamp().toLocalDate().atStartOfDay(),
                    Collectors.counting()));
        summary.put("changesByDay", changesByDay);
        
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    @Operation(summary = "Get audit entries by complex criteria")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<FeatureValueAudit>>> searchAudits(
            @RequestBody Map<String, Object> criteria,
            Pageable pageable) {
        log.info("Searching audit entries with criteria: {}", criteria);
        
        // Example implementation - extend based on your needs
        Long featureId = (Long) criteria.get("featureId");
        Long valueId = (Long) criteria.get("valueId");
        String action = (String) criteria.get("action");
        String startTime = (String) criteria.get("startTime");
        String endTime = (String) criteria.get("endTime");
        
        // Build dynamic query based on criteria
        Page<FeatureValueAudit> audits;
        if (featureId != null && action != null) {
            audits = repository.findByFeatureIdAndAction(
                featureId, FeatureValueAudit.AuditAction.valueOf(action), pageable);
        } else if (featureId != null) {
            audits = repository.findByFeatureId(featureId, pageable);
        } else if (valueId != null) {
            audits = repository.findByValueId(valueId, pageable);
        } else {
            audits = repository.findAll(pageable);
        }
        
        return ResponseEntity.ok(ApiResponse.success(audits));
    }

    @Operation(summary = "Get audit statistics")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAuditStats() {
        log.info("Fetching audit statistics");
        
        Map<String, Object> stats = new HashMap<>();
        
        // Total number of audit entries
        long totalEntries = repository.count();
        stats.put("totalEntries", totalEntries);
        
        // Count by action type
        Map<FeatureValueAudit.AuditAction, Long> actionCounts = repository.findAll().stream()
                .collect(Collectors.groupingBy(FeatureValueAudit::getAction, Collectors.counting()));
        stats.put("actionCounts", actionCounts);
        
        // Recent activity
        List<FeatureValueAudit> recentAudits = repository.findTop10ByOrderByTimestampDesc();
        stats.put("recentActivity", recentAudits);
        
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
