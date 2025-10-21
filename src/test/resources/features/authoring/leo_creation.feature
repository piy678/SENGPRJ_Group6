# language: en
Feature: Authoring LEOs and relationships
  Allows teachers to create and manage Learning Elements and define dependencies between them.

  Background:
    Given a teacher is authenticated with role "Teacher"
    And a course "Mathematics 101" exists

  @functional @domain @authoring
  Scenario: Teacher creates a new LEO
    When the teacher creates a LEO named "Fractions" with topic "Arithmetic"
    Then the system saves the new LEO in the course
    And the LEO "Fractions" appears in the authoring list

  @functional @domain @authoring
  Scenario: Teacher defines a prerequisite relationship between LEOs
    Given "Fractions" and "Mixed Numbers" exist
    When the teacher sets "Fractions" as a prerequisite of "Mixed Numbers"
    Then the relationship is stored in the dependency graph

  @validation @edge-case @authoring
  Scenario: Duplicate LEO names are not allowed
    Given a LEO named "Fractions" already exists
    When the teacher attempts to create another LEO named "Fractions"
    Then the system shows error "Duplicate LEO name not allowed"

  @safety @authoring
  Scenario: Deleting a LEO with dependents triggers confirmation
    Given "Fractions" is a prerequisite of "Mixed Numbers"
    When the teacher attempts to delete "Fractions"
    Then the system displays a confirmation warning listing dependent LEOs
