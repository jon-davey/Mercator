package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class StepDefinitions {
    WebDriver driver;
    WebElement highestItemPriceWebElement;
    String highestItemPriceItemName;

    @Given("^The user has navigated to the following URL: (.*)$")
    public void theUserHasNavigatedToTheFollowingURL(String url) {
        System.out.println("\nInitialising Chrome Driver:");
        System.setProperty("webdriver.chrome.driver", "target\\classes\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("guest"); //This prevents a Change your password popup
        driver = new ChromeDriver(chromeOptions);

        System.out.println("\nNavigating to '" + url + "'");
        driver.get(url);
        driver.manage().window().maximize();
    }

    @Given("^The user has logged in using the following details: \\(username: (.*), password: (.*)\\)$")
    public void theUserHasLoggedInUsingTheFollowingDetails(String username, String password) {
        System.out.println("\nLogging in with '" + username + "'");
        driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(password);
        driver.findElement(By.xpath("//input[@value='Login']")).click();

        String actualSubHeader = driver.findElement(By.xpath("//div[@class='header_secondary_container']/span")).getText();
        AssertionFunctions.assertEquals("Page Sub Header", "Products", actualSubHeader);
    }

    @When("The user selects the highest price item")
    public void theUserSelectsTheHighestPriceItem() {
        System.out.println("\nIterating through inventory items");
        List<WebElement> inventoryItemsWebElements = driver.findElements(By.xpath("//div[@class='inventory_item']"));

        float highestItemPrice = 0;
        for (WebElement inventoryItemWebElement : inventoryItemsWebElements) {
            String itemName = inventoryItemWebElement.findElement(By.xpath(".//a/div")).getText();
            String itemPriceAsString = inventoryItemWebElement.findElement(By.xpath(".//div[@class='inventory_item_price']")).getText();
            float itemPrice = Float.parseFloat(itemPriceAsString.replace("$", ""));
            if (itemPrice > highestItemPrice) {
                highestItemPriceWebElement = inventoryItemWebElement;
                highestItemPriceItemName = itemName;
                highestItemPrice = itemPrice;
            }
        }
        System.out.println("Item '" + highestItemPriceItemName + "' has the highest price of '$" + highestItemPrice + "'");

        System.out.println("\nClicking highest price item");
        highestItemPriceWebElement.findElement(By.xpath(".//a")).click();

        String actualSubHeader = driver.findElement(By.xpath("//div[@class='header_secondary_container']")).getText();
        AssertionFunctions.assertEquals("Page Sub Header", "Back to products", actualSubHeader);
    }

    @Then("The highest price item is shown")
    public void theHighestPriceItemIsShown() {
        String actualItemName = driver.findElement(By.xpath("//div[@class='inventory_details_name large_size']")).getText();
        AssertionFunctions.assertEquals("Selected Item Name", highestItemPriceItemName, actualItemName);
    }

    @When("The user adds the selected highest price item to the cart")
    public void theUserAddsTheSelectedHighestPriceItemToTheCart() {
        System.out.println("\nClicking Add to cart");

        driver.findElement(By.xpath("//button[text()='Add to cart']")).click();
    }

    @Then("The highest price item appears in the cart")
    public void theHighestPriceItemAppearsInTheCart() {
        System.out.println("\nClicking Shopping cart");
        driver.findElement(By.xpath("//a[@class='shopping_cart_link']")).click();

        String actualSubHeader = driver.findElement(By.xpath("//div[@class='header_secondary_container']")).getText();
        AssertionFunctions.assertEquals("Page Sub Header", "Your Cart", actualSubHeader);

        String actualItemNameInCart = driver.findElement(By.xpath("//div[@class='inventory_item_name']")).getText();
        AssertionFunctions.assertEquals("Item Name In Cart", highestItemPriceItemName, actualItemNameInCart);
    }
}
