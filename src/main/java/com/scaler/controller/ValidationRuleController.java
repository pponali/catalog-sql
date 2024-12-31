package com.scaler.controller;

import com.scaler.model.ValidationRule;
import com.scaler.repository.ValidationRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validation-rules")
@RequiredArgsConstructor
public class ValidationRuleController {

    private final ValidationRuleRepository repository;

    @GetMapping
    public ResponseEntity<?> getAllRules() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getRuleByCode(@PathVariable String code) {
        return repository.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createRule(@RequestBody ValidationRule rule) {
        return ResponseEntity.ok(repository.save(rule));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateRule(@PathVariable String code, @RequestBody ValidationRule rule) {
        if (!repository.existsByCode(code)) {
            return ResponseEntity.notFound().build();
        }
        rule.setCode(code);
        return ResponseEntity.ok(repository.save(rule));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteRule(@PathVariable String code) {
        if (!repository.existsByCode(code)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteByCode(code);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{code}/priority/{priority}")
    public ResponseEntity<?> updatePriority(@PathVariable String code, @PathVariable int priority) {
        return repository.findByCode(code)
                .map(rule -> {
                    rule.setPriority(priority);
                    return ResponseEntity.ok(repository.save(rule));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{code}/toggle")
    public ResponseEntity<?> toggleRule(@PathVariable String code) {
        return repository.findByCode(code)
                .map(rule -> {
                    rule.setActive(!rule.isActive());
                    return ResponseEntity.ok(repository.save(rule));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{code}/priority")
    public ResponseEntity<?> updatePriority(@PathVariable String code, @RequestBody Integer priority) {
        return repository.findByCode(code)
                .map(rule -> {
                    rule.setPriority(priority);
                    return ResponseEntity.ok(repository.save(rule));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
