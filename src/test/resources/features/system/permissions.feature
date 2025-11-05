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

  @security @permissions @smoke
  Scenario: Only course teacher can modify LEOs in their course
    Given course "Math 101" with teacher "Dr. Mueller"
    And course "Physics 101" with teacher "Prof. Jones"
    And LEO "Calculus" in "Math 101"
    And LEO "Mechanics" in "Physics 101"
    When teacher "Dr. Mueller" attempts to edit "Calculus" (own course)
    Then the edit is allowed
    And changes are saved
    When teacher "Prof. Jones" attempts to edit "Calculus" (different course)
    Then the system denies with error "Access denied - you are not the course owner"
    And HTTP status code is 403 (Forbidden)
    And no changes are made
    And audit log shows: action="LEO_UPDATE_ATTEMPT", status="REJECTED_NOT_COURSE_TEACHER"

  @security @permissions @regression
  Scenario: Student can only view their own progress, not other students
    Given a student "Alice" is authenticated
    And a student "Bob" is authenticated
    And both students are enrolled in "Software Engineering Project"
    And "Alice" has assessments in the course
    And "Bob" has assessments in the course
    When Alice opens "My Progress" page
    Then Alice sees only her own assessments and progress
    And Alice does NOT see Bob's assessments
    And Alice does NOT see Bob's grades or statistics
    When Alice attempts to access "/api/students/Bob/progress"
    Then the system denies with error "Access denied - cannot view other student's data"
    And HTTP status code is 403 (Forbidden)
    And audit log shows: user="Alice", action="UNAUTHORIZED_ACCESS_ATTEMPT", target="Bob's data"

  @security @permissions @regression
  Scenario: Teacher can view all students in their course only
    Given course "Software Engineering Project" with teacher "Dr. Mueller"
    And students "Alice", "Bob", "Carol" enrolled in this course
    And course "Different Course" with teacher "Prof. Jones"
    And student "Dave" enrolled in "Different Course"
    When teacher "Dr. Mueller" views "Student List" for "Software Engineering Project"
    Then Dr. Mueller sees: Alice, Bob, Carol
    And Dr. Mueller does NOT see Dave
    When Dr. Mueller attempts to access "/api/courses/Different%20Course/students"
    Then the system denies with error "Access denied - not the course teacher"
    And HTTP status code is 403 (Forbidden)

  @security @permissions @edge-case
  Scenario: Cannot view archive/history without permission
    Given a student "Alice" is authenticated
    And teacher "Dr. Mueller" is authenticated
    And "Alice" has assessment history (multiple versions)
    When Alice attempts to view her own assessment history
    Then the system allows (own data)
    And Alice sees her complete history
    When another student "Bob" attempts to view Alice's assessment history
    Then the system denies "Access denied - cannot view other student's data"
    And HTTP status code is 403 (Forbidden)
    When teacher "Prof. Jones" (different course) attempts to view "Alice's" history
    Then the system denies "Access denied - not the course teacher"
    And HTTP status code is 403 (Forbidden)

  @security @permissions @regression
  Scenario: Audit log entries are protected from unauthorized access
    Given an audit log entry exists for "Alice's assessment update"
    When student "Alice" attempts to access audit logs
    Then the system denies "Access denied - students cannot view audit logs"
    When student "Bob" attempts to access any audit logs
    Then the system denies "Access denied - students cannot view audit logs"
    When teacher "Dr. Mueller" (course teacher) attempts to access audit logs
    Then the system denies "Access denied - teachers cannot view audit logs (admin only)"
    When administrator is authenticated
    Then admin can view complete audit log with all entries:
      | Field | Value |
      | user | Dr. Mueller |
      | action | ASSESSMENT_RECORDED |
      | timestamp | 2025-10-21 19:30:00 |
      | status | SUCCESS |

  @security @permissions @edge-case
  Scenario: Permission denied when attempting cascade operations without authorization
    Given teacher "Dr. Mueller" teaches "Software Engineering Project"
    And teacher "Prof. Jones" teaches "Different Course"
    And LEO "A" in "Software Engineering Project" (taught by Dr. Mueller)
    And LEO "B" in "Different Course" (taught by Prof. Jones)
    When Prof. Jones attempts to set "A" as prerequisite of "B"
    Then the system denies with error "Access denied - insufficient permissions on source LEO"
    And the relationship is NOT created
    And audit log shows: action="PREREQUISITE_CREATE_ATTEMPT", status="REJECTED_PERMISSIONS"