# language: en

Feature: Prerequisite satisfaction unlocks dependent LEOs
  As a teacher
  I want prerequisites to unlock dependent LEOs when completed
  So that students receive appropriate next-step recommendations and system maintains graph integrity

  Rule: Completing prerequisites makes dependents available but does not auto-complete them

    Background:
      Given a teacher "Dr. Mueller" is authenticated with role "Teacher"
      And course "Software Engineering Project" exists
      And course "Software Engineering Project" has teacher "Dr. Mueller"
      And student "Alice" is enrolled in "Software Engineering Project"

    @domain @dependencies @teacher @smoke
    Scenario: Completing a prerequisite unlocks dependent LEO with notification
      Given LEO "Anforderungsanalyse verstehen" exists in course
      And LEO "Gherkin-Spezifikationen erstellen" depends on "Anforderungsanalyse verstehen"
      And student "Alice" has status "Not Reached" for "Gherkin-Spezifikationen erstellen"
      When teacher "Dr. Mueller" records assessment "Reached" for "Anforderungsanalyse verstehen" for "Alice"
      Then "Gherkin-Spezifikationen erstehen" is marked "Available" (unlocked) for "Alice"
      And a suggestion for "Gherkin-Spezifikationen erstellen" appears in "Alice's" recommendations
      And the rationale shows "Prerequisite 'Anforderungsanalyse verstehen' completed on 2025-10-21"
      And an audit log entry records this prerequisite satisfaction

    @domain @dependencies @teacher @edge-case
    Scenario: Circular dependency is rejected with clear error message
      Given LEO "A" exists in course
      And LEO "B" exists in course
      And "A" is already an indirect prerequisite of "B"
      When teacher "Dr. Mueller" attempts to set "B" as a prerequisite of "A"
      Then the system rejects the relationship with error "Circular dependency detected"
      And the error explains "Setting B→A would create cycle: A→...→B→A"
      And no relationship between "B" and "A" is created
      And an audit log entry records this failed attempt with reason "CIRCULAR_DEPENDENCY_VIOLATION"

    @domain @dependencies @teacher @edge-case
    Scenario: No downgrade of already reached prerequisites
      Given LEO "Test-Driven Development anwenden" requires LEO "Gherkin-Spezifikationen erstellen"
      And student "Carol" has status "Reached" for "Gherkin-Spezifikationen erstellen" (recorded on 2025-10-15)
      And student "Carol" has status "Partially Reached" for "Test-Driven Development anwenden" (recorded on 2025-10-18)
      When teacher "Dr. Mueller" marks "Test-Driven Development anwenden" as "Reached" for "Carol"
      Then "Gherkin-Spezifikationen erstellen" status remains "Reached" (NOT downgraded)
      And cascade logic does NOT affect prerequisites that are already reached
      And an audit log shows this cascade protection with reason "PREREQUISITE_ALREADY_REACHED"

    @domain @dependencies @teacher @edge-case
    Scenario: Idempotent recomputation of dependencies (long chain)
      Given a dependency chain exists:
        | LEO | Depends On | Status |
        | A | none | Not Reached |
        | B | A | Not Reached |
        | C | B | Not Reached |
        | D | B, C | Not Reached |
      And student "Dave" is enrolled with all LEOs "Not Reached"
      When teacher "Dr. Mueller" marks "A" as "Reached" for "Dave"
      Then the system recomputes all dependents exactly once (idempotent):
        | LEO | NewStatus | Reason |
        | B | Reached | Prereq A reached |
        | C | Reached | Prereq B reached (cascaded) |
        | D | Reached | Prereqs B, C reached (cascaded) |
      And all updates occur in a single database transaction
      And each cascade is logged separately in audit log with order

    @domain @dependencies @teacher @regression
    Scenario: Multiple prerequisites must ALL be satisfied before unlocking
      Given LEO "Integration Testing" requires both:
        | Prerequisite |
        | Test-Driven Development anwenden |
        | Backend mit Spring Boot implementieren |
      And student "Eve" has status "Reached" for "Test-Driven Development anwenden"
      And student "Eve" has status "Not Reached" for "Backend mit Spring Boot implementieren"
      When teacher checks available LEOs for "Eve"
      Then "Integration Testing" is NOT marked "Available" for "Eve"
      And the UI shows "Locked" with message:
        | Prerequisite | Status | Missing |
        | Test-Driven Development anwenden | ✓ Completed | No |
        | Backend mit Spring Boot implementieren | ✗ Incomplete | **Yes** |
      And when teacher marks "Backend mit Spring Boot implementieren" as "Reached"
      Then "Integration Testing" immediately becomes "Available"
