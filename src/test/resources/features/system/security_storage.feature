# language: en
Feature: Secure data storage
  Ensures sensitive information is protected both in transit and at rest.

  @security @database
  Scenario: Database connections use TLS
    Given the application is connected to the production database
    Then all connections are encrypted via TLS

  @security @database
  Scenario: Sensitive data is encrypted at rest
    Given the database contains student personal data
    Then the fields "name" and "assessment" are stored encrypted
