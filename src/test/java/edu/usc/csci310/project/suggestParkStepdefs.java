package edu.usc.csci310.project;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class suggestParkStepdefs {
    @And("has friends added to their network")
    public void hasFriendsAddedToTheirNetwork() {

    }

    @Given("the user is logged in")
    public void theUserIsLoggedIn() {
    }

    @Given("the user has visited parks in the past")
    public void theUserHasVisitedParksInThePast() {
    }

    @When("the user requests for a park suggestion")
    public void theUserRequestsForAParkSuggestion() {
    }

    @Then("the system suggests a park based on the user's past visited parks")
    public void theSystemSuggestsAParkBasedOnTheUserSPastVisitedParks() {
    }

    @Given("the user is planning a trip with friends")
    public void theUserIsPlanningATripWithFriends() {
    }

    @When("the user requests a group park suggestion")
    public void theUserRequestsAGroupParkSuggestion() {
    }

    @Then("the system suggests a park based on the group's past visited parks")
    public void theSystemSuggestsAParkBasedOnTheGroupSPastVisitedParks() {
    }

    @Given("the user has no past visited parks")
    public void theUserHasNoPastVisitedParks() {
    }

    @Then("the system suggests a popular park among app users")
    public void theSystemSuggestsAPopularParkAmongAppUsers() {
    }

    @When("the system identifies parks with no availability")
    public void theSystemIdentifiesParksWithNoAvailability() {
    }

    @Then("the system excludes those parks from the suggestions")
    public void theSystemExcludesThoseParksFromTheSuggestions() {
    }
}
