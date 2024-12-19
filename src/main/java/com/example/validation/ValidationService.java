package com.example.validation;

import com.example.entity.ClassAttributeAssignment;

public interface ValidationService {
    /**
     * Validate a value against the rules defined in a class attribute assignment
     * @param assignment The class attribute assignment containing validation rules
     * @param value The value to validate
     * @return true if the value is valid, false otherwise
     */
    boolean validateValue(ClassAttributeAssignment assignment, String value);
}
