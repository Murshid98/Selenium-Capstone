package com.saucedemo.utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * WaitUtility - Utility class for explicit wait operations
 *
 * This class provides explicit wait methods using WebDriverWait instead of Thread.sleep().
 * Explicit waits are more efficient and reliable than implicit waits or hard sleeps.
 * Purpose: Ensure elements are ready before interaction and reduce flakiness in tests.
 */
public class WaitUtility {

    private WebDriver driver;
    private WebDriverWait wait;

    /**
     * Constructor - Initialize WaitUtility with WebDriver instance
     * @param driver - WebDriver instance
     */
    public WaitUtility(WebDriver driver) {
        this.driver = driver;
        // Create WebDriverWait with explicit wait time from config
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    /**
     * Wait for an element to be visible on the page
     * @param element - WebElement to wait for
     * @return The visible WebElement
     */
    public WebElement waitForElementToBeVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for an element located by a By locator to be visible (handles elements not yet in DOM)
     * @param locator - By locator
     * @return The visible WebElement
     */
    public WebElement waitForElementToBeVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for an element to be clickable (visible and enabled)
     * @param element - WebElement to wait for
     * @return The clickable WebElement
     */
    public WebElement waitForElementToBeClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait for an element located by a By locator to be clickable (handles elements not yet in DOM)
     * @param locator - By locator
     * @return The clickable WebElement
     */
    public WebElement waitForElementToBeClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for an element to be present in the DOM
     * @param element - WebElement to wait for
     * @return The present WebElement
     */
    public WebElement waitForElementToBePresent(WebElement element) {
        return wait.until(driver -> element.isDisplayed() || element != null ? element : null);
    }

    /**
     * Wait for an element to disappear from view
     * @param element - WebElement to wait for
     * @return True when element is invisible
     */
    public boolean waitForElementToBeInvisible(WebElement element) {
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Wait for element text to be present
     * @param element - WebElement containing the text
     * @param text - Expected text value
     * @return True when element contains the specified text
     */
    public boolean waitForElementToContainText(WebElement element, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    /**
     * Generic wait with custom timeout for specific conditions
     * @param driver - WebDriver instance
     * @param timeoutInSeconds - Custom timeout value
     * @return WebDriverWait instance with custom timeout
     */
    public static WebDriverWait getWebDriverWait(WebDriver driver, int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }
}
