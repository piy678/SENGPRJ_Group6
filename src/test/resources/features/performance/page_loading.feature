# language: en
Feature: Page loading performance
  Ensures critical pages load within performance targets.

  @performance @ui
  Scenario: Dashboard page loads within defined performance budget
    Given a user is authenticated and has stable network connection
    When the user navigates to the "Dashboard" page
    Then the DOMContentLoaded event fires within 1 second
    And the largest contentful paint occurs within 2 seconds
    And the page becomes interactive within 2.5 seconds
