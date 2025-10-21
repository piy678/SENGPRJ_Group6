# language: en
Feature: Prerequisite satisfaction unlocks dependent LEOs
  Defines how the system handles prerequisite relationships between LEOs.

  Background:
    Given LEO "Fractions" exists
    And LEO "Mixed Numbers" exists
    And "Fractions" is a prerequisite of "Mixed Numbers"
    And student "Alice" is enrolled in "Mathematics 101"

  @functional @domain @dependencies
  Scenario: Completing a prerequisite unlocks the dependent LEO
    Given student "Alice" has not reached "Mixed Numbers"
    When the teacher marks "Fractions" as "Reached" for "Alice"
    Then "Mixed Numbers" becomes "Available" for "Alice"
    And the system logs "Prerequisite satisfied"

  @edge-case @dependencies
  Scenario: Circular dependencies are rejected
    Given LEO "A" exists and LEO "B" exists
    And "B" is already a prerequisite of "A"
    When the teacher attempts to set "A" as a prerequisite of "B"
    Then the system rejects the relationship with error "Circular dependency detected"

  @edge-case @dependencies
  Scenario: No downgrade of already reached prerequisites
    Given "Fractions" is "Reached" for student "Alice"
    When the teacher marks "Mixed Numbers" as "Partially Reached"
    Then "Fractions" remains "Reached"

  @edge-case @dependencies
  Scenario: Idempotent recalculation of dependency graph
    Given the dependency graph for course "Mathematics 101" exists
    When the system recalculates dependency statuses twice
    Then the resulting statuses are identical in both runs
