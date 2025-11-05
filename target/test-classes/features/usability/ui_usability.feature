# language: en
Feature: UI usability for teachers
  Measures how easily a teacher can complete common tasks.

  @usability @ui
  Scenario: Teacher completes main workflow without external help
    Given a teacher is using the authoring interface for the first time
    When the teacher creates a new LEO and sets a prerequisite
    Then the task is completed without external guidance
    And no more than one error message appears
