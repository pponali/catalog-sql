package com.example.controller;

import com.example.dto.ClassificationAttributeDTO;
import com.example.dto.ClassificationClassDTO;
import com.example.entity.ClassificationAttribute;
import com.example.entity.ClassificationClass;
import com.example.service.ClassificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/classifications")
@RequiredArgsConstructor
@Tag(name = "Classification Management", description = "APIs for managing classification attributes and classes")
public class ClassificationController {

    private final ClassificationService classificationService;

    @PostMapping("/attributes")
    @Operation(summary = "Create a new classification attribute")
    public ResponseEntity<ClassificationAttributeDTO> createAttribute(@RequestBody ClassificationAttributeDTO attributeDTO) {
        ClassificationAttribute attribute = new ClassificationAttribute();
        attribute.setCode(attributeDTO.getCode());
        attribute.setName(attributeDTO.getName());
        ClassificationAttribute saved = classificationService.saveAttribute(attribute);
        attributeDTO.setId(saved.getId());
        return ResponseEntity.ok(attributeDTO);
    }

    @GetMapping("/attributes/{id}")
    @Operation(summary = "Get classification attribute by ID")
    public ResponseEntity<ClassificationAttributeDTO> getAttribute(@PathVariable Long id) {
        ClassificationAttribute attribute = classificationService.findAttributeById(id);
        ClassificationAttributeDTO dto = new ClassificationAttributeDTO();
        dto.setId(attribute.getId());
        dto.setCode(attribute.getCode());
        dto.setName(attribute.getName());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/attributes")
    @Operation(summary = "Get all classification attributes")
    public ResponseEntity<List<ClassificationAttributeDTO>> getAllAttributes() {
        List<ClassificationAttributeDTO> attributes = classificationService.getAllAttributes().stream()
            .map(attribute -> {
                ClassificationAttributeDTO dto = new ClassificationAttributeDTO();
                dto.setId(attribute.getId());
                dto.setCode(attribute.getCode());
                dto.setName(attribute.getName());
                return dto;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(attributes);
    }

    @GetMapping("/classes")
    @Operation(summary = "Get all classification classes")
    public ResponseEntity<List<ClassificationClassDTO>> getAllClasses() {
        List<ClassificationClassDTO> classes = classificationService.getAllClasses().stream()
            .map(classificationClass -> {
                ClassificationClassDTO dto = new ClassificationClassDTO();
                dto.setId(classificationClass.getId());
                dto.setCode(classificationClass.getCode());
                dto.setName(classificationClass.getName());
                return dto;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(classes);
    }

    @PostMapping("/classes")
    @Operation(summary = "Create a new classification class")
    public ResponseEntity<ClassificationClassDTO> createClass(@RequestBody ClassificationClassDTO classDTO) {
        ClassificationClass classificationClass = new ClassificationClass();
        classificationClass.setCode(classDTO.getCode());
        classificationClass.setName(classDTO.getName());
        ClassificationClass saved = classificationService.saveClass(classificationClass);
        classDTO.setId(saved.getId());
        return ResponseEntity.ok(classDTO);
    }

    @GetMapping("/classes/{id}")
    @Operation(summary = "Get classification class by ID")
    public ResponseEntity<ClassificationClassDTO> getClass(@PathVariable Long id) {
        ClassificationClass classificationClass = classificationService.findClassById(id);
        ClassificationClassDTO dto = new ClassificationClassDTO();
        dto.setId(classificationClass.getId());
        dto.setCode(classificationClass.getCode());
        dto.setName(classificationClass.getName());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/attributes/{id}")
    @Operation(summary = "Delete classification attribute")
    public ResponseEntity<Void> deleteAttribute(@PathVariable Long id) {
        classificationService.deleteAttributeById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/classes/{id}")
    @Operation(summary = "Delete classification class")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        classificationService.deleteClassById(id);
        return ResponseEntity.ok().build();
    }
}
