package com.nosql.poc.channel.validation;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationResult {
    private boolean valid = true;
    private List<ValidationError> violations = new ArrayList<>();
}
