package edu.usc.csci310.project;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DetailsStepdefs {
    private WebDriver driver = new ChromeDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    //    @BeforeEach()
//    public void showSearchResult() throws InterruptedException {
//
//
//    }
    @Given("the park is on my favorite list")
    public void theparkisonmyfavoritelist() throws InterruptedException {
        ChromeOptions options=new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);
        driver=new ChromeDriver(options);
        driver.get("https://localhost:8080");
        //sign up and then log in
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[1]/input")).sendKeys("username10");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[3]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username10");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        //input search
        Thread.sleep(3000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.sendKeys("park");
        //click search
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(3000);
        //hover over park card
        Thread.sleep(10000);
        List<WebElement> parks=driver.findElements(By.id("park-card"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", parks.get(0));
        Actions action = new Actions(driver);
        action.moveToElement(parks.get(0)).perform();
        Thread.sleep(3000);
        //add park
        WebElement Button = driver.findElement(By.id("favorite-id"));
        Button.click();
        Thread.sleep(8000);
    }

    @When("I click on the park in search results")
    public void iClickOnTheParkInSearchResults() throws InterruptedException {

        //clicking park name
        Thread.sleep(8000);
        WebElement parkname = driver.findElement(By.xpath("/html/body/div/div/div/div/div/div[1]/h3"));
        parkname.click();
        Thread.sleep(3000);
    }

    @Given("I am on the park details page")
    public void iAmOnTheParkDetailsPage() throws InterruptedException {
        //clicking park name
        Thread.sleep(8000);
        WebElement parkname = driver.findElement(By.xpath("/html/body/div/div/div/div/div/div[1]/h3"));
        parkname.click();
        Thread.sleep(3000);
        //checking if park details show
        assertTrue(driver.getPageSource().contains("Website"));
    }

    @Then("I should see the park details and the park is on my favorite list")
    public void iShouldSeeTheParkDetailsAndTheParkIsOnMyFavoriteList() {
        //checking if park details show
        assertTrue(driver.getPageSource().contains("Website"));
        //checking if park is on my favorite list
        assertTrue(driver.getPageSource().contains( " 🌟️ " ));

    }

    @Given("the park is not on my favorite list")
    public void theParkIsNotOnMyFavoriteList() throws InterruptedException {
        ChromeOptions options=new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);
        driver=new ChromeDriver(options);
        driver.get("https://localhost:8080");
        //sign up and then log in
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[1]/input")).sendKeys("username101");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[3]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username101");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        //go to fav list
        Thread.sleep(3000);
        WebElement favButton = driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[3]"));
        favButton.click();
        //delete all parks in fav list
        Thread.sleep(5000);
        List<WebElement> deleteAllButton = driver.findElements(By.xpath("/html/body/div/div[1]/div/div/button"));

        // Check if the  button exists
        if (!deleteAllButton.isEmpty()) {
            // Click on the first (and presumably only) logout button
            deleteAllButton.get(0).click();
            Thread.sleep(3000);
            List<WebElement> confirmButton = driver.findElements(By.xpath("/html/body/div[3]/div/div/div[3]/button[2]"));
            confirmButton.get(0).click();

        }
        //go to search page
        Thread.sleep(3000);
        WebElement logoutButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/header[1]/nav/ul/li[2]"));
        logoutButton.click();
        //input search
        Thread.sleep(3000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.sendKeys("park");
        //click search
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(3000);
    }

    @Then("I should see the park details and the park is not on my favorite list")
    public void iShouldSeeTheParkDetailsAndTheParkIsNotOnMyFavoriteList() {
        //checking if park details show
        assertTrue(driver.getPageSource().contains("Website"));
        //checking if park is on my favorite list
        assertFalse(driver.getPageSource().contains("🌟️"));
    }

    @When("I click on the add to favorite list button on details page")
    public void iClickOnTheAddToFavoriteListButtonOnDetails() throws InterruptedException {
        Thread.sleep(3000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement FavButton = driver.findElement(By.xpath("/html/body/div[4]/div/div/div/button"));
        js.executeScript("arguments[0].scrollIntoView(true);", FavButton);
        Thread.sleep(3000);
        FavButton.click();
    }

    @Then("the park should be added to my favorite list")
    public void theParkShouldBeAddedToMyFavoriteList() throws InterruptedException {
        Thread.sleep(3000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement parkName = driver.findElement(By.xpath("/html/body/div[4]/div/div/div/h2"));
        js.executeScript("arguments[0].scrollIntoView(true);", parkName);
        Thread.sleep(3000);
        assertTrue(driver.getPageSource().contains( " 🌟️ " ));
    }

    @Given("I have added {string} to my favorites list")
    public void iHaveAddedToMyFavoritesList(String arg0) throws InterruptedException {
        ChromeOptions options=new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);
        driver=new ChromeDriver(options);
        driver.get("https://localhost:8080");
        //sign up and then log in
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[1]/input")).sendKeys("username100");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[3]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username100");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        //input search
        Thread.sleep(3000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.sendKeys(arg0);
        //click search
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(3000);
        //hover over park card
        Thread.sleep(10000);
        List<WebElement> parks=driver.findElements(By.id("park-card"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", parks.get(0));
        Actions action = new Actions(driver);
        action.moveToElement(parks.get(0)).perform();
        Thread.sleep(3000);
        //add park
        WebElement Button = driver.findElement(By.id("favorite-id"));
        Button.click();
        Thread.sleep(3000);
        driver.get("https://localhost:8080/search");
    }

    @When("I search for {string} in the search box")
    public void iSearchForInTheSearchBox(String arg0) throws InterruptedException {
        //input search
        Thread.sleep(3000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.sendKeys(arg0);
        //click search
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(3000);
    }

    @Then("I should see the park is favorited")
    public void iShouldSeeTheParkIsFavorited() throws InterruptedException {
        Thread.sleep(3000);
        assertTrue(driver.getPageSource().contains( " 🌟️ " ));
    }

    @When("I click on the name of the park")
    public void iClickOnTheNameOfThePark() {
        WebElement parkName = driver.findElement(By.xpath("/html/body/div[4]/div/div/div/h2"));
        parkName.click();
    }

    @Then("the details should be minimized")
    public void theDetailsShouldBeMinimized() {

        assertFalse(driver.getPageSource().contains("Website️"));
    }

    @When("I click on an amenity")
    public void iClickOnAnAmenity() throws InterruptedException {
        //clicking the first amenity of the first park /html/body/div[4]/div/div/div/div[1]/span[1]
        Thread.sleep(3000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement amenity1 = driver.findElement(By.xpath("/html/body/div[4]/div/div/div/div[1]/span[1]"));
        js.executeScript("arguments[0].scrollIntoView(true);", amenity1);
        Thread.sleep(3000);
        amenity1.click();
    }

    @Then("a search should be triggered for that amenity")
    public void aSearchShouldBeTriggeredForThatAmenity() throws InterruptedException {
        Thread.sleep(3000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        String input=searchBox.getAttribute("value");
        Thread.sleep(3000);
        assertEquals(input, "Accessible Rooms", "search was not triggered properly");
        //check if the radio button is selected
        WebElement radioButton = driver.findElement(By.id("amenity" ));
        assertTrue(radioButton.isSelected());


    }

    @When("I click on the state")
    public void iClickOnTheState() throws InterruptedException {
        //clicking the address of the first park /html/body/div[4]/div/div/div/p[1]
        Thread.sleep(3000);
        WebElement address1 = driver.findElement(By.xpath("/html/body/div[4]/div/div/div/p[1]"));
        address1.click();

    }

    @Then("a search should be triggered for that state")
    public void aSearchShouldBeTriggeredForThatState() throws InterruptedException {
        Thread.sleep(3000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        String input=searchBox.getAttribute("value");
        Thread.sleep(3000);
        assertEquals(input, "KY", "search was not triggered properly");
        //check if the radio button is selected
        WebElement radioButton = driver.findElement(By.id("state" ));
        assertTrue(radioButton.isSelected());
    }

    @When("I click on an activity")
    public void iClickOnAnActivity() throws InterruptedException {
        //click on the first activity of the first park /html/body/div[4]/div/div/div/div[2]/span[1]
        Thread.sleep(3000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement activity1 = driver.findElement(By.xpath("/html/body/div[4]/div/div/div/div[2]/span[1]"));
        js.executeScript("arguments[0].scrollIntoView(true);", activity1);
        Thread.sleep(3000);
        activity1.click();

    }

    @Then("a search should be triggered for that activity")
    public void aSearchShouldBeTriggeredForThatActivity() throws InterruptedException { //Astronomy
        //check if search input is correct
        Thread.sleep(3000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        String input=searchBox.getAttribute("value");
        Thread.sleep(3000);
        assertEquals(input, "Astronomy", "search was not triggered properly");
        //check if the radio button is selected
        WebElement radioButton = driver.findElement(By.id("activity" ));
        assertTrue(radioButton.isSelected());
    }


    @Then("the plus button should be visible")
    public void thePlusButtonShouldBeVisible() throws InterruptedException {
        Thread.sleep(3000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        List<WebElement> FavButton = driver.findElements(By.xpath("/html/body/div[4]/div/div/div/button"));
        js.executeScript("arguments[0].scrollIntoView(true);", FavButton.get(0));
        Thread.sleep(3000);
        boolean isPresent = FavButton.size() > 0;
        assertTrue(isPresent, "Plus button is not present on the page.");
    }

    @Given("I log in and am on the park details page")
    public void iLogInAndAmOnTheParkDetailsPage() throws InterruptedException {
        ChromeOptions options=new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);
        driver=new ChromeDriver(options);
        driver.get("https://localhost:8080");
        //sign up and then log in
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[1]/input")).sendKeys("username90");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div/div[3]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[1]/input")).sendKeys("username90");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/div[2]/input")).sendKeys("123Ab");
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/div/form/button")).click();
        //input search
        Thread.sleep(3000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.sendKeys("park");
        //click search
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(3000);
        //clicking park name
        Thread.sleep(3000);
        WebElement parkname = driver.findElement(By.xpath("/html/body/div/div/div/div/div/div[1]/h3"));
        parkname.click();
        Thread.sleep(3000);
        //checking if park details show
        assertTrue(driver.getPageSource().contains("Website"));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

