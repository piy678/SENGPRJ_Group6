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
