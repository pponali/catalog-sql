package com.nosql.poc.channel.validation.constraints;

import com.nosql.poc.channel.model.ImageSpec;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageSpecValidator implements ConstraintValidator<ValidImageSpec, ImageSpec> {
    
    @Override
    public boolean isValid(ImageSpec spec, ConstraintValidatorContext context) {
        if (spec == null) {
            return true; // Let @NotNull handle null validation
        }
        
        boolean valid = true;
        context.disableDefaultConstraintViolation();
        
        if (spec.getWidth() != null && spec.getWidth() <= 0) {
            context.buildConstraintViolationWithTemplate("Width must be positive")
                .addPropertyNode("width")
                .addConstraintViolation();
            valid = false;
        }
        
        if (spec.getHeight() != null && spec.getHeight() <= 0) {
            context.buildConstraintViolationWithTemplate("Height must be positive")
                .addPropertyNode("height")
                .addConstraintViolation();
            valid = false;
        }
        
        if (spec.getQuality() != null && (spec.getQuality() < 0 || spec.getQuality() > 100)) {
            context.buildConstraintViolationWithTemplate("Quality must be between 0 and 100")
                .addPropertyNode("quality")
                .addConstraintViolation();
            valid = false;
        }
        
        return valid;
    }
}
