Feature: Update and review a favorite park list

  Background:
    Given the user is logged in

  Scenario: Create a list
    Given the user does not have a list yet
    When the user add a park to the list for the first time
    Then the system creates a list with the park in it

  Scenario: Update the list
    Given the user has created a list
    When the user add a park to the list
    Then the system adds that park to the list

  Scenario: Review the list
    Given the user has created a list
    When the user tries to check out the list
    Then the system shows the list



