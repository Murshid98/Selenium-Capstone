package com.saucedemo.pages;

import com.saucedemo.utilities.WaitUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * LoginPage - Page Factory model for the Login page
 *
 * Page URL: https://www.saucedemo.com
 */
public class LoginPage {

    private WebDriver driver;
    private WaitUtility waitUtility;

    // ===== PAGE FACTORY ELEMENTS =====

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(xpath = "//h3[@data-test='error']")
    private WebElement errorMessage;

    @FindBy(xpath = "//div[@class='login_logo']")
    private WebElement loginPageHeader;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtility = new WaitUtility(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean isLoginPageLoaded() {
        try {
            waitUtility.waitForElementToBeVisible(loginPageHeader);
            return loginPageHeader.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterUsername(String username) {
        waitUtility.waitForElementToBeVisible(usernameField);
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        waitUtility.waitForElementToBeVisible(passwordField);
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public ProductListingPage clickLoginButton() {
        waitUtility.waitForElementToBeClickable(loginButton);
        loginButton.click();
        dismissPasswordPopup();
        return new ProductListingPage(driver);
    }

    private void dismissPasswordPopup() {
        try {
            Thread.sleep(2000);
            driver.switchTo().alert().dismiss();
        } catch (Exception e) {
            // No alert present, which is expected
        }
    }

    public ProductListingPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    public String getErrorMessage() {
        try {
            waitUtility.waitForElementToBeVisible(errorMessage);
            return errorMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement getUsernameFieldElement() {
        return usernameField;
    }

    public WebElement getPasswordFieldElement() {
        return passwordField;
    }
}
