package org.example.util;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SeleniumHelper {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public SeleniumHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Click an element
    public void click(WebElement element, int timeout) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            throw new WebDriverException("Failed to click element: " + e.getMessage());
        }
    }

    // Send keys to an element
    public void sendKeys(WebElement element, String text, int timeout) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            throw new WebDriverException("Failed to send keys to element: " + e.getMessage());
        }
    }

    // Get text from an element
    public String getText(WebElement element, int timeout) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.getText();
        } catch (Exception e) {
            throw new WebDriverException("Failed to get text from element: " + e.getMessage());
        }
    }

    // Check if element is displayed
    public boolean isDisplayed(WebElement element, int timeout) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Select dropdown by visible text
    public void selectByVisibleText(WebElement element, String text) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            Select select = new Select(element);
            select.selectByVisibleText(text);
        } catch (Exception e) {
            throw new WebDriverException("Failed to select by visible text: " + e.getMessage());
        }
    }

    // Get all elements matching a locator
    public List<WebElement> findElements(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
            return driver.findElements(locator);
        } catch (Exception e) {
            throw new WebDriverException("Failed to find elements: " + e.getMessage());
        }
    }

    // Wait for element to be visible
    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // Wait for element to be clickable
    public WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // Scroll to element
    public void scrollToElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            throw new WebDriverException("Failed to scroll to element: " + e.getMessage());
        }
    }
}