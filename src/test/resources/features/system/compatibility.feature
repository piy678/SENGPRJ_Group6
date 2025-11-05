# language: en
Feature: Desktop OS compatibility
  Ensures the desktop app runs on supported operating systems.

  @compatibility @smoke
  Scenario Outline: Smoke tests pass on supported desktop OS
    Given a clean installation on "<os>"
    When the application runs the smoke test suite
    Then all critical smoke tests pass

    Examples:
      | os      |
      | Windows |
      | macOS   |
