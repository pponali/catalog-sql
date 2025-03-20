package com.nosql.poc.vendor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for service health and status checks.
 */
@RestController
@RequestMapping("/status")
public class HealthController {

    /**
     * Simple endpoint to verify the service is running.
     */
    @GetMapping
    public Map<String, String> getStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "vendor-service");
        status.put("version", "1.0.0");
        return status;
    }
}