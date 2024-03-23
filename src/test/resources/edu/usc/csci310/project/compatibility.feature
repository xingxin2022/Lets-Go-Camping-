Feature: Compatability with Chrome browser and mobile devices.
  Scenario: Accessing the application on the Chrome browser
    Given I have a device with the latest version of Chrome
    When I navigate to the web application URL
    Then the application should load and display the home page
    And I can interact with UI elements on the page

  Scenario: Accessing the application on a mobile device
    Given I have a mobile device with a web browser
    When I navigate to the web application URL
    Then the application should load and display the home page
    And should be responsive to the mobile screen size
    And I can interact with UI elements on the page

  Scenario: Accessing the application on a tablet device
    Given I have a tablet device with a web browser
    When I navigate to the web application URL
    Then the application should load and display the home page
    And should be responsive to the mobile screen size
    And I can interact with UI elements on the page

