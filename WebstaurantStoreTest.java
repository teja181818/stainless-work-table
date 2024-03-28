import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

public class WebstaurantStoreTest {
    public static void main(String[] args) {
        // Setting up Chrome WebDriver
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // Maximize the browser window
        WebDriver driver = new ChromeDriver(options);

        // Navigate to the Webstaurant Store website
        driver.get("https://www.webstaurantstore.com/");

        // Search for 'stainless work table'
        WebElement searchBox = driver.findElement(By.id("searchval"));
        searchBox.sendKeys("stainless work table");
        searchBox.submit();

        // Wait until search results are loaded
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("details")));

        // Check search results ensuring every product has the word 'Table' in its title
        boolean allProductsContainTable = true;
        for (WebElement product : driver.findElements(By.className("details"))) {
            String title = product.findElement(By.className("description")).getText();
            if (!title.toLowerCase().contains("table")) {
                allProductsContainTable = false;
                break;
            }
        }

        // If all products contain 'Table' in their title, proceed to add the last found item to the cart
        if (allProductsContainTable) {
            WebElement lastProduct = driver.findElements(By.className("details")).get(driver.findElements(By.className("details")).size() - 1);
            WebElement addToCartButton = lastProduct.findElement(By.xpath(".//input[@value='Add to Cart']"));
            addToCartButton.click();

            // Wait for the cart popup to appear and then close it
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cartDropdown")));
            driver.findElement(By.className("close-modal")).click();

            // Empty Cart
            driver.findElement(By.id("cartItemCount")).click();
            driver.findElement(By.id("cartItemCountEmpty")).click();
        } else {
            System.out.println("Not all products contain 'Table' in their title.");
        }

        // Close the browser
        driver.quit();
    }
}
