package com.velocity_flow.api.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to enforce permission-based authorization on methods.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    
    /**
     * The permission key(s) required to access the annotated method/class.
     * If multiple are provided, the user must possess at least one of them.
     */
    String[] value();
}
