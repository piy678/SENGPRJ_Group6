package com.sensprj.leo.entity.enums;

public enum DependencyType {

    PREREQUISITE("Must be completed before", true),

    RECOMMENDED("Recommended before", false),

    RELATED("Related to", false);

    private final String description;
    private final boolean isMandatory;

    DependencyType(String description, boolean isMandatory) {
        this.description = description;
        this.isMandatory = isMandatory;
    }

    public String getDescription() {
        return description;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public boolean requiresReachedStatus() {
        return this == PREREQUISITE;
    }
}
