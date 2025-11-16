package com.sensprj.leo.entity.enums;

public enum AssessmentStatus {

    REACHED("Reached", "✓", 4),

    PARTIALLY_REACHED("Partially Reached", "◐", 2),

    NOT_REACHED("Not Reached", "✗", 1),

    UNMARKED("Unmarked", "○", 0);

    private final String displayName;
    private final String symbol;
    private final int level;  // for sorting/comparison

    AssessmentStatus(String displayName, String symbol, int level) {
        this.displayName = displayName;
        this.symbol = symbol;
        this.level = level;
    }


    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getLevel() {
        return level;
    }

    public boolean satisfiesPrerequisites() {
        return this == REACHED;
    }

    public boolean isDowngradeTo(AssessmentStatus newStatus) {
        return this.level > newStatus.level;
    }

    public boolean isUpgradeTo(AssessmentStatus newStatus) {
        return this.level < newStatus.level;
    }
}
