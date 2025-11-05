# language: en
Feature: Searching and filtering LEOs
  Enables teachers and learners to search for LEOs by name, topic, and status.

  Background:
    Given a set of LEOs exist in course "Mathematics 101"

  @functional @search
  Scenario Outline: Filter LEOs by property
    When the user searches for LEOs by <filter> "<value>"
    Then only LEOs matching <filter> "<value>" are shown
    Examples:
      | filter | value       |
      | name   | Fractions   |
      | topic  | Algebra     |
      | status | Reached     |

  @functional @search
  Scenario: Combine filters
    When the user searches for LEOs by topic "Arithmetic" and status "Not Reached"
    Then the result list shows only matching LEOs
    And the number of results is displayed

  @usability @search
  Scenario: Empty search results are handled gracefully
    When the user searches for "Quantum Mechanics"
    Then the system shows "No LEOs found"
