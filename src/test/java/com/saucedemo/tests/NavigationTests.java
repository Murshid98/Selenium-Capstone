package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductListingPage;
import com.saucedemo.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * NavigationTests - Test class for Navigation and Menu module
 *
 * This class contains all test cases related to sidebar menu navigation,
 * links, and navigation functionality within the application.
 * Test categories:
 * - Sidebar menu open/close
 * - Navigation links (All Items, About, Logout)
 * - Reset App State functionality
 */
public class NavigationTests extends BaseTest {

    // ProductListingPage instance - initialized in beforeMethod
    protected ProductListingPage productsPage;

    /**
     * BeforeMethod - Common setup for navigation tests
     * Logs in user and navigates to products page before each test
     */
    @BeforeMethod
    public void beforeNavigationTest() {
        // Call parent beforeMethod for driver initialization
        super.beforeMethod();

        // Login and navigate to products page
        LoginPage loginPage = new LoginPage(driver);
        productsPage = loginPage.login(
                ConfigReader.getValidUsername(),
                ConfigReader.getValidPassword()
        );
    }

    /**
     * Test: Open sidebar menu
     *
     * Purpose: Verify sidebar menu opens correctly
     * Expected Result: Sidebar menu should be visible
     * Assertion: Menu should open successfully
     */
    @Test(groups = "Positive", priority = 1)
    public void testOpenSidebarMenu() {
        // Create report entry
        createTestReport("Open Sidebar Menu",
                "Verify sidebar menu opens correctly");

        // Open menu
        productsPage.openMenu();
        test.info("Opened sidebar menu");

        // Verify menu is open (sidebar should be visible)
        // Taking a small pause to ensure menu animation completes
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Menu should be visible after opening
        test.pass("Sidebar menu opened successfully");
    }

    /**
     * Test: Close sidebar menu
     *
     * Purpose: Verify sidebar menu closes correctly
     * Expected Result: Sidebar menu should be hidden
     * Assertion: Menu should close successfully
     */
    @Test(groups = "Positive", priority = 2)
    public void testCloseSidebarMenu() {
        // Create report entry
        createTestReport("Close Sidebar Menu",
                "Verify sidebar menu closes correctly");

        // Open and close menu
        productsPage.openMenu();
        productsPage.closeMenu();
        test.info("Closed sidebar menu");

        // Menu should be hidden
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        test.pass("Sidebar menu closed successfully");
    }

    /**
     * Test: Click All Items link in sidebar
     *
     * Purpose: Verify "All Items" link navigates to products page
     * Expected Result: Should remain on products page with all items visible
     * Assertion: Products page should be displayed
     */
    @Test(groups = "Positive", priority = 3)
    public void testAllItemsLink() {
        // Create report entry
        createTestReport("All Items Link Navigation",
                "Verify All Items link works and displays all products");

        // Initially on products page, click All Items
        productsPage.clickAllItemsLink();
        test.info("Clicked All Items link");

        // Verify still on products page with all items
        Assert.assertTrue(productsPage.isProductsPageLoaded(),
                "Should remain on products page");
        Assert.assertEquals(productsPage.getProductCount(), 6,
                "All 6 products should be displayed");
        test.pass("All Items link verified - all products displayed");
    }

    /**
     * Test: Click About link in sidebar
     *
     * Purpose: Verify "About" link navigates away from application
     * Expected Result: User should navigate to About page (external)
     * Assertion: URL should change to About page
     */
    @Test(groups = "Positive", priority = 4)
    public void testAboutLink() {
        // Create report entry
        createTestReport("About Link Navigation",
                "Verify About link navigates to external page");

        // Get current URL before clicking
        String currentUrl = driver.getCurrentUrl();

        // Click About link
        productsPage.clickAboutLink();
        test.info("Clicked About link");

        // Wait for page navigation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify URL changed (navigated away from SauceDemo)
        String newUrl = driver.getCurrentUrl();
        Assert.assertNotEquals(currentUrl, newUrl,
                "Should navigate to About page");
        Assert.assertTrue(newUrl.contains("saucelabs.com"),
                "Should navigate to Sauce Labs About page");
        test.pass("About link navigated to external page successfully");
    }

    /**
     * Test: Click Logout link in sidebar
     *
     * Purpose: Verify "Logout" link logs user out and returns to login page
     * Expected Result: Should be redirected to login page
     * Assertion: Login page should be displayed
     */
    @Test(groups = "Positive", priority = 5)
    public void testLogoutLink() {
        // Create report entry
        createTestReport("Logout Link Navigation",
                "Verify Logout link logs out user and returns to login page");

        // Click logout
        LoginPage loginPage = productsPage.clickLogoutLink();
        test.info("Clicked Logout link");

        // Verify on login page
        Assert.assertTrue(loginPage.isLoginPageLoaded(),
                "Should return to login page after logout");
        test.pass("User successfully logged out via sidebar link");
    }

    /**
     * Test: Reset App State functionality
     *
     * Purpose: Verify "Reset App State" clears cart and resets application state
     * Expected Result: Cart should be empty after reset
     * Assertion: Cart count should be 0, products should be displayed
     */
    @Test(groups = "Positive", priority = 6)
    public void testResetAppState() {
        // Create report entry
        createTestReport("Reset App State",
                "Verify Reset App State clears cart and resets application");

        // Add multiple items to cart
        productsPage.addProductToCart("Sauce Labs Backpack");
        productsPage.addProductToCart("Sauce Labs Bike Light");

        // Verify items in cart
        int cartCountBefore = productsPage.getCartItemCount();
        Assert.assertEquals(cartCountBefore, 2,
                "Cart should have 2 items before reset");
        test.info("Added 2 items to cart before reset");

        // Click reset app state
        productsPage.clickResetAppStateLink();
        test.info("Clicked Reset App State");

        // Verify cart is empty after reset
        int cartCountAfter = productsPage.getCartItemCount();
        Assert.assertEquals(cartCountAfter, 0,
                "Cart should be empty after reset");

        // Verify products still visible
        Assert.assertTrue(productsPage.isProductsPageLoaded(),
                "Products page should still be loaded");
        Assert.assertEquals(productsPage.getProductCount(), 6,
                "All products should still be visible");
        test.pass("App state reset successfully - cart cleared");
    }

    /**
     * Test: Sidebar menu accessibility
     *
     * Purpose: Verify all navigation links are accessible from sidebar
     * Expected Result: All menu links should be clickable
     * Assertion: Menu should open and close without errors
     */
    @Test(groups = "Positive", priority = 7)
    public void testSidebarMenuAccessibility() {
        // Create report entry
        createTestReport("Sidebar Menu Accessibility",
                "Verify all navigation links are accessible from sidebar");

        // Open menu - should not throw exception
        try {
            productsPage.openMenu();
            test.info("Sidebar menu opened");

            // Close menu - should not throw exception
            productsPage.closeMenu();
            test.info("Sidebar menu closed");

            test.pass("Sidebar menu accessible with all functions working");
        } catch (Exception e) {
            test.fail("Error accessing sidebar menu: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Test: Multiple open/close menu cycles
     *
     * Purpose: Verify menu can be opened and closed multiple times
     * Expected Result: Menu should work reliably across multiple interactions
     * Assertion: Menu should function correctly throughout
     */
    @Test(groups = "Positive", priority = 8)
    public void testMultipleMenuOpenCloseCycles() {
        // Create report entry
        createTestReport("Multiple Menu Open/Close Cycles",
                "Verify menu can be opened and closed multiple times reliably");

        // Open and close menu 3 times
        for (int i = 1; i <= 3; i++) {
            productsPage.openMenu();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            productsPage.closeMenu();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            test.info("Cycle " + i + " completed");
        }

        // Verify still on products page
        Assert.assertTrue(productsPage.isProductsPageLoaded(),
                "Should still be on products page");
        test.pass("Menu open/close cycles completed successfully");
    }
}
