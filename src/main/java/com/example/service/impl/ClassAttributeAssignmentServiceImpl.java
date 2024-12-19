package com.example.service.impl;

import com.example.dto.ClassAttributeAssignmentDTO;
import com.example.entity.ClassAttributeAssignment;
import com.example.entity.ClassificationAttribute;
import com.example.entity.ClassificationClass;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.ClassAttributeAssignmentMapper;
import com.example.repository.ClassAttributeAssignmentRepository;
import com.example.repository.ClassificationAttributeRepository;
import com.example.repository.ClassificationClassRepository;
import com.example.service.ClassAttributeAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassAttributeAssignmentServiceImpl implements ClassAttributeAssignmentService {

    private final ClassAttributeAssignmentRepository assignmentRepository;
    private final ClassificationClassRepository classRepository;
    private final ClassificationAttributeRepository attributeRepository;
    private final ClassAttributeAssignmentMapper mapper;

    @Override
    public ClassAttributeAssignmentDTO createAssignment(ClassAttributeAssignmentDTO dto) {
        ClassificationClass classificationClass = classRepository.findByCode(dto.getClassificationClass())
            .orElseThrow(() -> new ResourceNotFoundException("ClassificationClass not found: " + dto.getClassificationClass()));
            
        ClassificationAttribute attribute = attributeRepository.findByCode(dto.getClassificationAttribute())
            .orElseThrow(() -> new ResourceNotFoundException("ClassificationAttribute not found: " + dto.getClassificationAttribute()));
            
        if (assignmentRepository.existsByClassificationClassAndClassificationAttribute(classificationClass, attribute)) {
            throw new IllegalStateException("Assignment already exists for this class and attribute");
        }
        
        ClassAttributeAssignment assignment = mapper.toEntity(dto);
        assignment.setClassificationClass(classificationClass);
        assignment.setClassificationAttribute(attribute);
        
        return mapper.toDTO(assignmentRepository.save(assignment));
    }

    @Override
    public ClassAttributeAssignmentDTO updateAssignment(Long id, ClassAttributeAssignmentDTO dto) {
        ClassAttributeAssignment assignment = assignmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ClassAttributeAssignment not found: " + id));
            
        mapper.updateAssignmentFromDTO(dto, assignment);
        return mapper.toDTO(assignmentRepository.save(assignment));
    }

    @Override
    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("ClassAttributeAssignment not found: " + id);
        }
        assignmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ClassAttributeAssignmentDTO getAssignment(Long id) {
        return assignmentRepository.findById(id)
            .map(mapper::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("ClassAttributeAssignment not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ClassAttributeAssignmentDTO> getAssignmentsByClassCode(String classCode) {
        return assignmentRepository.findByClassificationClassCode(classCode)
            .stream()
            .map(mapper::toDTO)
            .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public ClassAttributeAssignmentDTO getAssignmentByClassAndAttributeCode(String classCode, String attributeCode) {
        return assignmentRepository.findByClassCodeAndAttributeCode(classCode, attributeCode)
            .map(mapper::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Assignment not found for class %s and attribute %s", classCode, attributeCode)));
    }

    @Override
    public List<ClassAttributeAssignmentDTO> createOrUpdateAssignments(String classCode, List<ClassAttributeAssignmentDTO> assignments) {
        ClassificationClass classificationClass = classRepository.findByCode(classCode)
            .orElseThrow(() -> new ResourceNotFoundException("ClassificationClass not found: " + classCode));
            
        List<ClassAttributeAssignmentDTO> results = new ArrayList<>();
        
        for (ClassAttributeAssignmentDTO dto : assignments) {
            dto.setClassificationClass(classCode);
            ClassificationAttribute attribute = attributeRepository.findByCode(dto.getClassificationAttribute())
                .orElseThrow(() -> new ResourceNotFoundException("ClassificationAttribute not found: " + dto.getClassificationAttribute()));
                
            assignmentRepository.findByClassificationClassAndClassificationAttribute(classificationClass, attribute)
                .ifPresentOrElse(
                    existing -> {
                        mapper.updateAssignmentFromDTO(dto, existing);
                        results.add(mapper.toDTO(assignmentRepository.save(existing)));
                    },
                    () -> {
                        ClassAttributeAssignment newAssignment = mapper.toEntity(dto);
                        newAssignment.setClassificationClass(classificationClass);
                        newAssignment.setClassificationAttribute(attribute);
                        results.add(mapper.toDTO(assignmentRepository.save(newAssignment)));
                    }
                );
        }
        
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValidValue(Long assignmentId, String value) {
        ClassAttributeAssignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new ResourceNotFoundException("ClassAttributeAssignment not found: " + assignmentId));
            
        if ("enum".equalsIgnoreCase(assignment.getAttributeType())) {
            return assignment.getAttributeValues().contains(value);
        }
        
        return switch (assignment.getAttributeType().toLowerCase()) {
            case "string" -> true;
            case "numeric" -> value.matches("^-?\\d*\\.?\\d+$");
            case "boolean" -> value.matches("^(true|false)$");
            default -> false;
        };
    }

    @Override
    @Transactional(readOnly = true)
    public boolean areValidValues(Long assignmentId, Set<String> values) {
        ClassAttributeAssignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new ResourceNotFoundException("ClassAttributeAssignment not found: " + assignmentId));
            
        if (!assignment.isMultiValued() && values.size() > 1) {
            return false;
        }
        
        return values.stream().allMatch(value -> isValidValue(assignmentId, value));
    }
}
