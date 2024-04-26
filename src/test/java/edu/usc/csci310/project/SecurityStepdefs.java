package edu.usc.csci310.project;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecurityStepdefs {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--ignore-ssl-errors");
        options.setAcceptInsecureCerts(true);
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Given("I am using SSL for login page")
    public void iAmUsingSSLForLoginPage() {
        driver.get("https://localhost:8080/login");
    }

    @Given("I am not using SSL for login page")
    public void iAmNotUsingSSLForLoginPage() {
        driver.get("http://localhost:8080/login");
    }

    @Given("I am using SSL for signup page")
    public void iAmUsingSSLForSignupPage() {
        driver.get("https://localhost:8080/signup");
    }

    @And("I am using SSL for search page")
    public void iAmUsingSSLForSearchPage() {
    }

    @And("I am not using SSL for search page")
    public void iAmNotUsingSSLForSearchPage() {
        driver.get("http://localhost:8080/search");
    }

    @And("I am using SSL for compare page")
    public void iAmUsingSSLForComparePage() throws InterruptedException {
        Thread.sleep(500);
        driver.get("https://localhost:8080/compare");
    }

    @And("I am not using SSL for compare page")
    public void iAmNotUsingSSLForComparePage() {
        driver.get("http://localhost:8080/compare");
    }

    @And("I am using SSL for favorite list page")
    public void iAmUsingSSLForFavoriteListPage() throws InterruptedException{
        Thread.sleep(500);
        driver.get("https://localhost:8080/favorite");
    }

    @And("I am not using SSL for favorite list page")
    public void iAmNotUsingSSLForFavoriteListPage() {
        driver.get("http://localhost:8080/favorite");
    }

    @Given("I am logged in")
    public void iamLoggedIn() throws InterruptedException {
        driver.get("https://localhost:8080");

        //sign up and then log in
        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[1]/input")).sendKeys("user3");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[3]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();

        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("user3");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        Thread.sleep(2000);
    }

    @When("I access the login page")
    public void iAccessTheLoginPage() {
    }

    @Then("I should see the login page")
    public void iShouldSeeTheLoginPage() throws InterruptedException {
        // wait until "Log in" button is visible
        Thread.sleep(1000);
        assertTrue(driver.getCurrentUrl().equals("https://localhost:8080/"));
    }

    @Then("I should not see the login page")
    public void iShouldNotSeeTheLoginPage() {
        // wait until "Log in" button is visible
        assertFalse(driver.getPageSource().contains("Log in"));
    }

    @When("I access the search page")
    public void iAccessTheSearchPage() {
    }

    @Then("I should see the search page")
    public void iShouldSeeTheSearchPage() throws InterruptedException {
        // wait until "Search" button is visible
        Thread.sleep(1000);
        assertTrue(driver.getCurrentUrl().contains("/search"));
    }

    @Then("I should not see the search page")
    public void iShouldNotSeeTheSearchPage() {
        // wait until "Search" button is visible
        assertFalse(driver.getPageSource().contains("Search By"));
    }

    @When("I access the compare page")
    public void iAccessTheComparePage() {
    }

    @Then("I should see the compare page")
    public void iShouldSeeTheComparePage() throws InterruptedException{
        // wait until "Compare" button is visible
        Thread.sleep(1000);
        assertTrue(driver.getCurrentUrl().contains("/compare"));
    }

    @Then("I should not see the compare page")
    public void iShouldNotSeeTheComparePage() {
        // wait until "Compare" button is visible
        assertFalse(driver.getPageSource().contains("COMPARE NOW"));
    }

    @When("I access the favorite list page")
    public void iAccessTheFavoriteListPage() {
    }

    @Then("I should see the favorite list page")
    public void iShouldSeeTheFavoriteListPage() throws InterruptedException {
        Thread.sleep(1000);
        assertTrue(driver.getCurrentUrl().contains("/favorite"));
    }

    @Then("I should not see the favorite list page")
    public void iShouldNotSeeTheFavoriteListPage() {
        // wait until "Favorite List" button is visible
        assertFalse(driver.getPageSource().contains("Favorite Parks"));
    }

    @Given("I am not logged in")
    public void iAmNotLoggedIn() {
    }

    @When("I access the signup page")
    public void iAccessTheSignupPage() {
        driver.get("https://localhost:8080/signup");
    }

    @Then("I should see the signup page")
    public void iShouldSeeTheSignupPage() {
        // wait until "Sign up" button is visible
        wait.until(driver -> driver.getPageSource().contains("Confirm Password"));
        assertTrue(driver.getPageSource().contains("Confirm Password"));
    }

//    @After
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//            driver = null;
//        }
//    }

}
