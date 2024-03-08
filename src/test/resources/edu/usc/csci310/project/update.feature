Feature: Update and review a favorite park list
  Scenario: Visiting favorite park list
    Given I have a favorite park list
    When I visit the favorite park list page
    Then I should see the list of my favorite parks
    And the order of the list should be by park name

  Scenario: Moving a park up in the list
    Given I am on the favorite park list page
    When I click the move up button for a park
    Then the park should move up in the list
    And the order of the list should be updated for future visits

  Scenario: Moving a park down in the list
    Given I am on the favorite park list page
    When I click the move down button for a park
    Then the park should move down in the list
    And the order of the list should be updated for future visits

  Scenario: Clicking on a park in the list to view details
    Given I am on the favorite park list page
    When I click on a park in the list
    Then I should be taken to the park details page
    And the park details should be displayed




