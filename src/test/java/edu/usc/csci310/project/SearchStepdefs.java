package edu.usc.csci310.project;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;
import java.time.Duration;
import java.util.List;

import static java.awt.SystemColor.menu;
import static org.junit.jupiter.api.Assertions.*;

public class SearchStepdefs {
    private WebDriver driver = new ChromeDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            @After
    public void tearDown2() {
        if (driver != null) {
            driver.quit();
        }
    }
    @Given("I am on the search page")
    public void iAmOnTheSearchPage() {
        ChromeOptions options=new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);
        driver=new ChromeDriver(options);
        driver.get("https://localhost:8080");

        //sign up and then log in
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[1]/input")).sendKeys("username");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[3]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();

        //driver.get("http://localhost:8080/search");
    }
    @Given("I am on the search results page")
    public void iAmOnTheSearchResultsPage() throws InterruptedException {
        ChromeOptions options=new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);
        driver=new ChromeDriver(options);
        driver.get("https://localhost:8080");
        //sign up and then log in
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[1]/input")).sendKeys("username103");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[3]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username103");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        //
        Thread.sleep(3000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.sendKeys("park");
        //click search
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
    }

    @When("I enter {string} into search box")
    public void iEnterIntoSearchBox(String query) throws InterruptedException {
        Thread.sleep(3000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.sendKeys(query);
    }

    @And("click the search button")
    public void clickTheSearchButton() throws InterruptedException {

        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
    }

    @And("choose {string} from the search criteria")
    public void chooseFromTheSearchCriteria(String criteria) {
        String radioButtonId;

        // Determine which radio button to select based on the criteria
        switch (criteria.toLowerCase()) {
            case "park name":
                radioButtonId = "parkName";
                break;
            case "amenity":
                radioButtonId = "amenity";
                break;
            case "state":
                radioButtonId = "state";
                break;
            case "activity":
                radioButtonId = "activity";
                break;
            default:
                throw new IllegalArgumentException("Unsupported search criteria: " + criteria);
        }

        // Find the radio button by its ID and click it
        WebElement radioButton = driver.findElement(By.id(radioButtonId));
        if (!radioButton.isSelected()) {
            radioButton.click();
        }
    }

    @Then("I should see {int} parks listed in the search results")
    public void iShouldSeeParksListedInTheSearchResults(int count) throws InterruptedException {
        Thread.sleep(10000);
        List<WebElement> parks=driver.findElements(By.id("park-card"));
        // System.out.println("here"+parks);
        new WebDriverWait(driver,Duration.ofSeconds(100));
        assertEquals(count,parks.size());
    }

    @Then("I should see a list of parks that match the {string} search criteria of {string}")
    public void iShouldSeeAListOfParksThatMatchTheSearchCriteriaOf(String criteria, String query) {
        assertTrue(driver.getPageSource().contains(query));

    }

//    @Then("I should be redirected to search page")
//    public void iShouldBeRedirectedToSearchPage2() {
//        wait.until(ExpectedConditions.urlContains("/search"));
//        assertTrue(driver.getCurrentUrl().contains("/search"));
//    }

    @Then("I should see the message {string}")
    public void iShouldSeeTheMessage(String message) {
        // ExpectedConditions.textToBePresentInElementLocated(By.id("confirmation-message"), message);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4)); // Considering messages disappear after 3 seconds
        try {
            WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmation-message")));
            String actualMessage = messageElement.getText();
            assertEquals(message, actualMessage, "The expected confirmation message is not displayed.");
        } catch (TimeoutException e) {
            fail("Confirmation message was not displayed within the expected time.");
        }
    }

    @Then("I should see a list of parks that match the search criteria of {string}")
    public void iShouldSeeAListOfParksThatMatchTheSearchCriteriaOf(String arg0) throws InterruptedException {
        Thread.sleep(8000);
        WebElement parkname = driver.findElement(By.xpath("/html/body/div/div/div/div/div/div[1]/h3"));
        parkname.click();
        Thread.sleep(3000);
        assertTrue(driver.getPageSource().contains(arg0));
    }
    @And("I click the load more results button")
    public void iClickTheButton() throws InterruptedException {
        Thread.sleep(8000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement loadButton = driver.findElement(By.id("load-more-button"));
        js.executeScript("arguments[0].scrollIntoView(true);", loadButton);
        Thread.sleep(5000);
//        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/button"));
        loadButton.click();
    }
    @And("I click the favorites button on a park")
    public void iClickTheButtonOnAPark() throws InterruptedException {
        WebElement Button = driver.findElement(By.id("favorite-id"));
        Button.click();
    }


    @Then("I should see the favorite button")
    public void iShouldSeeTheFavoriteButton() {

        assertTrue(driver.getPageSource().contains("+"));
    }

    @When("I hover over the park card")
    public void iHoverOverTheParkCard() throws InterruptedException {
        Thread.sleep(10000);
        List<WebElement> parks=driver.findElements(By.id("park-card"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", parks.get(1));
        Actions action = new Actions(driver);
        action.moveToElement(parks.get(0)).perform();
        Thread.sleep(3000);

    }

    @When("I click the name of the park")
    public void iClickTheNameOfThePark() throws InterruptedException {
        Thread.sleep(8000);
        WebElement parkname = driver.findElement(By.xpath("/html/body/div/div/div/div/div/div[1]/h3"));
        parkname.click();
        Thread.sleep(3000);

    }

    @Then("I should see park details")
    public void iShouldSeeParkDetails() {
        assertTrue(driver.getPageSource().contains("Website"));
    }

    @When("I click home button on search page")
    public void iClickHomeButton() throws InterruptedException {
        Thread.sleep(3000);
        WebElement homeButton = driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[1]"));
        homeButton.click();
        Thread.sleep(3000);
    }


    @Then("I should be directed to {string} page")
    public void iShouldBeDirectedToPage(String arg0) {
        assertTrue(driver.getPageSource().contains(arg0));
    }

    @When("I click favoriteList button")
    public void iClickFavoriteListButton() throws InterruptedException {
        Thread.sleep(3000);
        WebElement favButton = driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[3]"));
        favButton.click();
        Thread.sleep(3000);
    }

    @When("I click compareList button on search page")
    public void iClickCompareListButton() throws InterruptedException {

        Thread.sleep(3000);
        WebElement compareButton = driver.findElement(By.xpath(" /html/body/div/div/div/header[1]/nav/ul/li[4]"));
        compareButton.click();
        Thread.sleep(3000);
    }

    @When("I click Logout button on search page")
    public void iClickLogoutButton() throws InterruptedException {
        Thread.sleep(3000);
        WebElement logoutButton = driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[5]"));
        logoutButton.click();
    }

    @And("press the enter key")
    public void pressTheEnterKey() {
        WebElement inputField = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        inputField.sendKeys(Keys.ENTER);
    }

    @And("delete all parks in favoriteList")
    public void deleteAllParksInFavoriteList() throws InterruptedException {
        Thread.sleep(10000);
        List<WebElement> deleteAllButton = driver.findElements(By.xpath("/html/body/div/div[1]/div/div/button"));

        // Check if the logout button exists
        if (!deleteAllButton.isEmpty()) {
            // Click on the first (and presumably only) logout button
            deleteAllButton.get(0).click();
            Thread.sleep(3000);
            List<WebElement> confirmButton = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[3]/button[2]"));
            confirmButton.get(0).click();

        }
        
    }

    @And("I click search button")
    public void iClickSearchButton() throws InterruptedException {
        Thread.sleep(3000);
        WebElement logoutButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/header[1]/nav/ul/li[2]"));
        logoutButton.click();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}


