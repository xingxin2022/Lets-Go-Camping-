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
=======
Feature: Search for a park to visit 
  Scenario: Search by location
    Given the user is logged into the camping trip planner application
    When the user navigates to the 'Search Parks' section
    And selects 'Location' as the search criterion
    And enters "Colorado" into the location field
    Then the system should display all national parks located in Colorado

  Scenario: Search by activity
    Given the user is on the 'Search Parks' page of the application
    When the user selects 'Activities' as the search criterion
    And checks 'Hiking' in the activities options
    Then the system should display all parks offering hiking  

  Scenario: Search by size
    Given the user is using the 'Search Parks' feature
    When the user selects 'Size' as the search criterion
    And specifies a range of "more than 500 square miles"
    Then the system should list all national parks larger than 500 square miles

  Scenario: Search by Visitor Ratings
    Given the user is logged into the web application
    When the user navigates to 'Search Parks'
    And selects 'Ratings' as the filter
    And chooses a rating of 4 stars
    Then the system should display all parks with a rating of 4 stars 

  Scenario: Search by Accessibility Options
    Given the user is on the 'Search Parks' interface
    When the user applies the 'Accessibility' filter
    And selects 'Wheelchair Accessible' from the accessibility features
    Then the system should display all parks with accessibility options

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

Scenario: Add to favorites button is functional
    Given I am on the search results page
    When I hover over the plus button next to a park
    And click the "add to favorites" button
    Then I should see the message "Park added to favorites"
    And the park should be added to my favorites list

Scenario: Cancelling add to favorites
    Given I am on the search results page
    When I click the "add to favorites" button on a park
    And click the "cancel" button
    Then I should see the message "Add to favorites cancelled
    And the park should not be added to my favorites list

