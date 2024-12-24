package com.scaler.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FeatureValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFeatureValue {
    String message() default "Invalid feature value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String valueType() default "";
}
