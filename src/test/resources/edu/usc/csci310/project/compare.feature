Feature: Compare favorite park list with friends

  Scenario: Comparison list covers every park on all chosen users' favorite lists DONE
    Given I have a list with "Adams National Historical Park" and "Abraham Lincoln Birthplace National Historical Park"
    And my friend has a list with "Acadia National Park" and "Abraham Lincoln Birthplace National Historical Park"
    When I compare my list with my friend's list
    Then I should see a list with "Adams National Historical Park", "Abraham Lincoln Birthplace National Historical Park", and "Acadia National Park"

  Scenario: Comparing with a friend with public list CANT YET
    Given I have a list with "Adams National Historical Park" and "Abraham Lincoln Birthplace National Historical Park"
    And my friend has a public list
    When I compare my list with my friend's list
    Then I should see a list with parks from both lists

  Scenario: Comparing with a friend with private list CANT YET
    Given I have a list with "Adams National Historical Park" and "Abraham Lincoln Birthplace National Historical Park"
    And my friend has a private list
    When I compare my list with my friend's list
    Then I should see "comparing failed"

  Scenario: Comparing with nonexistent friend DONE
    Given I have a list with "Adams National Historical Park" and "Abraham Lincoln Birthplace National Historical Park"
    And my friend does not exist
    Then I should see the message "User not found."

  Scenario: Comparing list shows correct ratios DONE
    Given I have a list with "Abraham Lincoln Birthplace National Historical Park" and "Acadia National Park"
    And first friend has a list with "Acadia National Park" and "Adams National Historical Park"
    And second friend has a list with "Abraham Lincoln Birthplace National Historical Park" and "Acadia National Park"
    When I compare my list with both of my friend's list
    Then I should see the message "100.00%" for "Acadia National Park"
    And I should see the message "66.67%" for "Abraham Lincoln Birthplace National Historical Park"
    And I should see the message "33.33%" for "Adams National Historical Park"

  Scenario: Comparing list is ranked by ratio DONE
    Given I have a list with "Abraham Lincoln Birthplace National Historical Park" and "Acadia National Park"
    And first friend has a list with "Acadia National Park" and "Adams National Historical Park"
    And second friend has a list with "Abraham Lincoln Birthplace National Historical Park" and "Acadia National Park"
    When I compare my list with both of my friend's list
    Then the list should be ordered with "Acadia National Park" first
    And the list should be ordered with "Abraham Lincoln Birthplace National Historical Park" second
    And the list should be ordered with "Adams National Historical Park" third

  Scenario: hovering over ratio shows users who have that park in their list DONE
    Given I have a list with "Abraham Lincoln Birthplace National Historical Park" and "Acadia National Park"
    And first friend has a list with "Acadia National Park" and "Adams National Historical Park"
    And second friend has a list with "Abraham Lincoln Birthplace National Historical Park" and "Acadia National Park"
    When I compare my list with both of my friend's list
    And I hover over the "Acadia National Park"
    Then I should see a list with my name, first friends name, and second friends name

  Scenario: clicking on park name shows park details DONE
    Given I have a list with "Abraham Lincoln Birthplace National Historical Park" and "Acadia National Park"
    And my friend has a list with "Acadia National Park" and "Adams National Historical Park"
    When I compare my list with my friend's list
    And I click on "Acadia National Park"
    Then I should see the details for "Acadia National Park"

  Scenario: navigate to search page DONE
    Given I am on the compare list page
    When I click on the search button in the header
    Then I should be on the "search" page

  Scenario: navigate to favorite list page DONE
    Given I am on the compare list page
    When I click on the favorite list button in the header
    Then I should be on the "favorite list" page

  Scenario: Logout button is functional DONE
    Given I am on the search results page
    When I click the logout button
    Then I should be on the "login" page
    And I should be logged out
