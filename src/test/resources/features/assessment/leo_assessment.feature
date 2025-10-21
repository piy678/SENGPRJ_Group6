# language: en
Feature: Recording assessments per LEO
  This feature covers how a teacher records, updates, and audits student assessments for Learning Elements (LEOs).

  Background:
    Given a teacher is authenticated with role "Teacher"
    And a course "Mathematics 101" exists
    And student "Alice" is enrolled in "Mathematics 101"
    And a LEO named "Fractions" exists in the course

  @functional @domain @assessment @teacher
  Scenario: Teacher records a new assessment for a student on a LEO
    Given student "Alice" has no previous assessment for "Fractions"
    When the teacher records the assessment result "Mastered" for "Fractions" with date "today"
    Then the system stores the assessment in the database
    And the student "Alice" shows status "Mastered" for "Fractions"

  @functional @domain @assessment @teacher
  Scenario: Teacher updates an existing assessment
    Given student "Alice" has an existing assessment "Partially Reached" for "Fractions"
    When the teacher updates the assessment to "Reached"
    Then the system archives the previous assessment
    And the new assessment shows status "Reached" for "Fractions"

  @security @edge-case @assessment
  Scenario: Only teachers can record or update assessments
    Given a user with role "Student" is signed in
    When the student attempts to record an assessment
    Then the system denies the operation with error "Access denied"

  @audit @edge-case @assessment
  Scenario: Assessment changes are logged with timestamp and author
    Given the teacher records an assessment "Mastered" for "Fractions" for "Alice"
    When the operation is completed
    Then the audit log contains an entry with user "Teacher", action "Assessment recorded", and a valid timestamp
