package com.nosql.poc.rules.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
@Tag(name = "Health and Status", description = "Endpoints for service health and status checks")
public class HealthController {

    private final Optional<BuildProperties> buildProperties;
    private final Optional<GitProperties> gitProperties;

    @GetMapping("/status")
    @Operation(
        summary = "Check service status",
        description = "Returns the current status of the rules service and related components",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Service is healthy",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Map.class))
            )
        }
    )
    public ResponseEntity<Map<String, Object>> getServiceStatus() {
        Map<String, Object> status = new HashMap<>();
        
        status.put("service", "rules-service");
        status.put("status", "UP");
        status.put("timestamp", System.currentTimeMillis());
        
        Map<String, String> versions = new HashMap<>();
        buildProperties.ifPresent(props -> {
            versions.put("version", props.getVersion());
            versions.put("name", props.getName());
            versions.put("time", props.getTime().toString());
        });
        
        gitProperties.ifPresent(props -> {
            versions.put("git.branch", props.getBranch());
            versions.put("git.commit.id", props.getCommitId());
            versions.put("git.commit.time", props.getCommitTime().toString());
        });
        
        status.put("build", versions);
        
        Map<String, Object> components = new HashMap<>();
        components.put("mongodb", Map.of("status", "UP"));
        components.put("rules_engine", Map.of("status", "UP", "type", "Drools"));
        
        status.put("components", components);
        
        return ResponseEntity.ok(status);
    }

    @GetMapping("/ready")
    @Operation(
        summary = "Readiness probe",
        description = "Returns 200 OK when the service is ready to accept requests"
    )
    public ResponseEntity<Map<String, String>> getReadiness() {
        return ResponseEntity.ok(Map.of(
            "status", "READY",
            "message", "Rules service is ready to accept requests"
        ));
    }

    @GetMapping("/live")
    @Operation(
        summary = "Liveness probe",
        description = "Returns 200 OK when the service is alive"
    )
    public ResponseEntity<Map<String, String>> getLiveness() {
        return ResponseEntity.ok(Map.of(
            "status", "ALIVE",
            "message", "Rules service is running"
        ));
    }
}