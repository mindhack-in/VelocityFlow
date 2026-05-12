package com.velocity_flow.api.security.aspect;

import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.security.permission.PermissionEvaluatorService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class PermissionAspect {

    private final PermissionEvaluatorService permissionEvaluatorService;

    public PermissionAspect(PermissionEvaluatorService permissionEvaluatorService) {
        this.permissionEvaluatorService = permissionEvaluatorService;
    }

    @Before("@annotation(com.velocity_flow.api.security.annotation.RequirePermission) || @within(com.velocity_flow.api.security.annotation.RequirePermission)")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // Check method level annotation first (using AnnotatedElementUtils supports meta-annotations)
        RequirePermission requirePermission = org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation(method, RequirePermission.class);
        
        // Fallback to class level annotation if not present on method
        if (requirePermission == null) {
            requirePermission = org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation(joinPoint.getTarget().getClass(), RequirePermission.class);
        }

        if (requirePermission != null) {
            String[] requiredPermissions = requirePermission.value();
            
            if (!permissionEvaluatorService.hasPermission(requiredPermissions)) {
                throw new AccessDeniedException("You do not have the required permissions to perform this action.");
            }
        }
    }
}
