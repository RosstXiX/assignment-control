package io.github.rosstxix.assignmentcontrol.infrastructure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    
    public static SecurityUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser user) {
            return user;
        }
        return null;
    }

    public static String getCurrentUsername() {
        SecurityUser user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }

    public static Integer getCurrentEmployeeId() {
        SecurityUser user = getCurrentUser();
        return user != null ? user.getEmployeeId() : null;
    }

    public static Integer getCurrentDepartmentId() {
        SecurityUser user = getCurrentUser();
        return user != null ? user.getDepartmentId() : null;
    }

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
            return authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(roleWithPrefix));
        }
        return false;
    }

    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }
}
