package com.nosql.poc.copy.controller;

import com.nosql.poc.copy.model.*;
import com.nosql.poc.copy.service.CopyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(/copy")
@RequiredArgsConstructor
public class CopyController {
    
    private final CopyService copyService;
    
    @PostMapping("/products")
    public ResponseEntity<List<CopyTask>> copyProducts(@Valid @RequestBody CopyRequest request) {
        return ResponseEntity.ok(copyService.copyProducts(request));
    }
    
    @GetMapping("/tasks")
    public ResponseEntity<List<CopyTask>> getTasks(
        @RequestParam(required = false) CopyStatus status,
        @RequestParam(required = false) String productId,
        @RequestParam(required = false) String sourceChannelId,
        @RequestParam(required = false) String targetChannelId) {
        
        List<CopyTask> tasks;
        if (status != null) {
            tasks = copyService.getTasksByStatus(status);
        } else if (productId != null) {
            tasks = copyService.getTasksByProduct(productId);
        } else if (sourceChannelId != null) {
            tasks = copyService.getTasksBySourceChannel(sourceChannelId);
        } else if (targetChannelId != null) {
            tasks = copyService.getTasksByTargetChannel(targetChannelId);
        } else {
            tasks = copyService.getTasksByStatus(CopyStatus.PENDING);
        }
        
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<CopyTask> getTask(@PathVariable String taskId) {
        return ResponseEntity.ok(copyService.getTask(taskId));
    }
    
    @PostMapping("/tasks/{taskId}/retry")
    public ResponseEntity<CopyTask> retryTask(@PathVariable String taskId) {
        CopyTask task = copyService.getTask(taskId);
        task.setStatus(CopyStatus.PENDING);
        task.setErrorMessage(null);
        copyService.processCopyTask(task);
        return ResponseEntity.ok(task);
    }
}
