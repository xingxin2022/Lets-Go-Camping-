package edu.usc.csci310.project;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SearchStepdefs {
    @Given("the user is logged into the camping trip planner application")
    public void theUserIsLoggedIntoTheCampingTripPlannerApplication() {
    }

    @When("the user navigates to the {string} section")
    public void theUserNavigatesToTheSearchParksSection() {
    }

    @And("selects {string} as the search criterion")
    public void selectsLocationAsTheSearchCriterion() {
    }

    @And("enters {string} into the location field")
    public void entersIntoTheLocationField(String arg0) {
    }

    @Then("the system should display all national parks located in Colorado")
    public void theSystemShouldDisplayAllNationalParksLocatedInColorado() {
    }

    @Given("the user is on the {string} page of the application")
    public void theUserIsOnTheSearchParksPageOfTheApplication() {
    }

    @When("the user selects {string} as the search criterion")
    public void theUserSelectsActivitiesAsTheSearchCriterion() {
    }

    @And("checks {string} and {string} in the activities options")
    public void checksHikingAndWildlifeViewingInTheActivitiesOptions() {
    }

    @Then("the system should display all national parks offering hiking and wildlife viewing activities")
    public void theSystemShouldDisplayAllNationalParksOfferingHikingAndWildlifeViewingActivities() {
    }

    @Given("the user is using the {string} feature")
    public void theUserIsUsingTheSearchParksFeature() {
    }

    @And("specifies a range of {string}")
    public void specifiesARangeOf(String arg0) {
    }

    @Then("the system should list all national parks larger than {int} square miles")
    public void theSystemShouldListAllNationalParksLargerThanSquareMiles(int arg0) {
    }

    @Given("the user is logged into the web application")
    public void theUserIsLoggedIntoTheWebApplication() {
    }

    @When("the user navigates to {string}")
    public void theUserNavigatesToSearchParks() {
    }

    @And("selects {string} as the filter")
    public void selectsRatingsAsTheFilter() {
    }

    @And("chooses a minimum rating of {int} stars")
    public void choosesAMinimumRatingOfStars(int arg0) {
    }

    @Then("the system should display all national parks with an average rating of {int} stars or higher")
    public void theSystemShouldDisplayAllNationalParksWithAnAverageRatingOfStarsOrHigher(int arg0) {
    }

    @Given("the user is on the {string} interface")
    public void theUserIsOnTheSearchParksInterface() {
    }

    @When("the user applies the {string} filter")
    public void theUserAppliesTheAccessibilityFilter() {
    }

    @And("selects {string} from the accessibility features")
    public void selectsWheelchairAccessibleFromTheAccessibilityFeatures() {
    }

    @Then("the system should display all national parks with wheelchair accessibility options")
    public void theSystemShouldDisplayAllNationalParksWithWheelchairAccessibilityOptions() {
    }
}
