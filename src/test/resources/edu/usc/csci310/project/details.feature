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
    Given I log in and am on the park details page
    When I click on the add to favorite list button on details page
    Then the park should be added to my favorite list

  Scenario: plus button shows and is functional
    Given the park is not on my favorite list
    When I am on the park details page
    Then the plus button should be visible
    And I click on the add to favorite list button on details page
    Then the park should be added to my favorite list

  Scenario: Park added to favorites
    Given I have added "Yosemite" to my favorites list
    When I search for "Yosemite" in the search box
    And I click on the park in search results
    Then I should see the park is favorited

  Scenario: clicking on name of park minimizes the details
    Given I log in and am on the park details page
    When I click on the name of the park
    Then the details should be minimized

  Scenario: clicking on amenities triggers search
    Given I log in and am on the park details page
    When I click on an amenity
    Then a search should be triggered for that amenity

  Scenario: clicking on state triggers search
    Given I log in and am on the park details page
    When I click on the state
    Then a search should be triggered for that state

  Scenario: clicking on activity triggers search
    Given I log in and am on the park details page
    When I click on an activity
    Then a search should be triggered for that activity


