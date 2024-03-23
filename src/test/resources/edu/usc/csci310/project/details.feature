Feature: obtain details about the various parks
  Scenario: park is on the favorite list
    Given the park is on my favorite list
    When I click on the park in search results
    Then I should see the park details and the park is on my favorite list

  Scenario: park is not on the favorite list
    Given the park is not on my favorite list
    When I click on the park in search results
    Then I should see the park details and the park is not on my favorite list

  Scenario: button to add park to favorite list
    Given I am on the park details page
    When I click on the add to favorite list button
    Then the park should be added to my favorite list