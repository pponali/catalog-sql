package com.scaler.controller;

import com.scaler.dto.ClassificationAttributeDTO;
import com.scaler.dto.ClassificationClassDTO;
import com.scaler.entity.ClassificationAttribute;
import com.scaler.entity.ClassificationClass;
import com.scaler.exception.ValidationException;
import com.scaler.mapper.ClassificationMapper;
import com.scaler.service.ClassificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/classifications")
@RequiredArgsConstructor
@Tag(name = "Classification Management", description = "APIs for managing classification attributes and classes")
public class ClassificationController {

    private final ClassificationService classificationService;
    private final ClassificationMapper mapper;

    @PostMapping("/attributes")
    @Operation(summary = "Create a new classification attribute")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Attribute created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Attribute already exists")
    })
    public ResponseEntity<ClassificationAttributeDTO> createAttribute(
            @Valid @RequestBody ClassificationAttributeDTO attributeDTO) {
        String operationId = UUID.randomUUID().toString();
        log.info("Operation ID: {} - Creating new classification attribute: {}", operationId, attributeDTO);
        
        validateAttributeDTO(attributeDTO);
        
        ClassificationAttribute attribute = mapper.toEntity(attributeDTO);
        ClassificationAttribute saved = classificationService.saveAttribute(attribute);
        
        ClassificationAttributeDTO result = mapper.toDTO(saved);
        log.info("Operation ID: {} - Successfully created classification attribute with ID: {}", 
            operationId, result.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/attributes/{id}")
    @Operation(summary = "Get classification attribute by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attribute found"),
        @ApiResponse(responseCode = "404", description = "Attribute not found")
    })
    public ResponseEntity<ClassificationAttributeDTO> getAttribute(@PathVariable Long id) {
        String operationId = UUID.randomUUID().toString();
        log.debug("Operation ID: {} - Fetching classification attribute with ID: {}", operationId, id);
        
        ClassificationAttribute attribute = classificationService.findAttributeById(id);
        ClassificationAttributeDTO result = mapper.toDTO(attribute);
        
        log.debug("Operation ID: {} - Successfully retrieved classification attribute", operationId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/attributes")
    @Operation(summary = "Get all classification attributes")
    public ResponseEntity<List<ClassificationAttributeDTO>> getAllAttributes() {
        String operationId = UUID.randomUUID().toString();
        log.debug("Operation ID: {} - Fetching all classification attributes", operationId);
        
        List<ClassificationAttributeDTO> attributes = classificationService.getAllAttributes().stream()
            .map(mapper::toDTO)
            .collect(Collectors.toList());
        
        log.debug("Operation ID: {} - Successfully retrieved {} classification attributes", 
            operationId, attributes.size());
        return ResponseEntity.ok(attributes);
    }

    @GetMapping("/classes")
    @Operation(summary = "Get all classification classes")
    public ResponseEntity<List<ClassificationClassDTO>> getAllClasses() {
        String operationId = UUID.randomUUID().toString();
        log.debug("Operation ID: {} - Fetching all classification classes", operationId);
        
        List<ClassificationClassDTO> classes = classificationService.getAllClasses().stream()
            .map(mapper::toDTO)
            .collect(Collectors.toList());
        
        log.debug("Operation ID: {} - Successfully retrieved {} classification classes", 
            operationId, classes.size());
        return ResponseEntity.ok(classes);
    }

    @PostMapping("/classes")
    @Operation(summary = "Create a new classification class")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Class created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Class already exists")
    })
    public ResponseEntity<ClassificationClassDTO> createClass(
            @Valid @RequestBody ClassificationClassDTO classDTO) {
        String operationId = UUID.randomUUID().toString();
        log.info("Operation ID: {} - Creating new classification class: {}", operationId, classDTO);
        
        validateClassDTO(classDTO);
        
        ClassificationClass classificationClass = mapper.toEntity(classDTO);
        ClassificationClass saved = classificationService.saveClass(classificationClass);
        
        ClassificationClassDTO result = mapper.toDTO(saved);
        log.info("Operation ID: {} - Successfully created classification class with ID: {}", 
            operationId, result.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/classes/{id}")
    @Operation(summary = "Get classification class by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Class found"),
        @ApiResponse(responseCode = "404", description = "Class not found")
    })
    public ResponseEntity<ClassificationClassDTO> getClass(@PathVariable Long id) {
        String operationId = UUID.randomUUID().toString();
        log.debug("Operation ID: {} - Fetching classification class with ID: {}", operationId, id);
        
        ClassificationClass classificationClass = classificationService.findClassById(id);
        ClassificationClassDTO result = mapper.toDTO(classificationClass);
        
        log.debug("Operation ID: {} - Successfully retrieved classification class", operationId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/attributes/{id}")
    @Operation(summary = "Delete classification attribute")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Attribute deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Attribute not found")
    })
    public ResponseEntity<Void> deleteAttribute(@PathVariable Long id) {
        String operationId = UUID.randomUUID().toString();
        log.info("Operation ID: {} - Deleting classification attribute with ID: {}", operationId, id);
        
        classificationService.deleteAttributeById(id);
        
        log.info("Operation ID: {} - Successfully deleted classification attribute", operationId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/classes/{id}")
    @Operation(summary = "Delete classification class")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Class deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Class not found")
    })
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        String operationId = UUID.randomUUID().toString();
        log.info("Operation ID: {} - Deleting classification class with ID: {}", operationId, id);
        
        classificationService.deleteClassById(id);
        
        log.info("Operation ID: {} - Successfully deleted classification class", operationId);
        return ResponseEntity.noContent().build();
    }

    private void validateAttributeDTO(ClassificationAttributeDTO dto) {
        if (dto == null) {
            throw new ValidationException("Classification attribute cannot be null");
        }
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new ValidationException("Classification attribute code is required");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new ValidationException("Classification attribute name is required");
        }
    }

    private void validateClassDTO(ClassificationClassDTO dto) {
        if (dto == null) {
            throw new ValidationException("Classification class cannot be null");
        }
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new ValidationException("Classification class code is required");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new ValidationException("Classification class name is required");
        }
    }
}
