Feature: Security and Protection of User Data
  Scenario: Using SSL for login page
    Given I am using SSL for login page
    When I access the login page
    Then I should see the login page

  Scenario: Not using SSL for login page
    Given I am not using SSL for login page
    When I access the login page
    Then I should not see the login page

  Scenario: Using SSL for search page
    Given I am logged in
    And I am using SSL for search page
    When I access the search page
    Then I should see the search page

  Scenario: Not using SSL for search page
    Given I am logged in
    And I am not using SSL for search page
    When I access the search page
    Then I should not see the search page

  Scenario: Using SSL for compare page
    Given I am logged in
    And I am using SSL for compare page
    When I access the compare page
    Then I should see the compare page

  Scenario: Not using SSL for compare page
    Given I am logged in
    And I am not using SSL for compare page
    When I access the compare page
    Then I should not see the compare page

  Scenario: Using SSL for favorite list page
    Given I am logged in
    And I am using SSL for favorite list page
    When I access the favorite list page
    Then I should see the favorite list page

  Scenario: Not using SSL for favorite list page
    Given I am logged in
    And I am not using SSL for favorite list page
    When I access the favorite list page
    Then I should not see the favorite list page

  Scenario: Can access login page without login
    Given I am not logged in
    And I am using SSL for login page
    When I access the login page
    Then I should see the login page

  Scenario: Can access signup page without login
    Given I am not logged in
    When I access the signup page
    Then I should see the signup page

  Scenario: Cannot access search page without login
    Given I am not logged in
    When I access the search page
    Then I should not see the search page

  Scenario: Cannot access compare page without login
    Given I am not logged in
    When I access the compare page
    Then I should not see the compare page

  Scenario: Cannot access favorite list page without login
    Given I am not logged in
    When I access the favorite list page
    Then I should not see the favorite list page
