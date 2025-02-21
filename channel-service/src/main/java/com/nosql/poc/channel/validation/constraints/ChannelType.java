package com.nosql.poc.channel.validation.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ChannelTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChannelType {
    String message() default "Invalid channel type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
