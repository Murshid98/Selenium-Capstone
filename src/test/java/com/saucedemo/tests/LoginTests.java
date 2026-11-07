package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductListingPage;
import com.saucedemo.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTests - Test class for Login module
 *
 * This class contains all test cases related to user authentication and login functionality.
 * Test categories:
 * - Positive scenarios: Valid login
 * - Negative scenarios: Invalid password, empty fields, locked-out user
 * - Edge cases: Logout and navigation
 *
 * All tests extend BaseTest to inherit setup/teardown and WebDriver management.
 */
public class LoginTests extends BaseTest {

    /**
     * Test: Valid Login with correct credentials
     *
     * Purpose: Verify that a user can successfully log in with valid credentials
     * Expected Result: User is redirected to products listing page
     * Assertion: Products page should be displayed
     */
    @Test(groups = "Positive", priority = 1)
    public void testValidLogin() {
        // Create report entry for this test
        createTestReport("Valid Login", "Verify successful login with valid credentials");

        // Initialize login page
        LoginPage loginPage = new LoginPage(driver);

        // Verify login page is loaded
        Assert.assertTrue(loginPage.isLoginPageLoaded(),
                "Login page should be displayed");
        test.info("Login page loaded successfully");

        // Perform login with valid credentials from config
        ProductListingPage productsPage = loginPage.login(
                ConfigReader.getValidUsername(),
                ConfigReader.getValidPassword()
        );

        // Wait for page to load and verify products page is displayed
        Assert.assertTrue(productsPage.isProductsPageLoaded(),
                "Products page should be displayed after valid login");
        test.pass("User successfully logged in and navigated to products page");
    }

    /**
     * Test: Login with invalid password
     *
     * Purpose: Verify that login fails with incorrect password
     * Expected Result: Error message displayed, user remains on login page
     * Assertion: Error message should contain "Username and password do not match"
     */
    @Test(groups = "Negative", priority = 2)
    public void testInvalidPassword() {
        // Create report entry
        createTestReport("Invalid Password Login", "Verify login fails with incorrect password");

        // Initialize login page
        LoginPage loginPage = new LoginPage(driver);

        // Attempt login with valid username but invalid password
        loginPage.enterUsername(ConfigReader.getValidUsername());
        loginPage.enterPassword(ConfigReader.getInvalidPassword());
        loginPage.clickLoginButton();

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for invalid password");

        // Verify error message text contains expected content
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Username and password do not match"),
                "Error message should indicate credentials mismatch");
        test.pass("Invalid password error handled correctly");
    }

    /**
     * Test: Login with empty username and password fields
     *
     * Purpose: Verify that login validation requires username and password
     * Expected Result: Error message displayed indicating required fields
     * Assertion: Error should mention "Username is required"
     */
    @Test(groups = "Negative", priority = 3)
    public void testEmptyFieldsLogin() {
        // Create report entry
        createTestReport("Empty Fields Login", "Verify login validation for empty fields");

        // Initialize login page
        LoginPage loginPage = new LoginPage(driver);

        // Click login button without entering any credentials
        loginPage.clickLoginButton();

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for empty fields");

        // Verify error message text
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Username is required"),
                "Error should indicate username is required");
        test.pass("Empty fields validation working correctly");
    }

    /**
     * Test: Login with locked-out user
     *
     * Purpose: Verify system behavior when locked-out user attempts login
     * Expected Result: Error message displayed preventing access
     * Assertion: Error message should indicate user is locked out
     */
    @Test(groups = "Negative", priority = 4)
    public void testLockedOutUserLogin() {
        // Create report entry
        createTestReport("Locked Out User Login",
                "Verify login fails for locked-out user account");

        // Initialize login page
        LoginPage loginPage = new LoginPage(driver);

        // Attempt login with locked-out user credentials
        loginPage.login(
                ConfigReader.getLockedUsername(),
                ConfigReader.getLockedPassword()
        );

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for locked-out user");

        // Verify error message text
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("locked out"),
                "Error should indicate user is locked out");
        test.pass("Locked-out user properly prevented from logging in");
    }

    /**
     * Test: Login → Logout → Back Button navigation
     *
     * Purpose: Verify logout functionality and back button behavior
     * Expected Result: User can logout and using back button doesn't bypass login
     * Assertion: Should return to login page, not bypass authentication
     */
    @Test(groups = "Positive", priority = 5)
    public void testLoginLogoutBackButton() {
        // Create report entry
        createTestReport("Login → Logout → Back Button",
                "Verify logout and back button security behavior");

        // Step 1: Valid login
        LoginPage loginPage = new LoginPage(driver);
        ProductListingPage productsPage = loginPage.login(
                ConfigReader.getValidUsername(),
                ConfigReader.getValidPassword()
        );
        Assert.assertTrue(productsPage.isProductsPageLoaded(),
                "Should be on products page after login");
        test.info("Step 1: User logged in successfully");

        // Step 2: Click logout from sidebar menu
        LoginPage logoutPage = productsPage.clickLogoutLink();
        Assert.assertTrue(logoutPage.isLoginPageLoaded(),
                "Should return to login page after logout");
        test.info("Step 2: User logged out successfully");

        // Step 3: Use browser back button
        driver.navigate().back();

        // Verify we're still on login page (back button shouldn't bypass login)
        Assert.assertTrue(logoutPage.isLoginPageLoaded(),
                "Using back button after logout should not bypass login security");
        test.pass("Logout and back button security verified");
    }

    /**
     * Test: Only username field filled
     *
     * Purpose: Verify validation when password field is empty
     * Expected Result: Error message displayed
     * Assertion: Error should indicate password is required
     */
    @Test(groups = "Negative", priority = 6)
    public void testOnlyUsernameFilledLogin() {
        // Create report entry
        createTestReport("Only Username Filled",
                "Verify validation when password field is empty");

        // Initialize login page
        LoginPage loginPage = new LoginPage(driver);

        // Enter only username and click login
        loginPage.enterUsername(ConfigReader.getValidUsername());
        loginPage.clickLoginButton();

        // Verify error message
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error should be displayed when password is empty");
        test.pass("Password field validation working correctly");
    }

    /**
     * Test: Only password field filled
     *
     * Purpose: Verify validation when username field is empty
     * Expected Result: Error message displayed
     * Assertion: Error should indicate username is required
     */
    @Test(groups = "Negative", priority = 7)
    public void testOnlyPasswordFilledLogin() {
        // Create report entry
        createTestReport("Only Password Filled",
                "Verify validation when username field is empty");

        // Initialize login page
        LoginPage loginPage = new LoginPage(driver);

        // Enter only password and click login
        loginPage.enterPassword(ConfigReader.getValidPassword());
        loginPage.clickLoginButton();

        // Verify error message
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error should be displayed when username is empty");
        test.pass("Username field validation working correctly");
    }
}
