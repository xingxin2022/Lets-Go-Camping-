Feature: Search for a park to visit based on various attributes
  Scenario: Search by location
    Given the user is logged into the camping trip planner application
    When the user navigates to the 'Search Parks' section
    And selects 'Location' as the search criterion
    And enters "Colorado" into the location field
    Then the system should display all national parks located in Colorado

  Scenario: Search by activity
    Given the user is on the 'Search Parks' page of the application
    When the user selects 'Activities' as the search criterion
    And checks 'Hiking' and 'Wildlife Viewing' in the activities options
    Then the system should display all national parks offering hiking and wildlife viewing activities

  Scenario: Search by size
    Given the user is using the 'Search Parks' feature
    When the user selects 'Size' as the search criterion
    And specifies a range of "more than 500 square miles"
    Then the system should list all national parks larger than 500 square miles

  Scenario: Search by Visitor Ratings
    Given the user is logged into the web application
    When the user navigates to 'Search Parks'
    And selects 'Ratings' as the filter
    And chooses a minimum rating of 4 stars
    Then the system should display all national parks with an average rating of 4 stars or higher

  Scenario: Search by Accessibility Options
    Given the user is on the 'Search Parks' interface
    When the user applies the 'Accessibility' filter
    And selects 'Wheelchair Accessible' from the accessibility features
    Then the system should display all national parks with wheelchair accessibility options


