package edu.usc.csci310.project;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginStepdefs {
    private static WebDriver driver;
    private static WebDriverWait wait;

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore--certificate-errors");
        options.addArguments("--ignore--ssl-errors");
        options.setAcceptInsecureCerts(true);
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Given("I am on the create account page")
    public void iAmOnTheCreateAccountPage() {
        driver.get("https://localhost:8080/signup");
    }

    @When("I enter {string} into signup username field")
    public void iEnterIntoSignupUsernameField(String username) {
        WebElement usernameInput = driver.findElement(By.id("usernameRegis"));
        usernameInput.sendKeys(username);
    }

    @And("I enter {string} into signup password field")
    public void iEnterIntoSignupPasswordField(String password) {
        WebElement passwordInput = driver.findElement(By.id("passwordRegis"));
        passwordInput.sendKeys(password);
    }

    @And("I enter {string} into signup confirm password field")
    public void iEnterIntoSignupConfirmPasswordField(String password) {
        WebElement passwordInput = driver.findElement(By.id("confirmPasswordRegis"));
        passwordInput.sendKeys(password);
    }

    @And("I click the signup button")
    public void iClickTheSignupButton() {
        WebElement signupButton = driver.findElement(By.xpath("//button[contains(text(), 'Sign Up')]"));
        signupButton.click();
    }

    @Then("I see sign up error {string}")
    public void iSeeError(String expectedMessage) {
        boolean expectingErrorMessage = true;

        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("response")));
        assertEquals(expectedMessage, messageElement.getText());

        if (expectingErrorMessage) {
            // check color is red
            String styleAttribute = messageElement.getAttribute("style");
            assertTrue(styleAttribute.contains("color: red;"));
        }
    }

    @And("the username {string} is already taken")
    public void theUsernameIsAlreadyTaken(String username) {
        WebElement password = driver.findElement(By.id("confirmPasswordRegis"));
        password.sendKeys("Password123");
        WebElement confirmPassword = driver.findElement(By.id("passwordRegis"));
        confirmPassword.sendKeys("Password123");
        WebElement usernameInput = driver.findElement(By.id("usernameRegis"));
        usernameInput.sendKeys(username);
        WebElement signupButton = driver.findElement(By.tagName("button"));
        signupButton.click();
        password.clear();
        confirmPassword.clear();
        usernameInput.clear();
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        driver.get("https://localhost:8080/login");
    }

    @And("I click the create account button")
    public void iClickTheCreateAccountButton() {
        WebElement signUpLink = driver.findElement(By.xpath("//span[text()=' Sign up here']"));
        signUpLink.click();
    }

    @Then("I should be on the sign up page")
    public void iShouldBeOnTheSignUpPage() {
        wait.until(ExpectedConditions.urlContains("/signup"));
        assertTrue(driver.getCurrentUrl().contains("/signup"));
    }

    @When("I click the button to navigate to the login page")
    public void iClickTheButtonToNavigateToTheLoginPage() {
        WebElement loginLink = driver.findElement(By.xpath("//span[contains(text(),'Back to Log-in')]"));
        loginLink.click();
    }

    @Then("I should be sent back to the login page")
    public void iShouldBeSentBackToTheLoginPage() {
        wait.until(ExpectedConditions.urlContains("localhost:8080"));
        assertTrue(driver.getCurrentUrl().equals("https://localhost:8080/"));
    }

    @And("I have an account with the username {string} and password {string}")
    public void iHaveAnAccountWithTheUsernameAndPassword(String username, String password) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:8080/register";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
//        map.add("username", username);
//        map.add("password", password);
//        map.add("confirmPassword", password);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
//
//        restTemplate.postForEntity(url, request, String.class);
    }

    @When("I enter {string} into login username field")
    public void iEnterIntoLoginUsernameField(String username) {
        WebElement usernameInput = driver.findElement(By.id("usernameLogin"));
        usernameInput.sendKeys(username);
    }

    @And("I enter {string} into login password field")
    public void iEnterIntoLoginPasswordField(String password) {
        WebElement passwordInput = driver.findElement(By.id("passwordLogin"));
        passwordInput.sendKeys(password);
    }

    @And("I click the login button")
    public void iClickTheLoginButton() {
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(), 'Log In')]"));
        loginButton.click();
    }

    @Then("I should be redirected to search page")
    public void iShouldBeRedirectedToSearchPage() {
        wait.until(ExpectedConditions.urlContains("/search"));
        assertTrue(driver.getCurrentUrl().contains("/search"));
    }

    @And("there is no account with the username {string}")
    public void thereIsNoAccountWithTheUsername(String username) {
        // no need to do anything since the user does not exist
    }

    @Then("I should see login error {string}")
    public void iShouldSeeLoginError(String expectedMessage) {
        WebElement errorMessageDiv = driver.findElement(By.cssSelector("div[data-testid='login-error']"));
        String actualErrorMessage = errorMessageDiv.getText();
        assertEquals(expectedMessage, actualErrorMessage);
    }

    @Given("I am on the signup page")
    public void iAmOnTheSignupPage() {
        driver.get("https://localhost:8080/signup");
    }

    @And("I click the cancel button")
    public void iClickTheCancelButton() {
        WebElement cancelButton = driver.findElement(By.xpath("//button[contains(text(), 'Cancel')]"));
        cancelButton.click();
    }


    @Then("I should see a confirmation dialog")
    public void iShouldSeeAConfirmationDialog() {
        // wait until the dialog is displayed
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cancelCancel")));
    }

    @And("I click the confirm button on the dialog")
    public void iClickTheCancelButtonOnTheDialog() {
        WebElement cancelButton = driver.findElement(By.id("confirmCancel"));
        cancelButton.click();
    }

    @Then("I should still be on the signup page")
    public void iShouldStillBeOnTheSignupPage() {
        wait.until(ExpectedConditions.urlContains("/signup"));
        assertTrue(driver.getCurrentUrl().contains("/signup"));
    }

    @And("I click the cancel button on the dialog")
    public void iClickTheConfirmButtonOnTheDialog() {
        WebElement cancelButton = driver.findElement(By.id("cancelCancel"));
        cancelButton.click();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

}
