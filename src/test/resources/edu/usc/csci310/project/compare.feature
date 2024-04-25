Feature: Compare favorite park list with friends

  Scenario: Comparison list covers every park on all chosen users' favorite lists.
    Given I have a list with "Yellowstone" and "Yosemite"
    And my friend has a list with "Yosemite" and "Grand Canyon"
    When I compare my list with my friend's list
    Then I should see a list with "Yellowstone", "Yosemite", and "Grand Canyon"

  Scenario: Comparing with a friend with public list
    Given I have a list with "Yellowstone" and "Yosemite"
    And my friend has a public list
    When I compare my list with my friend's list
    Then I should see a list with parks from both lists

  Scenario: Comparing with a friend with private list
    Given I have a list with "Yellowstone" and "Yosemite"
    And my friend has a private list
    When I compare my list with my friend's list
    Then I should see "One or more of your friends has a private list"

  Scenario: Comparing with nonexistent friend
    Given I have a list with "Yellowstone" and "Yosemite"
    And my friend does not exist
    When I compare my list with my friend's list
    Then I should see the message "One or more of your friends does not exist"

  Scenario: Comparing list shows correct ratios
    Given I have a list with "Yellowstone" and "Yosemite"
    And first friend has a list with "Yosemite" and "Grand Canyon"
    And second friend has a list with "Yellowstone" and "Yosemite"
    When I compare my list with my friend's list
    Then I should see the message "3/3" for "Yosemite"
    And I should see the message "2/3" for "Yellowstone"
    And I should see the message "1/3" for "Grand Canyon"

  Scenario: Comparing list is ranked by ratio
    Given I have a list with "Yellowstone" and "Yosemite"
    And first friend has a list with "Yosemite" and "Grand Canyon"
    And second friend has a list with "Yellowstone" and "Yosemite"
    When I compare my list with my friend's list
    Then the list should be ordered with "Yosemite" first
    And the list should be order with "Yellowstone" second
    And the list should be ordered with "Grand Canyon" third

  Scenario: hovering over ratio shows users who have that park in their list
    Given I have a list with "Yellowstone" and "Yosemite"
    And first friend has a list with "Yosemite" and "Grand Canyon"
    And second friend has a list with "Yellowstone" and "Yosemite"
    When I compare my list with my friend's list
    And I hover over the "3/3" for "Yosemite"
    Then I should see a list with my name, first friend's name, and second friend's name

  Scenario: clicking on park name shows park details
    Given I have a list with "Yellowstone" and "Yosemite"
    And my friend has a list with "Yosemite" and "Grand Canyon"
    When I compare my list with my friend's list
    And I click on "Yosemite"
    Then I should see the details for "Yosemite"

  Scenario: navigate to search page
    Given I am on the compare list page
    When I click on the search button in the header
    Then I should be on the search page

  Scenario: navigate to favorite list page
    Given I am on the compare list page
    When I click on the favorite list button in the header
    Then I should be on the favorite list page

  Scenario: Logout button is functional
    Given I am on the search results page
    When I click the logout button
    Then I should be taken to the login page
    And I should be logged out