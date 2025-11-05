# language: en

Feature: Authoring LEOs and relationships with improved quality
  As a teacher
  I want to create, manage, and organize Learning Outcomes with their relationships
  So that I can build a structured and validated learning path for my course

  Background:
    Given a teacher "Dr. Mueller" is authenticated with role "Teacher"
    And course "Software Engineering Project" exists
    And course "Software Engineering Project" has teacher "Dr. Mueller"

  @domain @authoring @teacher @smoke
  Scenario: Teacher creates a new LEO with mandatory fields and default values
    Given teacher "Dr. Mueller" is on the LEO authoring page for course "Software Engineering Project"
    When the teacher creates a LEO with:
      | Field | Value |
      | name | Anforderungsanalyse verstehen |
      | description | Grundlegende Konzepte der Requirements Engineering und deren Anwendung |
      | course | Software Engineering Project |
    Then the LEO "Anforderungsanalyse verstehen" is saved in the system
    And the LEO has:
      | Field | Value |
      | course_id | <id_of_course> |
      | created_at | current timestamp |
      | created_by | Dr. Mueller |
      | is_active | true |
    And the LEO appears in the course LEO list with green indicator "Active"
    And an audit log entry records: action="LEO_CREATED", created_by="Dr. Mueller"

  @domain @authoring @teacher @smoke
  Scenario: Teacher defines a prerequisite relationship between two LEOs
    Given LEO "Anforderungsanalyse verstehen" exists in the course
    And LEO "Gherkin-Spezifikationen erstellen" exists in the course
    When teacher "Dr. Mueller" sets "Anforderungsanalyse verstehen" as a prerequisite of "Gherkin-Spezifikationen erstellen"
    Then the relationship "Anforderungsanalyse verstehen → Gherkin-Spezifikationen erstellen" is saved
    And the relationship is visible in:
      | View | Display |
      | LEO Detail page | "Requires: Anforderungsanalyse verstehen" |
      | Dependency Graph | Tree with arrow showing prerequisite |
      | Course LEO List | Indicator showing "1 prerequisite" |
    And an audit log entry records: action="PREREQUISITE_CREATED", created_by="Dr. Mueller", source="Anforderungsanalyse verstehen", target="Gherkin-Spezifikationen erstellen"

  @domain @authoring @teacher @edge-case
  Scenario: Prevent duplicate LEO names within the same course
    Given LEO "Anforderungsanalyse verstehen" already exists in course "Software Engineering Project"
    When teacher "Dr. Mueller" attempts to create another LEO named "Anforderungsanalyse verstehen" in the same course
    Then the system rejects the creation with error:
      | Error | "LEO name 'Anforderungsanalyse verstehen' already exists in this course" |
      | Suggestion | "Please choose a different name or edit the existing LEO" |
    And the form highlights the "name" field in red
    And no duplicate LEO is created
    And an audit log records: action="LEO_CREATE_ATTEMPT", status="REJECTED_DUPLICATE_NAME"

  @domain @authoring @teacher @edge-case
  Scenario: Delete LEO with safety check when dependents exist
    Given LEO "Anforderungsanalyse verstehen" exists
    And LEOs "Gherkin-Spezifikationen erstellen", "Low-Fidelity Prototypen entwickeln", "Requirements Dokumentation" depend on "Anforderungsanalyse verstehen"
    When teacher "Dr. Mueller" attempts to delete "Anforderungsanalyse verstehen"
    Then the system shows warning dialog:
      | Title | "Cannot delete - dependencies exist" |
      | Message | "3 LEOs depend on 'Anforderungsanalyse verstehen'" |
      | Details | Gherkin-Spezifikationen erstellen, Low-Fidelity Prototypen entwickeln, Requirements Dokumentation |
      | Options | "Cancel", "Remove all prerequisites first", "Force delete and update dependents" |
    And the LEO is NOT deleted unless explicitly confirmed
    And an audit log records: action="LEO_DELETE_ATTEMPT", status="BLOCKED_DEPENDENCIES"

  @domain @authoring @teacher @regression
  Scenario: Update LEO details and preserve relationships
    Given LEO "Anforderungsanalyse verstehen" exists with:
      | Field | Value |
      | description | Old description |
      | is_active | true |
    And LEO "Gherkin-Spezifikationen erstellen" depends on "Anforderungsanalyse verstehen"
    When teacher "Dr. Mueller" updates LEO "Anforderungsanalyse verstehen" with:
      | Field | NewValue |
      | description | Erweiterte Requirements Engineering Konzepte mit praktischen Anwendungen |
      | updated_at | current timestamp |
    Then the LEO description is updated
    And the prerequisite relationship to "Gherkin-Spezifikationen erstellen" remains intact (NOT affected)
    And the dependent LEO still shows "Depends on: Anforderungsanalyse verstehen"
    And an audit log entry records: action="LEO_UPDATED", changed_fields="description", created_by="Dr. Mueller"

  @domain @authoring @teacher @regression
  Scenario: Create LEO with multiple prerequisites (complex dependency)
    Given LEOs exist in the course:
      | LEO |
      | Test-Driven Development anwenden |
      | Backend mit Spring Boot implementieren |
      | Frontend mit React entwickeln |
    When teacher "Dr. Mueller" creates LEO "Integration Testing" with prerequisites:
      | Prerequisite |
      | Test-Driven Development anwenden |
      | Backend mit Spring Boot implementieren |
      | Frontend mit React entwickeln |
    Then "Integration Testing" is created with 3 prerequisites
    And all relationships are visible in the dependency graph:
      | Source | Target |
      | Test-Driven Development anwenden | Integration Testing |
      | Backend mit Spring Boot implementieren | Integration Testing |
      | Frontend mit React entwickeln | Integration Testing |
    And the LEO page shows "3 prerequisites" badge
    And audit log records 3 separate PREREQUISITE_CREATED entries

  @domain @authoring @teacher @edge-case
  Scenario: Duplicate prerequisites are rejected
    Given LEO "Backend mit Spring Boot implementieren" exists in the course
    And LEO "Advanced Backend" exists with prerequisite "Backend mit Spring Boot implementieren"
    When teacher "Dr. Mueller" attempts to add "Backend mit Spring Boot implementieren" again as a prerequisite to "Advanced Backend"
    Then the system rejects with error:
      | Error | "Prerequisite 'Backend mit Spring Boot implementieren' is already added" |
    And the duplicate relationship is NOT created
    And audit log records: action="PREREQUISITE_CREATE_ATTEMPT", status="REJECTED_DUPLICATE"
