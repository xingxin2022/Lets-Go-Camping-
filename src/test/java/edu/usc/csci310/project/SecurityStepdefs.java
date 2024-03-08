package edu.usc.csci310.project;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SecurityStepdefs {

    @Given("a registered user {string} with password {string}")
    public void aRegisteredUserWithPassword(String username, String password) {
    }

    @When("the user {string} logs in")
    public void theUserLogsIn(String username) {
    }

    @Then("the user {string} should be able to access his data")
    public void theUserShouldBeAbleToAccessHisData(String username) {
    }

    @And("the user {string} should not be able to access other users' data")
    public void theUserShouldNotBeAbleToAccessOtherUsersData(String username) {
    }

    @Then("the user {string} should not be able to access his data after logging out")
    public void theUserShouldNotBeAbleToAccessHisDataAfterLoggingOut(String username) {
    }

    @When("the user {string} fails to login {int} times")
    public void theUserFailsToLoginTimes(String username, Integer int1) {
    }

    @Then("the user {string} should not be able to login after the {int}th attempt")
    public void theUserShouldNotBeAbleToLoginAfterTheThAttempt(String username, Integer int1) {
    }

    @And("the user {string} should be able to login after waiting {int} minutes")
    public void theUserShouldBeAbleToLoginAfterWaitingMinutes(String username, Integer int1) {
    }

    @When("the user submits personal information")
    public void theUserSubmitsPersonalInformation() {
    }

    @Then("the user's personal information should be encrypted")
    public void theUserSPersonalInformationShouldBeEncrypted() {
    }

    @And("the user's personal information should be decrypted when accessed by the user")
    public void theUserSPersonalInformationShouldBeDecryptedWhenAccessedByTheUser() {
    }

    @When("the web application encounters an error")
    public void theWebApplicationEncountersAnError() {
    }

    @Then("the web application should display an error message")
    public void theWebApplicationShouldDisplayAnErrorMessage() {
    }

    @And("should not expose any sensitive information")
    public void shouldNotExposeAnySensitiveInformation() {
    }
}
