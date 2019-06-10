Feature: Interaction diagram

  Scenario: Interaction diagram
    Given we read an interaction diagram as XMI
    When  an actor sends a request
    Then another actor sends a response