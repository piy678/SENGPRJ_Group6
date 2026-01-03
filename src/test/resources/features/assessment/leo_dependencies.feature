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