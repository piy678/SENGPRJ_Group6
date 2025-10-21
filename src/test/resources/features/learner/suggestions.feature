# language: en
Feature: Suggesting next LEOs
  Recommends the next Learning Elements based on completed prerequisites.

  Background:
    Given learner "Alice" is enrolled in "Mathematics 101"

  @functional @recommendations
  Scenario: Suggest dependents when prerequisites are reached
    Given learner "Alice" has mastered "Fractions"
    And "Mixed Numbers" requires "Fractions"
    When the learner opens the "Suggestions" panel
    Then the system suggests "Mixed Numbers" with rationale "Prerequisite completed"

  @functional @recommendations
  Scenario: Do not suggest LEOs with unmet prerequisites
    Given "Advanced Algebra" requires "Basic Algebra"
    And "Basic Algebra" is not yet reached for "Alice"
    When the learner opens the "Suggestions" panel
    Then "Advanced Algebra" is not suggested

  @functional @recommendations
  Scenario: Do not suggest already mastered LEOs
    Given learner "Alice" has already mastered "Decimals"
    When the learner opens "Suggestions"
    Then "Decimals" is not included in suggestions
