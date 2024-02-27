Feature: Suggest a Park to Visit for a Group of Users (Friends)

  Background:
    Given the user is logged in
    And has friends added to their network

  Scenario: Suggest a park based on user's past preferences
    Given the user has visited parks in the past
    When the user requests for a park suggestion
    Then the system suggests a park based on the user's past visited parks

  Scenario: Suggest a park based on a group's combined preferences
    Given the user is planning a trip with friends
    When the user requests a group park suggestion
    Then the system suggests a park based on the group's past visited parks

  Scenario: Suggest a park when there are no past preferences
    Given the user has no past visited parks
    When the user requests for a park suggestion
    Then the system suggests a popular park among app users

  Scenario: Update suggestion based on real-time availability
    Given the user requests for a park suggestion
    When the system identifies parks with no availability
    Then the system excludes those parks from the suggestions