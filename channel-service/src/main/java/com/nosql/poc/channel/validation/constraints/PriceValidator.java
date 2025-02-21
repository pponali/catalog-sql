package com.nosql.poc.channel.validation.constraints;

import com.nosql.poc.channel.model.PricingRules;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriceValidator implements ConstraintValidator<ValidPrice, PricingRules> {
    
    @Override
    public boolean isValid(PricingRules rules, ConstraintValidatorContext context) {
        if (rules == null) {
            return true; // Let @NotNull handle null validation
        }
        
        boolean valid = true;
        context.disableDefaultConstraintViolation();
        
        if (rules.getMinPrice() != null && rules.getMaxPrice() != null 
            && rules.getMinPrice() > rules.getMaxPrice()) {
            context.buildConstraintViolationWithTemplate("Minimum price cannot be greater than maximum price")
                .addPropertyNode("minPrice")
                .addConstraintViolation();
            valid = false;
        }
        
        if (rules.getDecimalPlaces() != null && rules.getDecimalPlaces() < 0) {
            context.buildConstraintViolationWithTemplate("Decimal places must be non-negative")
                .addPropertyNode("decimalPlaces")
                .addConstraintViolation();
            valid = false;
        }
        
        return valid;
    }
}
