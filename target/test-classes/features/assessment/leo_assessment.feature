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

  @domain @assessment @teacher @security @edge-case
  Scenario: Only teachers can record or update assessments (permission check)
    Given student "Bob" is authenticated with role "Student"
    And "Bob" has assessment "Not Reached" for "Anforderungsanalyse verstehen"
    When student "Bob" attempts to change assessment to "Reached"
    Then the system rejects the action with error "Access denied - insufficient permissions"
    And the assessment status remains "Not Reached"
    And no audit log entry is created for this failed attempt (or marked as 'REJECTED')

  @domain @assessment @audit @regression
  Scenario: Assessment change is written to audit log with complete details
    Given student "Carol" has assessment "Partially Reached" for "Anforderungsanalyse verstehen"
    When teacher "Dr. Mueller" updates assessment to "Reached" at "2025-10-21 19:30:00"
    Then an audit log entry is created with:
      | Field      | Value                                |
      | assessment_id | <id_of_assessment>                |
      | changed_by | Dr. Mueller                          |
      | changed_at | 2025-10-21 19:30:00                  |
      | old_status | Partially Reached                    |
      | new_status | Reached                              |
      | change_type | MANUAL                              |
      | reason     | Teacher manual update               |
    And the audit log is immutable (read-only)

  @domain @assessment @audit @regression
  Scenario: Archive history scenario - view complete assessment history
    Given student "Dave" has multiple assessments for "Anforderungsanalyse verstehen":
      | status | date | assessor |
      | Not Reached | 2025-10-10 | Dr. Mueller |
      | Partially Reached | 2025-10-15 | Dr. Mueller |
      | Reached | 2025-10-21 | Dr. Mueller |
    When teacher "Dr. Mueller" views "Dave's" assessment history for "Anforderungsanalyse verstehen"
    Then the history shows all three assessments with:
      | Assessment | Status | RecordedOn | ArchivedOn | AssessorName |
      | Current | Reached | 2025-10-21 | null | Dr. Mueller |
      | Archive 1 | Partially Reached | 2025-10-15 | 2025-10-21 | Dr. Mueller |
      | Archive 2 | Not Reached | 2025-10-10 | 2025-10-15 | Dr. Mueller |
    And student "Dave" can see this history for transparency

  @domain @assessment @audit @edge-case
  Scenario: Cannot downgrade prerequisite if dependent is already achieved
    Given LEO "Anforderungsanalyse verstehen" is prerequisite of "Gherkin-Spezifikationen erstellen"
    And student "Eve" has status "Reached" for "Anforderungsanalyse verstehen"
    And student "Eve" has status "Reached" for "Gherkin-Spezifikationen erstellen"
    When teacher "Dr. Mueller" attempts to downgrade "Anforderungsanalyse verstehen" to "Partially Reached"
    Then the system shows warning "Downgrade may violate dependency - dependent LEO is also Reached"
    And teacher can choose to:
      | Option | Result |
      | Cancel | Assessment unchanged, no audit entry |
      | Force override | Assessment changed, audit logged with reason "Forced override - violation accepted" |
