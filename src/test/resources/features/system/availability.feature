# language: en
Feature: System availability monitoring
  Ensures the platform meets uptime requirements.

  @monitoring @reliability
  Scenario: System uptime meets 99% target
    Given a health check endpoint is monitored every 60 seconds for 24 hours
    When uptime is calculated
    Then the measured uptime is at least 99%
    And no single outage lasts longer than 10 minutes
