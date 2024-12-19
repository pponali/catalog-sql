package com.example.service;

import com.example.dto.ClassAttributeAssignmentDTO;
import java.util.List;
import java.util.Set;

public interface ClassAttributeAssignmentService {
    
    /**
     * Create a new class attribute assignment
     */
    ClassAttributeAssignmentDTO createAssignment(ClassAttributeAssignmentDTO dto);
    
    /**
     * Update an existing class attribute assignment
     */
    ClassAttributeAssignmentDTO updateAssignment(Long id, ClassAttributeAssignmentDTO dto);
    
    /**
     * Delete a class attribute assignment
     */
    void deleteAssignment(Long id);
    
    /**
     * Get a class attribute assignment by ID
     */
    ClassAttributeAssignmentDTO getAssignment(Long id);
    
    /**
     * Get all assignments for a classification class
     */
    Set<ClassAttributeAssignmentDTO> getAssignmentsByClassCode(String classCode);
    
    /**
     * Get a specific assignment by class code and attribute code
     */
    ClassAttributeAssignmentDTO getAssignmentByClassAndAttributeCode(String classCode, String attributeCode);
    
    /**
     * Create or update multiple assignments for a classification class
     */
    List<ClassAttributeAssignmentDTO> createOrUpdateAssignments(String classCode, List<ClassAttributeAssignmentDTO> assignments);
    
    /**
     * Validate if a value is valid for a specific assignment
     */
    boolean isValidValue(Long assignmentId, String value);
    
    /**
     * Validate if multiple values are valid for a specific assignment
     */
    boolean areValidValues(Long assignmentId, Set<String> values);
}
