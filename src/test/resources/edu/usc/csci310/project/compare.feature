Feature: Compare favorite park list with friends

  Scenario: Comparison list covers every park on all chosen users' favorite lists.
    Given I have a list with "Yellowstone" and "Yosemite"
    And my friend has a list with "Yosemite" and "Grand Canyon"
    When I compare my list with my friend's list
    Then I should see a list with "Yellowstone", "Yosemite", and "Grand Canyon"

  Scenario: Comparing with a friend with private list
    Given I have a list with "Yellowstone" and "Yosemite"
    And my friend has a private list
    When I compare my list with my friend's list
    Then I should see the message "One or more of your friends has a private list"

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
    Then the list should be ordered with "Yosemite" first, "Yellowstone" second, and "Grand Canyon" third

