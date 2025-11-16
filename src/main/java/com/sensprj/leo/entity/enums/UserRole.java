package com.sensprj.leo.entity.enums;

import java.util.Arrays;
import java.util.List;


public enum UserRole {

    TEACHER("Teacher", List.of(
        "course:create",
        "course:read:own",
        "course:update:own",
        "course:delete:own",
        "leo:create",
        "leo:read:own_course",
        "leo:update:own_course",
        "leo:delete:own_course",
        "leo_dependency:create:own_course",
        "assessment:create",
        "assessment:read:students_in_course",
        "assessment:update",
        "assessment:view_history",
        "suggestion:read:students_in_course",
        "audit_log:read:own_course"
    )),

    STUDENT("Student", List.of(
        "assessment:read:own",
        "assessment:view_history:own",
        "suggestion:read:own",
        "suggestion:dismiss:own",
        "leo:read:available_in_enrolled_courses",
        "course:read:enrolled"
    )),

    ADMIN("Administrator", List.of(
        "user:create",
        "user:read:all",
        "user:update",
        "user:delete",
        "course:read:all",
        "course:delete:any",
        "leo:read:all",
        "leo:delete:any",
        "assessment:read:all",
        "suggestion:read:all",
        "audit_log:read:all",
        "system:config"
    ));

    private final String displayName;
    private final List<String> permissions;

    UserRole(String displayName, List<String> permissions) {
        this.displayName = displayName;
        this.permissions = List.copyOf(permissions);  // immutable
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public boolean canViewAuditLogs() {
        return this == ADMIN;
    }

    public boolean canCreateAssessments() {
        return this == TEACHER;
    }

    public boolean canCreateLeos() {
        return this == TEACHER;
    }

    public boolean canViewOthersProgress() {
        return this == TEACHER || this == ADMIN;
    }

    public static List<UserRole> getAllRoles() {
        return Arrays.asList(UserRole.values());
    }
}
