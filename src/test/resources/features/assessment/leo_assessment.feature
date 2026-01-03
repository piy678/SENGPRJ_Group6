# language: en

Feature: Recording assessments per LEO
  As a teacher
  I want to record, update, and audit student assessments for Learning Outcomes
  So that I can track learning progress, maintain complete history, and ensure accountability

  Background:
    Given a teacher "Dr. Mueller" is authenticated with role "Teacher"
    And course "Software Engineering Project" exists
    And course "Software Engineering Project" has teacher "Dr. Mueller"
    And student "Alice" is enrolled in "Software Engineering Project"
    And a LEO named "Anforderungsanalyse verstehen" exists in "Software Engineering Project"

  @domain @assessment @teacher @smoke
  Scenario: Teacher records a new assessment for a student on a LEO
    Given student "Alice" has no assessment for "Anforderungsanalyse verstehen"
    When teacher "Dr. Mueller" records assessment "Reached" for "Anforderungsanalyse verstehen" for student "Alice" with date "today"
    Then the assessment is stored in database
    And "Alice" shows status "Reached" for "Anforderungsanalyse verstehen"
    And assessed_by field contains "Dr. Mueller"
    And is_archived flag is false

  @domain @assessment @teacher @regression
  Scenario: Teacher updates an existing assessment and archives the previous one
    Given student "Alice" has assessment "Partially Reached" for "Anforderungsanalyse verstehen" recorded on "2025-10-15"
    When teacher "Dr. Mueller" updates assessment to "Reached" for "Anforderungsanalyse verstehen" on "2025-10-21"
    Then the current assessment shows status "Reached"
    And the current assessment is_archived is false
    And the previous assessment is marked as is_archived = true
    And the current assessment has field previous_assessment_id linking to old assessment
