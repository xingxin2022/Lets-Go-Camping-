Feature: Compare favorite park list with friends

  Background:
    Given the user is logged in
    And has friends added to their network

  Scenario: Have no friends with a valid list
    Given the user has a list and select friends without a valid list
    When the user compare with selected friends
    Then the system indicates friends do not have a valid list

  Scenario: Have no park in common
    Given the user has a list and select at least one friend with a list
    When the user compare with selected friends
    And they have no favorite park in common
    Then the system indicates nothing in common

  Scenario: Have one park in common
    Given the user has a list and select at least one friend with a list
    When the user compare with selected friends
    And they have one favorite park in common
    Then the system indicates the name of that park

  Scenario: Have more than one park in common
    Given the user has a list and select at least one friend with a list
    When the user compare with selected friends
    And they have more than one favorite park in common
    Then the system indicates the names of the parks in alphabetic order

