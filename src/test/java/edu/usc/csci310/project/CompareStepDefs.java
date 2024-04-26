package edu.usc.csci310.project;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompareStepDefs {
    private WebDriver driver = new ChromeDriver();
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    //CANT DO YET
    @And("my friend has a {string} list")
    public void myFriendHasAList(String visibility) {
        // NOT DONE YET
    }

    //DONE
    @Given("I have a list with {string} and {string}")
    public void iHaveAListWithAnd(String park1, String park2) throws InterruptedException {

        ChromeOptions options=new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);


        driver=new ChromeDriver(options);
        driver.get("https://localhost:8080");

        //sign up and then log in
        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[1]/input")).sendKeys("username_me");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[3]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();

        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username_me");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();

        //FIRST PARK
        //input search
        Thread.sleep(1000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.sendKeys(park1);
        //click search
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();

        //hover over park card
        Thread.sleep(10000);
        WebElement parkContainer = driver.findElement(By.xpath("//div[contains(@class, 'park-container') " +
                "and .//h3[text()='" + park1 + "']]"));
        Actions action = new Actions(driver);
        action.moveToElement(parkContainer).perform();
        Thread.sleep(5000);

        //add park
        driver.findElement(By.xpath("//button[@data-testid='addToFav']")).click();
        Thread.sleep(1000);

        //SECOND PARK
        //input search
        Thread.sleep(1000);
        searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.clear();
        searchBox.sendKeys(park2);
        //click search
        searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(5000);

        //hover over park card
        Thread.sleep(1000);
        parkContainer = driver.findElement(By.xpath("//div[contains(@class, 'park-container') " +
                "and .//h3[text()='" + park2 + "']]"));
        action.moveToElement(parkContainer).perform();
        Thread.sleep(1000);

        //add park
        driver.findElement(By.xpath("//button[@data-testid='addToFav']")).click();
        Thread.sleep(1000);

        //log out right now
        driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[5]")).click();
        Thread.sleep(1000);

    }

    //DONE
    @And("my friend has a list with {string} and {string}")
    public void myFriendHasAListWithAnd(String park1, String park2) throws InterruptedException {

        Thread.sleep(1000);
        //sign up and then log in
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[1]/input")).sendKeys("username_friend");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[3]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username_friend");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();

        //FIRST PARK
        //input search
        Thread.sleep(1000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.sendKeys(park1);
        //click search
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(5000);

        //hover over park card
        Thread.sleep(1000);
        WebElement parkContainer = driver.findElement(By.xpath("//div[contains(@class, 'park-container') " +
                "and .//h3[text()='" + park1 + "']]"));
        Actions action = new Actions(driver);
        action.moveToElement(parkContainer).perform();
        Thread.sleep(1000);

        //add park
        driver.findElement(By.xpath("//button[@data-testid='addToFav']")).click();
        Thread.sleep(1000);

        //SECOND PARK
        //input search
        Thread.sleep(1000);
        searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.clear();
        searchBox.sendKeys(park2);
        //click search
        searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(5000);

        //hover over park card
        Thread.sleep(1000);
        parkContainer = driver.findElement(By.xpath("//div[contains(@class, 'park-container') " +
                "and .//h3[text()='" + park2 + "']]"));
        action.moveToElement(parkContainer).perform();
        Thread.sleep(1000);

        //add park
        driver.findElement(By.xpath("//button[@data-testid='addToFav']")).click();
        Thread.sleep(1000);

        //Sign out
        driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[5]")).click();
        Thread.sleep(1000);
    }

    //DONE
    @When("I compare my list with my friend's list")
    public void iCompareMyListWithMyFriendsList() throws InterruptedException {

        //log in
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username_me");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        Thread.sleep(3000);

        //go to compare
        driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[4]")).click();
        Thread.sleep(5000);

        //select both
        driver.findElement(By.xpath("//div[@data-testid='user-username_friend']")).click();
        driver.findElement(By.xpath("//div[@data-testid='user-username_me']")).click();

        //click compare now
        driver.findElement(By.xpath("/html/body/div/div/div/div/div[1]/button[2]")).click();
        Thread.sleep(5000);
    }

    //DONE
    @Then("I should see a list with {string}, {string}, and {string}")
    public void iShouldSeeAListWithAnd(String park1, String park2, String park3) {
        assertTrue(driver.getPageSource().contains(park1),
                "Page does not contain expected text: " + park1);
        assertTrue(driver.getPageSource().contains(park2),
                "Page does not contain expected text: " + park2);
        assertTrue(driver.getPageSource().contains(park3),
                "Page does not contain expected text: " + park3);
    }

    //DONE
    @Then("I should see {string}")
    public void iShouldSee(String message) {
        assertTrue(driver.getPageSource().contains(message),
                "Page does not contain expected text: " + message);
    }

    //DONE
    @Then("I should see the message {string}")
    public void iShouldSeeTheMessage(String arg0) {
        assertTrue(driver.getPageSource().contains(arg0),
                "Page does not contain expected text: " + arg0);
    }

    //DONE
    @Then("I should see a list with parks from both lists")
    public void iShouldSeeAListWithParksFromBothLists() {
        assertTrue(driver.getPageSource().contains("Grand Teton National Park"),
                "Page does not contain expected text: " + "Grand Teton National Park");
        assertTrue(driver.getPageSource().contains("Yellowstone National Park"),
                "Page does not contain expected text: " + "Yellowstone National Park");
    }

    //DONE
    @Then("I should see a list with my name, first friends name, and second friends name")
    public void iShouldSeeAListWithMyNameFirstFriendsNameAndSecondFriendsName() {
        assertTrue(driver.getPageSource().contains("username_firstFriend"),
                "Page does not contain expected text: " + "username_firstFriend");
        assertTrue(driver.getPageSource().contains("username_secondFriend"),
                "Page does not contain expected text: " + "username_secondFriend");
    }

    //DONE
    @And("my friend does not exist")
    public void myFriendDoesNotExist() throws InterruptedException {

        //log in
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username_me");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();

        //go to compare
        driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[4]")).click();
        Thread.sleep(5000);

        //type his name
        driver.findElement(By.xpath("/html/body/div/div/div/div/div[1]/input")).sendKeys("username_donotexist");

        //press submit
        driver.findElement(By.xpath("/html/body/div/div/div/div/div[1]/button[1]")).click();

        assertTrue(driver.getPageSource().contains("User not found."),
                "Page does not contain expected text: " + "User not found.");

        //log out right now
        driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[5]")).click();
        Thread.sleep(1000);
    }

    //DONE
    @And("first friend has a list with {string} and {string}")
    public void firstFriendHasAListWithAnd(String arg0, String arg1) throws InterruptedException {

        driver.get("https://localhost:8080");

        //sign up and then log in
        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[1]/input")).sendKeys("username_firstFriend");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[3]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();

        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username_firstFriend");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();

        //FIRST PARK
        //input search
        Thread.sleep(1000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.sendKeys(arg0);
        //click search
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(5000);

        //hover over park card
        Thread.sleep(1000);
        WebElement parkContainer = driver.findElement(By.xpath("//div[contains(@class, 'park-container') " +
                "and .//h3[text()='" + arg0 + "']]"));
        Actions action = new Actions(driver);
        action.moveToElement(parkContainer).perform();
        Thread.sleep(1000);

        //add park
        driver.findElement(By.xpath("//button[@data-testid='addToFav']")).click();
        Thread.sleep(1000);

        //SECOND PARK
        //input search
        Thread.sleep(1000);
        searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.clear();
        searchBox.sendKeys(arg1);
        //click search
        searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(5000);

        //hover over park card
        Thread.sleep(1000);
        parkContainer = driver.findElement(By.xpath("//div[contains(@class, 'park-container') " +
                "and .//h3[text()='" + arg1 + "']]"));
        action.moveToElement(parkContainer).perform();
        Thread.sleep(1000);

        //add park
        driver.findElement(By.xpath("//button[@data-testid='addToFav']")).click();
        Thread.sleep(1000);

        //log out right now
        driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[5]")).click();
        Thread.sleep(1000);

    }

    //DONE
    @And("second friend has a list with {string} and {string}")
    public void secondFriendHasAListWithAnd(String arg0, String arg1) throws InterruptedException {

        //sign up and then log in
        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[1]/input")).sendKeys("username_secondFriend");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[3]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();

        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username_secondFriend");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();

        //FIRST PARK
        //input search
        Thread.sleep(1000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.sendKeys(arg0);
        //click search
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(5000);

        //hover over park card
        Thread.sleep(1000);
        WebElement parkContainer = driver.findElement(By.xpath("//div[contains(@class, 'park-container') " +
                "and .//h3[text()='" + arg0 + "']]"));
        Actions action = new Actions(driver);
        action.moveToElement(parkContainer).perform();
        Thread.sleep(1000);

        //add park
        driver.findElement(By.xpath("//button[@data-testid='addToFav']")).click();
        Thread.sleep(1000);

        //SECOND PARK
        //input search
        Thread.sleep(1000);
        searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.clear();
        searchBox.sendKeys(arg1);
        //click search
        searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(5000);

        //hover over park card
        Thread.sleep(1000);
        parkContainer = driver.findElement(By.xpath("//div[contains(@class, 'park-container') " +
                "and .//h3[text()='" + arg1 + "']]"));
        action.moveToElement(parkContainer).perform();
        Thread.sleep(1000);

        //add park
        driver.findElement(By.xpath("//button[@data-testid='addToFav']")).click();
        Thread.sleep(1000);

        //log out right now
        driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[5]")).click();
        Thread.sleep(1000);
    }

    //DONE
    @When("I click the logout button")
    public void iClickTheLogoutButton() throws InterruptedException {
        //log out right now
        driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[5]")).click();
        Thread.sleep(1000);
    }

    //DONE
    @And("I should be logged out")
    public void iShouldBeLoggedOut() {
        assertFalse(driver.getPageSource().contains("Home"),
                "Page still contains home - not signed out.");
    }

    //DONE
    @Then("I should be taken to the login page")
    public void iShouldBeTakenToTheLoginPage() {
        assertTrue(driver.getPageSource().contains("Log in"),
                "Page does not contain expected text: " + "Log in");
    }

    //DONE
    @Then("I should be on the {string} page")
    public void iShouldBeOnThePage(String arg0) {
        assertTrue(driver.getPageSource().contains(arg0),
                "Page does not contain expected text: " + arg0);
    }

    //DONE
    @And("I click on {string}")
    public void iClickOn(String arg0) {

        driver.findElement(By.xpath("//span[starts-with(@data-testid, 'park-name-') and contains(text(), '" + arg0 + "')]")).click();

    }

    //DONE
    @Given("I am on the compare list page")
    public void iAmOnTheCompareListPage() throws InterruptedException {

        ChromeOptions options=new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);


        driver=new ChromeDriver(options);
        driver.get("https://localhost:8080");

        //sign up and then log in
        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[1]/input")).sendKeys("username_me");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[3]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();

        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username_me");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();

        Thread.sleep(2000);

        //log out right now
        driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[4]")).click();
        Thread.sleep(1000);

    }

    //DONE
    @Given("I am on the search results page")
    public void iAmOnTheSearchResultsPage() throws InterruptedException {

        ChromeOptions options=new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);


        driver=new ChromeDriver(options);
        driver.get("https://localhost:8080");

        //sign up and then log in
        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[1]/input")).sendKeys("username_me");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[3]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();

        Thread.sleep(1000);
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username_me");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();


        Thread.sleep(2000);
        //log out right now
        driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[2]")).click();
        Thread.sleep(1000);


    }

    //DONE
    @Then("I should see the message {string} for {string}")
    public void iShouldSeeTheMessageFor(String arg0, String arg1) {

        String expectedPattern = arg1 + " (" + arg0 + ")";

        // Find all span elements that could potentially match the pattern
        List<WebElement> spans = driver.findElements(By.xpath("//span"));

        // Check if any of the spans contain the expected text pattern
        boolean found = spans.stream()
                .anyMatch(span -> span.getText().contains(expectedPattern));

        // Assert that the expected pattern was found
        assertTrue(found, "No span element contains the expected text pattern: " + expectedPattern);
    }

    //DONE
    @Then("I should see the details for {string}")
    public void iShouldSeeTheDetailsFor(String arg0) {
        assertTrue(driver.getPageSource().contains(arg0),
                "Page does not contain the details for: " + arg0);
    }

    @Then("the list should be ordered with {string} first")
    public void theListShouldBeOrderedWithFirst(String parkName) {
        List<WebElement> parks = driver.findElements(By.xpath("//li[contains(@data-testid, 'park-item')]/span"));
        assertTrue(parks.size() > 0, "The parks list is empty.");
        assertTrue(parks.get(0).getText().trim().contains(parkName), "The first park in the list is not as expected.");
    }

    @Then("the list should be ordered with {string} second")
    public void theListShouldBeOrderedWithSecond(String parkName) {
        List<WebElement> parks = driver.findElements(By.xpath("//li[contains(@data-testid, 'park-item')]/span"));
        assertTrue(parks.size() > 1, "The parks list does not have a second item.");
        assertTrue(parks.get(1).getText().trim().contains(parkName), "The second park in the list is not as expected.");
    }

    @Then("the list should be ordered with {string} third")
    public void theListShouldBeOrderedWithThird(String parkName) {
        List<WebElement> parks = driver.findElements(By.xpath("//li[contains(@data-testid, 'park-item')]/span"));
        assertTrue(parks.size() > 2, "The parks list does not have a third item.");
        assertTrue(parks.get(2).getText().trim().contains(parkName), "The third park in the list is not as expected.");
    }




    //DONE
    @And("I hover over the {string}")
    public void iHoverOverThe(String arg0) throws InterruptedException {

        Thread.sleep(1000);
        WebElement toHover = driver.findElement(By.xpath("//span[starts-with(@data-testid, 'park-name-') and contains(text(), '" + arg0 + "')]"));

        Actions action = new Actions(driver);
        action.moveToElement(toHover).perform();
        Thread.sleep(1000);
    }

    @When("I compare my list with both of my friend's list")
    public void iCompareMyListWithBothOfMyFriendSList() throws InterruptedException {
        //log in
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username_me");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        Thread.sleep(3000);

        //go to compare
        driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[4]")).click();
        Thread.sleep(5000);

        //select both
        driver.findElement(By.xpath("//div[@data-testid='user-username_firstFriend']")).click();
        driver.findElement(By.xpath("//div[@data-testid='user-username_secondFriend']")).click();
        driver.findElement(By.xpath("//div[@data-testid='user-username_me']")).click();

        //click compare now
        driver.findElement(By.xpath("/html/body/div/div/div/div/div[1]/button[2]")).click();
        Thread.sleep(5000);
    }

    @When("I click on the favorite list button in the header")
    public void iClickOnTheFavoriteListButtonInTheHeader() {
        String xpath = "//nav//ul//li[contains(text(), '" + "FavoriteList" + "')]";
        driver.findElement(By.xpath(xpath)).click();
    }

    @When("I click on the search button in the header")
    public void iClickOnTheSearchButtonInTheHeader() {
        String xpath = "//nav//ul//li[contains(text(), '" + "Search" + "')]";
        driver.findElement(By.xpath(xpath)).click();
    }
}
