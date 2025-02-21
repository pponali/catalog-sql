package com.nosql.poc.channel.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageSpecValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImageSpec {
    String message() default "Invalid image specification";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
