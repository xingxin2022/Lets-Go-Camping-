Feature: Search for a park to visit based on various attributes
  Scenario: Search button is functional
    Given I am on the search page
    When I enter "Yosemite" into search box
    And click the search button
    Then I should see 10 parks listed in the search results

  Scenario: Search should return 10 parks
    Given I am on the search page
    When I enter "" into the search box
    And click the search button
    Then I should see 10 parks listed in the search results

  Scenario: Search by park name
    Given I am on the search page
    When I enter "Yellowstone" into search box
    And choose "park name" from the search criteria
    And click the search button
    Then I should see a list of parks that match the "park name" search criteria of "Yellowstone"

  Scenario: Search by state
    Given I am on the search page
    When I enter "California" into search box
    And choose "state" from the search criteria
    And click the search button
    Then I should see a list of parks that match the "state" search criteria of "California"

  Scenario: Search by activity
    Given I am on the search page
    When I enter "hiking" into search box
    And choose "activity" from the search criteria
    And click the search button
    Then I should see a list of parks that match the activity search criteria of "hiking"

  Scenario: Search by amenity
    Given I am on the search page
    When I enter "camping" into search box
    And choose "amenity" from the search criteria
    And click the search button
    Then I should see a list of parks that match the "amenity" search criteria of "camping"

  Scenario: Search should default to by park name
    Given I am on the search page
    When I enter "Yosemite" into search box
    And click the search button
    Then I should see a list of parks that match the "park name" search criteria of "Yosemite"

  Scenario: Load more results button is functional
    Given I am on the search results page
    When I click the "load more results" button
    Then I should see 10 more parks listed in the search results

  Scenario: Add to favorites button is functional
    Given I am on the search results page
    When I click the "add to favorites" button on a park
    Then I should see the message "Park added to favorites"
    And the park should be added to my favorites list

  Scenario: Park is already in favorites
    Given I am on the search results page
    When I click the "add to favorites" button on a park
    And the park is already in my favorites list
    Then I should see the message "Park is already in favorites"
    And the park should not be added to my favorites list

  Scenario: Clicking enter key triggers search
    Given I am on the search page
    When I enter "Yosemite" into search box
    And press the enter key
    Then I should see a list of parks that match the "park name" search criteria of "Yosemite"

  Scenario: Clicking search button given input exists
    Given I am on the search page
    When I enter "Yosemite" into search box
    And click the search button
    Then I should see a list of parks that match the "park name" search criteria of "Yosemite"


  Scenario: hover over park shows plus button
    Given I am on the search results page
    When I hover over a park
    Then I should see a plus button appear

  Scenario: Adding to favorites
    Given I am on the search results page
    When I hover over a park
    And click the plus button
    Then I should see the park added to my favorites list

  Scenario: Park is already in favorite list
    Given I am on the search results page
    When I hover over a park
    And click the plus button
    And the park is already in my favorites list
    Then I should see the message "Park is already in favorites"
    And the park should not be added to my favorites list

  Scenario: Navigate to compare list page
    Given I am on the search page
    When I click the compare list button on the header
    Then I should be taken to the compare list page

  Scenario: Navigate to favorites list page
    Given I am on the search page
    When I click the favorites list button on the header
    Then I should be taken to the favorites list page

  Scenario: Clicking on park name shows details
    Given I am on the search results page
    When I click on a park name
    Then I should be taken to the park details page

  Scenario: Logout button is functional
    Given I am on the search results page
    When I click the logout button
    Then I should be taken to the login page
    And I should be logged out
