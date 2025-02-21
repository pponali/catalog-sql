package com.nosql.poc.channel.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PriceValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrice {
    String message() default "Invalid price configuration";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
