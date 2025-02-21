package com.nosql.poc.channel.validation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationError {
    private String field;
    private String message;
    private String invalidValue;
}
