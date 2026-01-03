# language: en

Feature: Role-based access control and permissions
  As a system administrator
  I want to enforce strict role-based permissions
  So that only authorized users can perform sensitive operations

  Background:
    Given the system has the following roles:
      | Role | Permissions |
      | TEACHER | create LEO, update LEO, delete LEO, record assessment, view all students |
      | STUDENT | view own progress, view own recommendations, view course LEOs (filtered by availability) |
      | ADMIN | manage users, manage courses, view audit logs |

  @security @permissions @smoke
  Scenario: Teacher can record assessments, student cannot
    Given a teacher "Dr. Mueller" is authenticated with role "Teacher"
    And a student "Alice" is authenticated with role "Student"
    And LEO "Anforderungsanalyse verstehen" exists
    And "Alice" is enrolled in course
    When teacher "Dr. Mueller" records assessment "Reached" for "Alice"
    Then the assessment is saved successfully
    And an audit log entry shows: user="Dr. Mueller", action="ASSESSMENT_RECORDED"
    When student "Alice" attempts to change her own assessment status
    Then the system denies with error "Access denied - insufficient permissions"
    And HTTP status code is 403 (Forbidden)
    And an audit log entry shows: user="Alice", action="ASSESSMENT_UPDATE_ATTEMPT", status="REJECTED_PERMISSIONS"
