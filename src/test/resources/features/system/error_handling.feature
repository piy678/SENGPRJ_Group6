# language: en
Feature: Error handling
  The system provides user-friendly and consistent error messages.

  @functional @system
  Scenario Outline: Display user-friendly error messages
    When an <errorType> occurs
    Then the system displays "<message>" without exposing technical details
    Examples:
      | errorType           | message                      |
      | network_failure     | Network unavailable          |
      | unauthorized_access | Access denied                |
      | invalid_input       | Please check your input data |
