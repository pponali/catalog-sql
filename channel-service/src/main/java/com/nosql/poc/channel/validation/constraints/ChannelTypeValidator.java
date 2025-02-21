package com.nosql.poc.channel.validation.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ChannelTypeValidator implements ConstraintValidator<ChannelType, String> {
    
    private static final Set<String> VALID_CHANNEL_TYPES = new HashSet<>(Arrays.asList(
        "MARKETPLACE",
        "QUICK_COMMERCE",
        "POS",
        "MOBILE_APP",
        "SOCIAL_COMMERCE",
        "PHYSICAL_STORE",
        "E_COMMERCE"
    ));
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull handle null validation
        }
        return VALID_CHANNEL_TYPES.contains(value.toUpperCase());
    }
}
