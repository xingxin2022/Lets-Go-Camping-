

package edu.usc.csci310.project;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.skyscreamer.jsonassert.JSONAssert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateStepdefs {
    private WebDriver driver = new ChromeDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

    @Given("I have a favorite park list")
    public void iHaveAFavoriteParkList() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);
        driver = new ChromeDriver(options);
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
        //go to favlist page
        Thread.sleep(3000);
        WebElement favButton = driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[3]"));
        favButton.click();
        Thread.sleep(3000);
        //delete all parks if exists
        List<WebElement> deleteAll = driver.findElements(By.xpath("/html/body/div/div/div/div/button"));
        if (deleteAll.size() != 0) {
            deleteAll.get(0).click();
            Thread.sleep(3000);
            driver.findElement(By.id("confirm")).click();
            Thread.sleep(3000);
        }

        //go to search page
        WebElement search = driver.findElement(By.cssSelector("[data-testid='toSearch']"));
        search.click();
        //input search
        Thread.sleep(3000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.sendKeys("park");
        //click search
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(3000);
        //hover over park card
        Thread.sleep(20000);
        List<WebElement> parks = driver.findElements(By.id("park-card"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", parks.get(0));
        Actions action = new Actions(driver);
        action.moveToElement(parks.get(0)).perform();
        Thread.sleep(3000);
        //add park
        WebElement Button = driver.findElement(By.id("favorite-id"));
        Button.click();
        Thread.sleep(8000);
        //hover over 2nd park card
        List<WebElement> parks2 = driver.findElements(By.id("park-card"));
        JavascriptExecutor js2 = (JavascriptExecutor) driver;
        js2.executeScript("arguments[0].scrollIntoView(true);", parks2.get(1));
        Actions action2 = new Actions(driver);
        action2.moveToElement(parks2.get(1)).perform();
        Thread.sleep(3000);
        //add 2nd park
        WebElement Button2 = driver.findElement(By.id("favorite-id"));
        Button2.click();
        Thread.sleep(8000);

    }

    @When("I visit the favorite park list page")
    public void iVisitTheFavoriteParkListPage() throws InterruptedException {
        Thread.sleep(3000);
        WebElement favButton = driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[3]"));
        favButton.click();
        Thread.sleep(3000);
    }

    @Then("I should see the list of my favorite parks")
    public void iShouldSeeTheListOfMyFavoriteParks() throws InterruptedException {
        Thread.sleep(10000);
        List<WebElement> parks = driver.findElements(By.id("park-box"));
        new WebDriverWait(driver, Duration.ofSeconds(100));
        //assertEquals(count,parks.size());
        boolean size = parks.size() > 1;
        assertTrue(size, "Element  is not present on the page.");

    }

    @And("the order of the list should be by park name")
    public void theOrderOfTheListShouldBeByParkName() {
        List<WebElement> elements = driver.findElements(By.tagName("h3"));

        // Extract the text from each element and collect into a list
        List<String> names = elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        // Create a copy of the list, then sort it alphabetically
        List<String> sortedNames = new ArrayList<>(names);
        Collections.sort(sortedNames);

        // Compare the original list with the sorted list
        JSONAssert Assert;
        assertTrue(sortedNames.equals(names), "The parks list is not in alphabetical order");

    }

    @Given("I am on the favorite park list page")
    public void iAmOnTheFavoriteParkListPage() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);
        driver = new ChromeDriver(options);
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
        //go to favlist page
        Thread.sleep(3000);
        WebElement favButton = driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[3]"));
        favButton.click();
        Thread.sleep(3000);
        //delete all parks if exists
        List<WebElement> deleteAll = driver.findElements(By.xpath("/html/body/div/div/div/div/button"));
        if (deleteAll.size() != 0) {
            deleteAll.get(0).click();
            Thread.sleep(3000);
            driver.findElement(By.id("confirm")).click();
            Thread.sleep(3000);
        }

        //go to search page
        WebElement search = driver.findElement(By.cssSelector("[data-testid='toSearch']"));
        search.click();
        Thread.sleep(3000);
        //input search
        Thread.sleep(3000);
        WebElement searchBox = driver.findElement(By.xpath("/html/body/div/div/div/div[1]/input"));
        searchBox.sendKeys("park");
        //click search
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/button"));
        searchButton.click();
        Thread.sleep(3000);
        //hover over park card
        Thread.sleep(20000);
        List<WebElement> parks = driver.findElements(By.id("park-card"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", parks.get(0));
        Actions action = new Actions(driver);
        action.moveToElement(parks.get(0)).perform();
        Thread.sleep(3000);
        //add park
        WebElement Button = driver.findElement(By.id("favorite-id"));
        Button.click();
        Thread.sleep(8000);
        //hover over 2nd park card
        List<WebElement> parks2 = driver.findElements(By.id("park-card"));
        JavascriptExecutor js2 = (JavascriptExecutor) driver;
        js2.executeScript("arguments[0].scrollIntoView(true);", parks2.get(1));
        Actions action2 = new Actions(driver);
        action2.moveToElement(parks2.get(1)).perform();
        Thread.sleep(3000);
        //add 2nd park
        WebElement Button2 = driver.findElement(By.id("favorite-id"));
        Button2.click();
        Thread.sleep(8000);
        //go to favlist page
        Thread.sleep(3000);
        WebElement fav = driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[3]"));
        fav.click();
        Thread.sleep(3000);


    }

    @And("I click the move up button for a park")
    public void iClickTheMoveUpButtonForAPark() {
        WebElement Button = driver.findElement(By.id("moveup"));
        Button.click();
        driver.findElement(By.id("confirm")).click();
    }

    @Then("the park should move up in the list")
    public void theParkShouldMoveUpInTheList() throws InterruptedException {
        Thread.sleep(3000);
        List<WebElement> elements = driver.findElements(By.tagName("h3"));

        // Extract the text from each element and collect into a list
        List<String> names = elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        // Create a copy of the list, then sort it alphabetically
        List<String> sortedNames = new ArrayList<>(names);
        Collections.sort(sortedNames);
        //move the second name to the first, as how move up button should work
        // Swap the first two elements in the sorted list if there are at least two elements
        if (sortedNames.size() > 1) {
            // Temporary store the first element
            String temp = sortedNames.get(0);
            // Swap the first element with the second
            sortedNames.set(0, sortedNames.get(1));
            sortedNames.set(1, temp);
        }

        // Compare the original list with the sorted list
        //JSONAssert Assert;
        assertTrue(sortedNames.equals(names), "The parks list is not in alphabetical order");

    }


    @And("I click the move down button for a park")
    public void iClickTheMoveDownButtonForAPark() {
        WebElement Button = driver.findElement(By.id("movedown"));
        Button.click();
        driver.findElement(By.id("confirm")).click();
    }

    @Then("the park should move down in the list")
    public void theParkShouldMoveDownInTheList() throws InterruptedException {
        Thread.sleep(3000);
        List<WebElement> elements = driver.findElements(By.tagName("h3"));

        // Extract the text from each element and collect into a list
        List<String> names = elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        // Create a copy of the list, then sort it alphabetically
        List<String> sortedNames = new ArrayList<>(names);
        Collections.sort(sortedNames);
        //move the second name to the first, as how move down button should work
        // Swap the first two elements in the sorted list if there are at least two elements
        if (sortedNames.size() > 1) {
            // Temporary store the first element
            String temp = sortedNames.get(0);
            // Swap the first element with the second
            sortedNames.set(0, sortedNames.get(1));
            sortedNames.set(1, temp);
        }

        // Compare the original list with the sorted list
        JSONAssert Assert;
        assertTrue(sortedNames.equals(names), "The parks list is not in alphabetical order");

    }

    @When("I click on a park in the list")
    public void iClickOnAParkInTheList() throws InterruptedException {
        Thread.sleep(3000);
        List<WebElement> elements = driver.findElements(By.tagName("h3"));
        elements.get(0).click();

    }

    @Then("I should be taken to the park details page")
    public void iShouldBeTakenToTheParkDetailsPage() throws InterruptedException {
        Thread.sleep(3000);
        assertTrue(driver.getPageSource().contains("Website"));

    }

    @Given("I change the order of the favorite park list")
    public void iChangeTheOrderOfTheFavoriteParkList() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);
        driver = new ChromeDriver(options);
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
        Thread.sleep(20000);
        List<WebElement> parks = driver.findElements(By.id("park-card"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", parks.get(0));
        Actions action = new Actions(driver);
        action.moveToElement(parks.get(0)).perform();
        Thread.sleep(3000);
        //add park
        WebElement Button = driver.findElement(By.id("favorite-id"));
        Button.click();
        Thread.sleep(8000);
        //hover over 2nd park card
        List<WebElement> parks2 = driver.findElements(By.id("park-card"));
        JavascriptExecutor js2 = (JavascriptExecutor) driver;
        js2.executeScript("arguments[0].scrollIntoView(true);", parks2.get(1));
        Actions action2 = new Actions(driver);
        action2.moveToElement(parks2.get(1)).perform();
        Thread.sleep(3000);
        //add 2nd park
        WebElement Button2 = driver.findElement(By.id("favorite-id"));
        Button2.click();
        Thread.sleep(8000);
        //go to favlist page
        Thread.sleep(3000);
        WebElement favButton = driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[3]"));
        favButton.click();
        Thread.sleep(3000);
        //hover second park
        Thread.sleep(3000);
        List<WebElement> parks3 = driver.findElements(By.id("park-box"));
        JavascriptExecutor js3 = (JavascriptExecutor) driver;
        js3.executeScript("arguments[0].scrollIntoView(true);", parks3.get(1));
        Actions action3 = new Actions(driver);
        action3.moveToElement(parks3.get(1)).perform();
        Thread.sleep(3000);
        //click move up button
        WebElement moveup = driver.findElement(By.id("moveup"));
        moveup.click();
        driver.findElement(By.id("confirm")).click();
    }

    @When("I visit the search page")
    public void iVisitTheSearchPage() throws InterruptedException {
        WebElement searchButton = driver.findElement(By.cssSelector("[data-testid='toSearch']"));
        searchButton.click();
        Thread.sleep(3000);
    }

    @And("I return to the favorite park list page")
    public void iReturnToTheFavoriteParkListPage() throws InterruptedException {
        Thread.sleep(3000);
        WebElement favButton = driver.findElement(By.xpath("/html/body/div/div/div/header[1]/nav/ul/li[3]"));
        favButton.click();
        Thread.sleep(3000);
    }

    @Then("I should see the list with the updated order")
    public void iShouldSeeTheListWithTheUpdatedOrder() {
        List<WebElement> elements = driver.findElements(By.tagName("h3"));

        // Extract the text from each element and collect into a list
        List<String> names = elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        // Create a copy of the list, then sort it alphabetically
        List<String> sortedNames = new ArrayList<>(names);
        Collections.sort(sortedNames);
        //move the second name to the first, as how move up button should work
        // Swap the first two elements in the sorted list if there are at least two elements
        if (sortedNames.size() > 1) {
            // Temporary store the first element
            String temp = sortedNames.get(0);
            // Swap the first element with the second
            sortedNames.set(0, sortedNames.get(1));
            sortedNames.set(1, temp);
        }

        // Compare the original list with the sorted list
        JSONAssert Assert;
        assertTrue(sortedNames.equals(names), "The parks list is not in updated order");

    }

    @When("I click the delete button for a park")
    public void iClickTheDeleteButtonForAPark() {
        WebElement Button = driver.findElement(By.id("remove"));
        Button.click();
    }

    @When("I hover over the second park")
    public void iHoverOverTheSecondPark() throws InterruptedException {
        Thread.sleep(3000);
        List<WebElement> parks = driver.findElements(By.id("park-box"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", parks.get(1));
        Actions action = new Actions(driver);
        action.moveToElement(parks.get(1)).perform();
        Thread.sleep(3000);
    }

    @When("I hover over the first park")
    public void iHoverOverTheFirstPark() throws InterruptedException {
        Thread.sleep(3000);
        List<WebElement> parks = driver.findElements(By.id("park-box"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", parks.get(0));
        Actions action = new Actions(driver);
        action.moveToElement(parks.get(0)).perform();
        Thread.sleep(3000);
    }

    @And("the park should be removed from the list")
    public void theParkShouldBeRemovedFromTheList() throws InterruptedException {
        Thread.sleep(3000);
        List<WebElement> parks = driver.findElements(By.id("park-box"));
        new WebDriverWait(driver, Duration.ofSeconds(100));
        boolean size = parks.size() == 1;
        assertTrue(size, "Not removed");
    }

    @Then("I click confirm on the pop-up to confirm deletion")
    public void iClickConfirmOnThePopUpToConfirmDeletion() {
        driver.findElement(By.id("confirm")).click();
    }

    @Then("I click cancel on the pop-up to cancel deletion")
    public void iClickCancelOnThePopUpToCancelDeletion() {
        driver.findElement(By.id("cancel")).click();

    }

    @And("the park should remain in the list")
    public void theParkShouldRemainInTheList() throws InterruptedException {
        Thread.sleep(3000);
        List<WebElement> parks = driver.findElements(By.id("park-box"));
        new WebDriverWait(driver, Duration.ofSeconds(100));
        boolean size = parks.size() == 2;
        assertTrue(size, "removed");
    }

    @Then("the list should be private by default")
    public void theListShouldBePrivateByDefault() {
        assertTrue(driver.getPageSource().contains("Public"));
    }

    @When("I click the make public button")
    public void iClickTheMakePublicButton() {///html/body/div/div[1]/div/div/div[1]/button
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div/div[1]/button")).click();
    }

    @Then("the list should be public")
    public void theListShouldBePublic() {///html/body/div/div/div/div/div[1]/button
        List<WebElement> elements = driver.findElements(By.xpath("/html/body/div/div/div/div/div[1]/button"));
        boolean isPresent = elements.size() > 0;
        assertTrue(isPresent, "Element  is not present on the page.");
    }

    @And("the list can be used to compare with others")
    public void theListCanBeUsedToCompareWithOthers() {
    }

    @When("I click the make private button")
    public void iClickTheMakePrivateButton() {
        driver.findElement(By.xpath("/html/body/div/div[1]/div/div/div[1]/button")).click();

    }

    @Then("the list should be private")
    public void theListShouldBePrivate() {
        List<WebElement> elements = driver.findElements(By.xpath("/html/body/div/div/div/div/div[1]/button"));
        boolean isPresent = elements.size() > 0;
        assertTrue(isPresent, "Element  is not present on the page.");
    }

    @When("I click the delete all button")
    public void iClickTheDeleteAllButton() throws InterruptedException {
        //delete all parks if exists
        List<WebElement> deleteAll = driver.findElements(By.xpath("/html/body/div/div/div/div/button"));
        if (deleteAll.size() != 0) {
            deleteAll.get(0).click();
        }
        Thread.sleep(3000);


    }

    @Then("I should see a pop-up to confirm deletion")
    public void iShouldSeeAPopUpToConfirmDeletion() {
        assertTrue(driver.getPageSource().contains("Confirm"));

    }

    @And("the list should be empty")
    public void theListShouldBeEmpty() throws InterruptedException {
        Thread.sleep(10000);
        List<WebElement> parks = driver.findElements(By.id("park-box"));
        new WebDriverWait(driver, Duration.ofSeconds(100));
        //assertEquals(count,parks.size());
        boolean size = parks.size() == 0;
        assertTrue(size, "Element  is not present on the page.");
    }

    @And("the list should remain the same")
    public void theListShouldRemainTheSame() throws InterruptedException {
        Thread.sleep(10000);
        List<WebElement> parks = driver.findElements(By.id("park-box"));
        new WebDriverWait(driver, Duration.ofSeconds(100));
        //assertEquals(count,parks.size());
        boolean size = parks.size() == 2;
        assertTrue(size, "Element  is not present on the page.");
    }

    @Then("move-up move-down remove buttons are visible")
    public void moveUpMoveDownRemoveButtonsAreVisible() {//driver.findElement(By.id("moveup"));
        List<WebElement> moveup = driver.findElements(By.id("moveup"));
        new WebDriverWait(driver, Duration.ofSeconds(100));
        assertTrue(moveup.size() == 1, "Moveup is not present on the page.");
        List<WebElement> movedown = driver.findElements(By.id("movedown"));
        new WebDriverWait(driver, Duration.ofSeconds(100));
        assertTrue(movedown.size() == 1, "Movedown is not present on the page.");
        List<WebElement> remove = driver.findElements(By.id("remove"));
        new WebDriverWait(driver, Duration.ofSeconds(100));
        assertTrue(remove.size() == 1, "Movedown is not present on the page.");
    }

    @When("I click the home button")
    public void iClickHomeButton() throws InterruptedException {
        Thread.sleep(3000);
        WebElement home = driver.findElement(By.cssSelector("[data-testid='toHome']"));
        home.click();
        
    }
    @When("I click search button on update page")
    public void iClickSearchButton() throws InterruptedException {
        Thread.sleep(3000);
        WebElement search = driver.findElement(By.cssSelector("[data-testid='toSearch']"));
        search.click();

    }

    @When("I click compareList button on update page")
    public void iClickCompareListButton() throws InterruptedException {//
        Thread.sleep(3000);
        WebElement compareList = driver.findElement(By.xpath("/html/body/div/div[1]/div/header[1]/nav/ul/li[4]"));
        compareList.click();
        
    }

    @When("I click Logout button on update page")
    public void iClickLogoutButton() throws InterruptedException {
        Thread.sleep(3000);
        WebElement logoutButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/header[1]/nav/ul/li[5]"));
        logoutButton.click();
        
    }

    @Then("I should be directed to compare page")
    public void iShouldBeDirectedToComparePage() {
        List<WebElement> elements = driver.findElements(By.xpath("/html/body/div/div/div/div/div[2]"));
        boolean isPresent = elements.size() > 0;
        assertTrue(isPresent, "Element  is not present on the page.");
    }


    @Then("I should be directed to search page")
    public void iShouldBeDirectedToSearchPage() {
        List<WebElement> elements = driver.findElements(By.xpath("/html/body/div/div/div/div[1]/button"));
        boolean isPresent = elements.size() > 0;
        assertTrue(isPresent, "Element  is not present on the page.");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}