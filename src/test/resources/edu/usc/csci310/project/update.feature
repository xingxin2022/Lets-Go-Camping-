Feature: Update and review a favorite park list
  Scenario: Default order of favorite park list
    Given I have a favorite park list
    When I visit the favorite park list page
    Then I should see the list of my favorite parks
    And the order of the list should be by park name

  Scenario: Moving a park up in the list
    Given I am on the favorite park list page
    When I hover over the second park
    And I click the move up button for a park
    Then the park should move up in the list

  Scenario: Moving a park down in the list
    Given I am on the favorite park list page
    When I hover over the first park
    And I click the move down button for a park
    Then the park should move down in the list

  Scenario: Clicking on a park in the list to view details
    Given I am on the favorite park list page
    When I click on a park in the list
    Then I should be taken to the park details page

  Scenario: order of favorite park list should be saved
    Given I change the order of the favorite park list
    When I visit the search page
    And I return to the favorite park list page
    Then I should see the list with the updated order

  Scenario: pop up to confirm delete a park from the list
    Given I am on the favorite park list page
    When I hover over the first park
    And I click the delete button for a park
    Then I click confirm on the pop-up to confirm deletion
    And the park should be removed from the list

  Scenario: cancel delete a park from the list
    Given I am on the favorite park list page
    When I hover over the first park
    And I click the delete button for a park
    Then I click cancel on the pop-up to cancel deletion
    And the park should remain in the list

  Scenario: favorite list defaults to being private
    Given I have a favorite park list
    When I visit the favorite park list page
    Then the list should be private by default

  Scenario: changing favorite list to public
    Given I am on the favorite park list page
    When I click the make public button
    Then the list should be public

  Scenario: changing favorite list to private
    Given I am on the favorite park list page
    When I click the make private button
    Then the list should be private

  Scenario: changing favorite list to private saves
    Given I am on the favorite park list page
    When I click the make private button
    When I visit the search page
    And I return to the favorite park list page
    Then the list should be private

  Scenario: delete all operation is functional
    Given I am on the favorite park list page
    When I click the delete all button
    Then I should see a pop-up to confirm deletion
    And I click confirm on the pop-up to confirm deletion
    And the list should be empty

  Scenario: delete all cancel is functional
    Given I am on the favorite park list page
    When I click the delete all button
    Then I should see a pop-up to confirm deletion
    And I click cancel on the pop-up to cancel deletion
    And the list should remain the same

  Scenario: hover over park shows move-up move-down remove button
    Given I am on the favorite park list page
    When I hover over the first park
    Then move-up move-down remove buttons are visible

  Scenario: Clicking home button works
    Given I am on the favorite park list page
    When I click the home button
    Then I should be directed to "Log In" page

  Scenario: Navigate to search page
    Given I am on the favorite park list page
    When I click search button on update page
    Then I should be directed to search page

  Scenario: Navigate to compare list page
    Given I am on the favorite park list page
    When I click compareList button on update page
    Then I should be directed to compare page

  Scenario: Logout button is functional
    Given I am on the favorite park list page
    When I click Logout button on update page
    Then I should be directed to "Log In" page