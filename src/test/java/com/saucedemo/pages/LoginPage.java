package com.saucedemo.pages;

import com.saucedemo.utilities.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * LoginPage - Page Object Model (POM) class for the Login page
 *
 * This class encapsulates all UI elements and interactions specific to the login page.
 * All locators are defined as By objects to maintain separation of concerns.
 * No test logic should be present in this class - only page element interactions.
 *
 * Page URL: https://www.saucedemo.com
 */
public class LoginPage {

    // WebDriver instance
    private WebDriver driver;

    // WaitUtility for explicit waits
    private WaitUtility waitUtility;

    // ===== LOCATORS - All UI element locators for this page =====

    // Username input field locator
    private By usernameField = By.id("user-name");

    // Password input field locator
    private By passwordField = By.id("password");

    // Login button locator
    private By loginButton = By.id("login-button");

    // Error message container locator (appears when login fails)
    private By errorMessage = By.xpath("//h3[@data-test='error']");

    // Login page title/header locator (to verify page is loaded)
    private By loginPageHeader = By.xpath("//div[@class='login_logo']");

    /**
     * Constructor - Initialize LoginPage with WebDriver instance
     * @param driver - WebDriver instance passed from test class
     */
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtility = new WaitUtility(driver);
    }

    /**
     * Verify that login page is loaded by checking for login header
     * @return true if page is loaded, false otherwise
     */
    public boolean isLoginPageLoaded() {
        try {
            WebElement header = driver.findElement(loginPageHeader);
            waitUtility.waitForElementToBeVisible(header);
            return header.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Enter username in the username field
     * @param username - Username to enter
     */
    public void enterUsername(String username) {
        WebElement usernameInput = driver.findElement(usernameField);
        waitUtility.waitForElementToBeVisible(usernameInput);
        usernameInput.clear(); // Clear any existing text
        usernameInput.sendKeys(username);
    }

    /**
     * Enter password in the password field
     * @param password - Password to enter
     */
    public void enterPassword(String password) {
        WebElement passwordInput = driver.findElement(passwordField);
        waitUtility.waitForElementToBeVisible(passwordInput);
        passwordInput.clear(); // Clear any existing text
        passwordInput.sendKeys(password);
    }

    /**
     * Click the login button to submit credentials
     * Returns ProductListingPage as successful login navigates to product listing
     * @return ProductListingPage instance
     */
    public ProductListingPage clickLoginButton() {
        WebElement loginBtn = driver.findElement(loginButton);
        waitUtility.waitForElementToBeClickable(loginBtn);
        loginBtn.click();

        dismissPasswordPopup();
        return new ProductListingPage(driver);
    }

    /**
     * Dismiss Chrome password manager popup if it appears
     * Wait a bit for popup to appear, then dismiss any alerts
     */
    private void dismissPasswordPopup() {
        try {
            Thread.sleep(2000);
            driver.switchTo().alert().dismiss();
        } catch (Exception e) {
            // No alert present, which is expected
        }
    }

    /**
     * Perform complete login with username and password
     * This is a composite method combining multiple interactions
     * @param username - Username credential
     * @param password - Password credential
     * @return ProductListingPage instance after successful login
     */
    public ProductListingPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    /**
     * Get error message displayed after failed login attempt
     * @return Error message text
     */
    public String getErrorMessage() {
        try {
            WebElement errorElement = driver.findElement(errorMessage);
            waitUtility.waitForElementToBeVisible(errorElement);
            return errorElement.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if error message is displayed
     * Used to verify failed login scenarios
     * @return true if error message is visible, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        try {
            WebElement errorElement = driver.findElement(errorMessage);
            return errorElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get username field element (for validation testing - e.g., checking placeholder)
     * @return WebElement representing username field
     */
    public WebElement getUsernameFieldElement() {
        return driver.findElement(usernameField);
    }

    /**
     * Get password field element (for validation testing - e.g., checking placeholder)
     * @return WebElement representing password field
     */
    public WebElement getPasswordFieldElement() {
        return driver.findElement(passwordField);
    }
}
