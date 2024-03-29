Feature: Security and Protection of User Data
  Scenario: User data is protected
    Given a registered user "tommytrojan" with password "FightOn"
    When the user "tommytrojan" logs in
    Then the user "tommytrojan" should be able to access his data
    And the user "tommytrojan" should not be able to access other users' data

  Scenario: User session is protected
    Given a registered user "tommytrojan" with password "FightOn"
    When the user "tommytrojan" logs in
    Then the user "tommytrojan" can access his data
    And the user "tommytrojan" cannot access data after logging out

  Scenario: User fails to login after 5 attempts
    Given a registered user "tommytrojan" with password "FightOn"
    When the user "tommytrojan" fails to login 5 times
    Then the user "tommytrojan" cannot login after the 5th attempt
    And the user "tommytrojan" can login after waiting 5 minutes

  Scenario: User data is encrypted
    Given a registered user "tommytrojan" with password "FightOn"
    When the user submits personal information
    Then the user's personal information should be encrypted
    And the user's information should be decrypted when accessed by the user

  Scenario: Web application error handling
    Given the web application is running
    When the web application encounters an error
    Then the web application should display an error message
    And should not expose any sensitive information
