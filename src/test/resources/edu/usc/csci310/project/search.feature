Feature: Search for a park to visit based on various attributes
  Scenario: Search button is functional
    Given I am on the search page
    When I enter "park" into search box
    And click the search button
    Then I should see 10 parks listed in the search results

  Scenario: Search should return 10 parks
    Given I am on the search page
    When I enter "park" into search box
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
    Then I should see a list of parks that match the search criteria of "hiking"

  Scenario: Search by amenity
    Given I am on the search page
    When I enter "camping" into search box
    And choose "amenity" from the search criteria
    And click the search button
    Then I should see a list of parks that match the search criteria of "camping"

  Scenario: Search should default to by park name
    Given I am on the search page
    When I enter "Yosemite" into search box
    And click the search button
    Then I should see a list of parks that match the "park name" search criteria of "Yosemite"

  Scenario: Load more results button is functional
    Given I am on search page
    When I enter "park" into search box
    And click the search button
    And I click the load more results button
    Then I should see 20 parks listed in the search results

  Scenario: Add to favorites button is functional/Adding park to favorite
    Given I am on the search results page
    When  I hover over the park card
    And I click the favorites button on a park
    Then I should see the message "Park successfully added to favorite list"


  Scenario: Park is already in favorites
    Given I am on the search results page
    When  I hover over the park card
    And I click the favorites button on a park
    Then I should see the message "Park already in the favorite list"


  Scenario: Clicking enter key triggers search
    Given I am on the search page
    When I enter "Yosemite" into search box
    And press the enter key
    Then I should see a list of parks that match the "park name" search criteria of "Yosemite"

  Scenario: hover over park shows plus button
    Given I am on the search results page
    When I hover over the park card
    Then I should see the favorite button

  Scenario:  Clicking the name of the park shows details
    Given I am on the search results page
    When I click the name of the park
    Then I should see park details

  Scenario: Clicking home button works
    Given I am on the search page
    When I click home button
    Then I should be directed to "Log In" page

  Scenario: Navigate to favorites list page
    Given I am on the search page
    When I click favoriteList button
    Then I should be directed to "Favorite Parks" page

  Scenario: Navigate to compare list page
    Given I am on the search page
    When I click compareList button
    Then I should be directed to compare page

  Scenario: Logout button is functional
    Given I am on the search page
    When I click Logout button
    Then I should be directed to "Log In" page
  Scenario: Clicking search button given input exists
    Given I am on the search page
    When I enter "Yosemite" into search box
    And click the search button
    Then I should see a list of parks that match the "park name" search criteria of "Yosemite"
