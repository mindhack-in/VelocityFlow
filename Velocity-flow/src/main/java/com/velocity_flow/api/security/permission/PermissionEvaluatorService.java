package com.velocity_flow.api.security.permission;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionEvaluatorService {

    /**
     * Checks if the current authenticated user has at least one of the required permissions.
     * @param requiredPermissions The permissions defined on the accessed resource.
     * @return true if the user has the required permission, false otherwise.
     */
    public boolean hasPermission(String[] requiredPermissions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            return false;
        }

        Set<String> userPermissions = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        for(int i =0;i<requiredPermissions.length;i++)
        	System.out.println(requiredPermissions[i]);
        
        System.out.println(userPermissions);
        // Return true if any of the required permissions match the user's permissions
        return Arrays.stream(requiredPermissions)
                .anyMatch(userPermissions::contains);
    }
}
