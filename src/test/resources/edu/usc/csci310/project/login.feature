Feature: Login page with create account and login functionality.
  Scenario: password not matching requirements
    Given I am on the create account page
    When I enter "Password" into the password field
    And I enter "username" into the username field
    And I enter "Password" into the confirm password field
    Then I should see an error message "Password must contain at least one digit"

  Scenario: two passwords do not match
    Given I am on the create account page
    When I enter "Password1" into the password field
    And I enter "Password2" into the confirm password field
    And I enter "username" into the username field
    Then I should see an error message "Passwords do not match"

  Scenario: only one password field is filled
    Given I am on the create account page
    When I enter "Password1" into the password field
    And I enter "username" into the username field
    Then I should see an error message "Please fill out both password fields"

  Scenario: username is taken
    Given I am on the create account page
    When the username "username" is already taken
    And I enter "Password1" into the password field
    And I enter "Password1" into the confirm password field
    And I enter "username1" into the username field
    Then I should see an error message "Username is already taken"

  Scenario: navigating to the create account page
    Given I am on the login page
    When I click the create account button
    Then I should be on the create account page

  Scenario: confirm cancel action when creating account
    Given I am on the login page
    When I click the cancel button
    Then I should see a confirmation dialog
    And I click the confirm button
    Then I should be on the home page

  Scenario: cancel the cancel action when creating account
    Given I am on the login page
    When I click the cancel button
    Then I should see a confirmation dialog
    And I click the cancel button
    Then I should still be on the create account page

  Scenario: successful login
    Given I am on the login page
    And I have an account with the username "username" and password "Password1"
    When I enter "username" into the username field
    And I enter "Password1" into the password field
    And I click the login button
    Then I should see the message "Login Successful"