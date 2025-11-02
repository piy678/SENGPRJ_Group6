# language: en
Feature: Learner mastery overview
  Shows a learner their current mastery per LEO with last updated timestamps.

  Background:
    Given learner "Alice" is authenticated
    And course "Mathematics 101" exists
    And learner "Alice" is enrolled in "Mathematics 101"

  @functional @learner @domain
  Scenario: Learner views their mastery dashboard
    Given LEOs "Fractions" and "Decimals" exist with statuses for "Alice"
    When the learner opens the mastery overview
    Then the list shows each LEO with current mastery status and last-updated date
    And only data for learner "Alice" is visible
