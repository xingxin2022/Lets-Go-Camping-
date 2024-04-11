Feature: Login page with create account and login.
  Scenario: password not matching requirements
    Given I am on the create account page
    When I enter "Password" into signup password field
    And I enter "username" into signup username field
    And I enter "Password" into signup confirm password field
    And I click the signup button
    Then I see sign up error "Password must contain at least one digit"

  Scenario: two passwords do not match
    Given I am on the create account page
    When I enter "Password1" into signup password field
    And I enter "Password2" into signup confirm password field
    And I enter "username" into signup username field
    And I click the signup button
    Then I see sign up error "Passwords do not match"

  Scenario: only one password field is filled
    Given I am on the create account page
    When I enter "Password1" into signup password field
    And I enter "username" into signup username field
    And I click the signup button
    Then I see sign up error "Please fill out both password fields"

  Scenario: username is taken
    Given I am on the create account page
    When the username "username1" is already taken
    And I enter "Password1" into signup password field
    And I enter "Password1" into signup confirm password field
    And I enter "username1" into signup username field
    And I click the signup button
    Then I see sign up error "Username already exists"

  Scenario: navigating to the create account page
    Given I am on the login page
    When I click the create account button
    Then I should be on the sign up page

  Scenario: navigating to the login page
    Given I am on the create account page
    When I click the button to navigate to the login page
    Then I should be sent back to the login page

#  Scenario: confirm cancel action when creating account
#    Given I am on the login page
#    When I click the cancel button
#    Then I should see a confirmation dialog
#    And I click the confirm button
#    Then I should be on the home page
#
#  Scenario: cancel the cancel action when creating account
#    Given I am on the login page
#    When I click the cancel button
#    Then I should see a confirmation dialog
#    And I click the cancel button
#    Then I should still be on the create account page

  Scenario: successful login
    Given I am on the login page
    And I have an account with the username "username1" and password "Password123"
    When I enter "username1" into login username field
    And I enter "Password123" into login password field
    And I click the login button
    Then I should be redirected to search page

  Scenario: login with non-existent username
    Given I am on the login page
    And there is no account with the username "username"
    When I enter "username" into login username field
    And I enter "Password123" into login password field
    And I click the login button
    Then I should see login error "Username does not exist"


  Scenario: login with incorrect password
    Given I am on the login page

    And I have an account with the username "username1" and password "Password123"
    When I enter "username1" into login username field
    And I enter "Password1" into login password field
    And I click the login button
    Then I should see login error "Invalid username or password"
