Feature: Login page with create account and login functionality.
  Scenario:
    Given I am on the login page
    When I enter "Tommy" into username,"1234" into password, and "1234" into re-enter password on the create account form
    And I click the "Create Account" button
    Then I should see the message "Password must have 1 uppercase letter, 1 lowercase letter, 1 number, and 1 number."

    Scenario:
      Given I am on the login page
      When I enter "Tommy" into username,"Ab1234" into password, and "Ab123" into re-enter password on the create account form
      And I click the "Create Account" button
      Then I should see the message "Passwords do not match."

    Scenario:
        Given I am on the login page
        When I enter "Tommy" into username,"Ab1234" into password on the create account form
        And I click the "Create Account" button
        Then I should see the message "Please re-enter your password."

    Scenario:
        Given I am on the login page
        When I enter "Tommy" into username,"Ab1234" into password, and "Ab1234" into re-enter password on the create account form
        And I click the "Create Account" button
        Then I should see the message "Account created successfully."


