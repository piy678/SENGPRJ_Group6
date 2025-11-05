# language: en

Feature: Next LEO suggestions with prioritization
  As a student
  I want to receive personalized, prioritized suggestions for next LEOs to complete
  So that I follow an optimal learning path based on my progress and readiness

  Background:
    Given student "Alice" is authenticated with role "Student"
    And student "Alice" is enrolled in course "Software Engineering Project"

  @domain @recommendations @student @smoke
  Scenario: Suggest dependents when prerequisites are reached with rationale
    Given "Alice" has completed "Anforderungsanalyse verstehen" with status "Reached" (on 2025-10-15)
    And "Alice" has completed "Gherkin-Spezifikationen erstellen" with status "Reached" (on 2025-10-18)
    And LEO "Test-Driven Development anwenden" requires "Gherkin-Spezifikationen erstellen"
    And LEO "Low-Fidelity Prototypen entwickeln" requires "Anforderungsanalyse verstehen"
    When Alice opens the "Suggested Next LEOs" panel
    Then the system suggests "Test-Driven Development anwenden" with:
      | Field | Value |
      | Rationale | Prerequisite 'Gherkin-Spezifikationen erstellen' completed on 2025-10-18 |
      | Ready | ✓ Ready to start |
      | Color | Green highlight |
    And the system suggests "Low-Fidelity Prototypen entwickeln" with:
      | Field | Value |
      | Rationale | Prerequisite 'Anforderungsanalyse verstehen' completed on 2025-10-15 |
      | Ready | ✓ Ready to start |
      | Color | Green highlight |

  @domain @recommendations @student @edge-case
  Scenario: Do not suggest LEOs with unmet prerequisites
    Given "Alice" has completed "Anforderungsanalyse verstehen" with status "Reached"
    And LEO "Backend mit Spring Boot implementieren" requires both:
      | Prerequisite |
      | Test-Driven Development anwenden |
      | Gherkin-Spezifikationen erstellen |
    And "Alice" has status "Not Reached" for "Test-Driven Development anwenden"
    And "Alice" has status "Not Reached" for "Gherkin-Spezifikationen erstellen"
    When Alice opens the "Suggested Next LEOs" panel
    Then "Backend mit Spring Boot implementieren" does NOT appear in suggestions
    And the system shows message:
      | Text | "This LEO requires prerequisites: Test-Driven Development anwenden, Gherkin-Spezifikationen erstellen" |
      | Tip | "Complete prerequisites first" |

  @domain @recommendations @student @edge-case
  Scenario: Exclude already mastered LEOs from suggestions
    Given "Alice" has completed "Test-Driven Development anwenden" with status "Reached"
    And LEO "Test-Driven Development anwenden" appears in "Alice's" previous suggestions
    When Alice opens the "Suggested Next LEOs" panel
    Then "Test-Driven Development anwenden" does NOT appear in suggestions
    And only incomplete LEOs with met prerequisites are suggested
    And a message shows "You've already mastered this LEO"

  @domain @recommendations @student @regression
  Scenario: Suggestions update dynamically when prerequisites are completed
    Given LEO "Integration Testing" requires:
      | Prerequisite |
      | Test-Driven Development anwenden |
      | Backend mit Spring Boot implementieren |
    And "Alice" has completed "Test-Driven Development anwenden"
    And "Alice" has status "Not Reached" for "Backend mit Spring Boot implementieren"
    And "Integration Testing" currently does NOT appear in suggestions
    When Alice completes "Backend mit Spring Boot implementieren"
    And Alice refreshes the "Suggested Next LEOs" panel
    Then "Integration Testing" immediately appears in suggestions
    And the rationale includes both completed prerequisites:
      | Prerequisite | CompletedOn |
      | Test-Driven Development anwenden | 2025-10-18 |
      | Backend mit Spring Boot implementieren | 2025-10-21 |

  @domain @recommendations @student @regression
  Scenario: No suggestions shown when all prerequisites are incomplete
    Given LEO "Advanced Topics" requires 5 prerequisite LEOs
    And "Alice" has completed 0 of these prerequisites
    When Alice opens the "Suggested Next LEOs" panel
    Then "Advanced Topics" does NOT appear
    And the system suggests starting with foundational LEOs:
      | Recommended | Prerequisite |
      | Yes | Anforderungsanalyse verstehen |
      | Yes | Gherkin-Spezifikationen erstellen |
    And a message encourages "Start with these fundamentals"

  @ui @recommendations @student @regression
  Scenario: Suggested LEOs are prioritized by readiness level and completion order
    Given "Alice" has completed these prerequisites:
      | LEO | CompletedOn | Status |
      | Anforderungsanalyse verstehen | 2025-10-10 | Reached |
      | Gherkin-Spezifikationen erstellen | 2025-10-15 | Reached |
    And available dependent LEOs:
      | LEO | Prerequisites | PrereqMet | Priority |
      | LEO-A | [Gherkin-Spezifikationen erstellen] | 1/1 | High |
      | LEO-B | [Gherkin-Spezifikationen erstellen, Anforderungsanalyse verstehen] | 2/2 | Highest |
      | LEO-C | [Anforderungsanalyse verstehen] | 1/1 | Medium |
    When Alice views suggestions panel
    Then LEOs appear in priority order:
      | Position | LEO | Prerequisites Met | Reason |
      | 1 | LEO-B | 2/2 ✓ | All prerequisites met - most ready |
      | 2 | LEO-A | 1/1 ✓ | Most recent prerequisite (2025-10-15) |
      | 3 | LEO-C | 1/1 ✓ | Older prerequisite (2025-10-10) |
    And each suggestion shows progress bar (100% for all)
    And suggestion "LEO-B" has badge "Most Ready"
