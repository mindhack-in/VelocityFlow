package com.velocity_flow.api.security.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(
    method = RequestMethod.POST, 
    produces = MediaType.APPLICATION_JSON_VALUE, 
    consumes = MediaType.APPLICATION_JSON_VALUE
)
@RequirePermission(value = {})
public @interface PostMappingWithPermission {

    @AliasFor(annotation = RequestMapping.class)
    String name() default "";

    @AliasFor(annotation = RequestMapping.class)
    String[] value() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] path() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] params() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] headers() default {};

    /**
     * The required permissions to access this endpoint.
     * Aliases to {@link RequirePermission#value()}.
     */
    @AliasFor(annotation = RequirePermission.class, attribute = "value")
    String[] permissions() default {};
}
