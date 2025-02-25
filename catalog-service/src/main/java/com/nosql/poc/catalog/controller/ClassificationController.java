package com.nosql.poc.catalog.controller;

import com.scaler.exception.ValidationException;
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
        attribute = classificationService.saveAttribute(attribute);
        return new ResponseEntity<>(mapper.toDTO(attribute), HttpStatus.CREATED);
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
        classificationClass = classificationService.saveClass(classificationClass);
        return new ResponseEntity<>(mapper.toDTO(classificationClass), HttpStatus.CREATED);
    }

    @GetMapping("/attributes")
    @Operation(summary = "Get all classification attributes")
    public ResponseEntity<List<ClassificationAttributeDTO>> getAllAttributes() {
        List<ClassificationAttribute> attributes = classificationService.findAllAttributes();
        return ResponseEntity.ok(attributes.stream()
            .map(mapper::toDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/classes")
    @Operation(summary = "Get all classification classes")
    public ResponseEntity<List<ClassificationClassDTO>> getAllClasses() {
        List<ClassificationClass> classes = classificationService.findAllClasses();
        return ResponseEntity.ok(classes.stream()
            .map(mapper::toDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/attributes/{id}")
    @Operation(summary = "Get classification attribute by ID")
    public ResponseEntity<ClassificationAttributeDTO> getAttributeById(@PathVariable UUID id) {
        ClassificationAttribute attribute = classificationService.findAttributeById(id);
        return ResponseEntity.ok(mapper.toDTO(attribute));
    }

    @GetMapping("/classes/{id}")
    @Operation(summary = "Get classification class by ID")
    public ResponseEntity<ClassificationClassDTO> getClassById(@PathVariable UUID id) {
        ClassificationClass classificationClass = classificationService.findClassById(id);
        return ResponseEntity.ok(mapper.toDTO(classificationClass));
    }

    @PutMapping("/attributes/{id}")
    @Operation(summary = "Update a classification attribute")
    public ResponseEntity<ClassificationAttributeDTO> updateAttribute(
            @PathVariable UUID id,
            @Valid @RequestBody ClassificationAttributeDTO attributeDTO) {
        validateAttributeDTO(attributeDTO);

        ClassificationAttribute attribute = mapper.toEntity(attributeDTO);
        attribute = classificationService.updateAttribute(id, attribute);
        return ResponseEntity.ok(mapper.toDTO(attribute));
    }

    @PutMapping("/classes/{id}")
    @Operation(summary = "Update a classification class")
    public ResponseEntity<ClassificationClassDTO> updateClass(
            @PathVariable UUID id,
            @Valid @RequestBody ClassificationClassDTO classDTO) {
        validateClassDTO(classDTO);

        ClassificationClass classificationClass = mapper.toEntity(classDTO);
        classificationClass = classificationService.updateClass(id, classificationClass);
        return ResponseEntity.ok(mapper.toDTO(classificationClass));
    }

    @DeleteMapping("/attributes/{id}")
    @Operation(summary = "Delete a classification attribute")
    public ResponseEntity<Void> deleteAttribute(@PathVariable UUID id) {
        classificationService.deleteAttribute(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/classes/{id}")
    @Operation(summary = "Delete a classification class")
    public ResponseEntity<Void> deleteClass(@PathVariable UUID id) {
        classificationService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }

    private void validateAttributeDTO(ClassificationAttributeDTO attributeDTO) {
        // Add validation logic here
    }

    private void validateClassDTO(ClassificationClassDTO classDTO) {
        // Add validation logic here
    }
}
