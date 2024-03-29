Feature: Suggest Park for a Group of Friends
  Scenario: Suggest with a friend who has a private favorite list
    Given I have a friend who has a private favorite list
    When I click the "Suggest a Park" button
    Then I should see the message "Your friends has a private list."
  Scenario: Suggest with a friend who has a public favorite list
    Given I have a friend who has a public favorite list
    When I click the "Suggest a Park" button
    Then I should see a suggested park
  Scenario: Suggest with a nonexistent friend
    Given I have a nonexistent friend
    When I click the "Suggest a Park" button
    Then I should see the message "One or more of your friends do not exist."
  Scenario: Suggestion is correct and has the highest ratio
    Given I have a favorite list with "Yosemite",and "Yellowstone"
    And friend 1 has a favorite list with "Yosemite", and "Grand Canyon"
    And friend 2 has a favorite list with "Yosemite", and "Yellowstone"
    When I click the "Suggest a Park" button
    Then I should see "Yosemite" as the suggested park
