package com.nosql.poc.validation.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validate {
    String type() default "BASIC";
    
    String channelId() default "";
    
    boolean[] validateParams() default {};
    
    boolean validateReturn() default false;
}
