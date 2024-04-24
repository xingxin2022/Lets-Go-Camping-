Feature: Web-app Accessibility for Disabled
  Scenario: Ensure keyboard navigation with TAB
    Given the user is navigating any page
    When the user inputs "TAB" key
    Then the next element is hovered
  Scenario: Ensure keyboard navigation with ENTER
    Given the user is navigating any page
    When the user inputs "TAB" key
    And the user inputs "ENTER" key
    Then the selected element is clicked
  Scenario: Provide alternative text for images
    Given the user is browsing any page
    When the user encounters images or icons
    Then each image or icon has alternative text
  Scenario: Offer text to speech for content
    Given the user is browing any page
    When the user presses "Text-to-Speech Mode" button
    Then text content is read out load
  Scenario: Offer high-contrast mode for images
    Given the user is browsing any page
    When the user presses "High-Contrast Mode" button
    Then images are switched to high-contrast mode
