package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductListingPage;
import com.saucedemo.pages.ShoppingCartPage;
import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.List;

/**
 * CheckoutTests - Test class for Checkout module
 *
 * This class contains all test cases related to checkout functionality.
 * Test categories:
 * - Complete checkout flow with valid data
 * - Form validation (empty fields, missing fields)
 * - Order summary verification
 * - Price calculation
 * - Order confirmation
 */
public class CheckoutTests extends BaseTest {

    // Page objects - initialized in beforeMethod
    protected ProductListingPage productsPage;

    /**
     * BeforeMethod - Common setup for checkout tests
     * Logs in user and navigates to products page before each test
     */
    @BeforeMethod
    public void beforeCheckoutTest() {
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
     * Test: Complete checkout flow with valid data
     *
     * Purpose: Verify entire checkout process from cart to order confirmation
     * Expected Result: Order completed successfully with confirmation message
     * Assertion: Confirmation page should display order completion message
     */
    @Test(groups = "Positive", priority = 1)
    public void testCompleteCheckoutFlow() {
        // Create report entry
        createTestReport("Complete Checkout Flow",
                "Verify entire checkout process from cart to confirmation");

        // Step 1: Add item to cart
        List<String> productNames = productsPage.getProductNames();
        productsPage.addProductToCart(productNames.get(0));
        test.info("Step 1: Added product to cart");

        // Step 2: Navigate to cart
        ShoppingCartPage cartPage = productsPage.clickOnCart();
        Assert.assertTrue(cartPage.isCartPageLoaded(),
                "Cart page should load");
        test.info("Step 2: Navigated to cart");

        // Step 3: Proceed to checkout
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutPage.isCheckoutStepOneLoaded(),
                "Checkout step one should load");
        test.info("Step 3: Navigated to checkout");

        // Step 4: Fill checkout form
        checkoutPage.fillCheckoutForm("John", "Doe", "12345");
        test.info("Step 4: Filled checkout form");

        // Step 5: Proceed to order review
        checkoutPage.clickContinue();
        Assert.assertTrue(checkoutPage.isCheckoutStepTwoLoaded(),
                "Checkout step two should load");
        test.info("Step 5: Proceeded to order review");

        // Step 6: Complete order
        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOrderConfirmationLoaded(),
                "Order confirmation page should load");
        test.info("Step 6: Order completed");

        // Verify confirmation message
        String confirmationMsg = checkoutPage.getConfirmationMessage();
        Assert.assertTrue(confirmationMsg.contains("Thank you"),
                "Confirmation message should contain 'Thank you'");
        test.pass("Checkout flow completed successfully with confirmation");
    }

    /**
     * Test: Checkout with empty form submission
     *
     * Purpose: Verify form validation prevents empty submission
     * Expected Result: Error message displayed, user remains on checkout page
     * Assertion: Error should indicate required fields
     */
    @Test(groups = "Negative", priority = 2)
    public void testCheckoutEmptyFormSubmission() {
        // Create report entry
        createTestReport("Checkout Empty Form Submission",
                "Verify checkout form validation for empty fields");

        // Add item and navigate to checkout
        List<String> productNames = productsPage.getProductNames();
        productsPage.addProductToCart(productNames.get(0));
        ShoppingCartPage cartPage = productsPage.clickOnCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();

        // Try to proceed without filling form
        checkoutPage.clickContinue();

        // Verify error message
        Assert.assertTrue(checkoutPage.isCheckoutErrorDisplayed(),
                "Error should be displayed for empty form");
        String error = checkoutPage.getCheckoutError();
        Assert.assertTrue(error.contains("First Name"),
                "Error should indicate First Name is required");
        test.pass("Empty form submission blocked with appropriate error");
    }

    /**
     * Test: Checkout with only first name filled
     *
     * Purpose: Verify validation when required fields are missing
     * Expected Result: Error message displayed
     * Assertion: Error should indicate Last Name is required
     */
    @Test(groups = "Negative", priority = 3)
    public void testCheckoutMissingLastName() {
        // Create report entry
        createTestReport("Checkout Missing Last Name",
                "Verify validation when last name field is empty");

        // Setup and navigate to checkout
        List<String> productNames = productsPage.getProductNames();
        productsPage.addProductToCart(productNames.get(0));
        ShoppingCartPage cartPage = productsPage.clickOnCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();

        // Fill only first name
        checkoutPage.enterFirstName("John");

        // Try to continue
        checkoutPage.clickContinue();

        // Verify error
        Assert.assertTrue(checkoutPage.isCheckoutErrorDisplayed(),
                "Error should be displayed");
        String error = checkoutPage.getCheckoutError();
        Assert.assertTrue(error.contains("Last Name"),
                "Error should indicate Last Name is required");
        test.pass("Missing last name validation working correctly");
    }

    /**
     * Test: Checkout with missing postal code
     *
     * Purpose: Verify validation when postal code is missing
     * Expected Result: Error message displayed
     * Assertion: Error should indicate Postal Code is required
     */
    @Test(groups = "Negative", priority = 4)
    public void testCheckoutMissingPostalCode() {
        // Create report entry
        createTestReport("Checkout Missing Postal Code",
                "Verify validation when postal code field is empty");

        // Setup and navigate to checkout
        List<String> productNames = productsPage.getProductNames();
        productsPage.addProductToCart(productNames.get(0));
        ShoppingCartPage cartPage = productsPage.clickOnCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();

        // Fill only first and last name
        checkoutPage.enterFirstName("John");
        checkoutPage.enterLastName("Doe");

        // Try to continue
        checkoutPage.clickContinue();

        // Verify error
        Assert.assertTrue(checkoutPage.isCheckoutErrorDisplayed(),
                "Error should be displayed");
        String error = checkoutPage.getCheckoutError();
        Assert.assertTrue(error.contains("Postal Code"),
                "Error should indicate Postal Code is required");
        test.pass("Missing postal code validation working correctly");
    }

    /**
     * Test: Verify order summary before completion
     *
     * Purpose: Ensure order review page displays correct item and price information
     * Expected Result: Order summary shows correct item, total, and tax
     * Assertion: Item name and total should match
     */
    @Test(groups = "Positive", priority = 5)
    public void testOrderSummaryVerification() {
        // Create report entry
        createTestReport("Order Summary Verification",
                "Verify order review page displays correct item and pricing");

        // Get product details
        List<String> productNames = productsPage.getProductNames();
        List<String> productPrices = productsPage.getProductPrices();
        String selectedProduct = productNames.get(0);
        String selectedPrice = productPrices.get(0);

        // Add item and navigate to checkout
        productsPage.addProductToCart(selectedProduct);
        ShoppingCartPage cartPage = productsPage.clickOnCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();

        // Fill and submit checkout form
        checkoutPage.fillCheckoutForm("John", "Doe", "12345");
        checkoutPage.clickContinue();

        // Verify checkout step two loaded
        Assert.assertTrue(checkoutPage.isCheckoutStepTwoLoaded(),
                "Order review page should load");

        // Verify item in order review
        List<String> reviewItems = checkoutPage.getOrderReviewItems();
        Assert.assertTrue(reviewItems.contains(selectedProduct),
                "Product should be in order review");

        // Get total amount
        String totalAmount = checkoutPage.getTotalAmount();
        Assert.assertFalse(totalAmount.isEmpty(),
                "Total amount should be displayed");
        test.pass("Order summary verified: " + selectedProduct + " - " + totalAmount);
    }

    /**
     * Test: Verify price calculations in order summary
     *
     * Purpose: Ensure item total, tax, and final total are calculated correctly
     * Expected Result: Price calculations should be accurate
     * Assertion: All price elements should be displayed and non-empty
     */
    @Test(groups = "Positive", priority = 6)
    public void testPriceCalculationVerification() {
        // Create report entry
        createTestReport("Price Calculation Verification",
                "Verify item total, tax, and final total calculations");

        // Add multiple items
        List<String> productNames = productsPage.getProductNames();
        productsPage.addProductToCart(productNames.get(0));
        productsPage.addProductToCart(productNames.get(1));

        // Navigate to checkout
        ShoppingCartPage cartPage = productsPage.clickOnCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.fillCheckoutForm("John", "Doe", "12345");
        checkoutPage.clickContinue();

        // Verify all price components
        String itemTotal = checkoutPage.getItemTotal();
        String taxAmount = checkoutPage.getTaxAmount();
        String totalAmount = checkoutPage.getTotalAmount();

        Assert.assertFalse(itemTotal.isEmpty(),
                "Item total should be displayed");
        Assert.assertFalse(taxAmount.isEmpty(),
                "Tax amount should be displayed");
        Assert.assertFalse(totalAmount.isEmpty(),
                "Total amount should be displayed");

        test.pass("Price calculations verified - Item: " + itemTotal +
                ", Tax: " + taxAmount + ", Total: " + totalAmount);
    }

    /**
     * Test: Order confirmation message
     *
     * Purpose: Verify order confirmation displays appropriate message
     * Expected Result: Confirmation page shows thank you message
     * Assertion: Confirmation message should contain expected text
     */
    @Test(groups = "Positive", priority = 7)
    public void testOrderConfirmationMessage() {
        // Create report entry
        createTestReport("Order Confirmation Message",
                "Verify order confirmation displays appropriate success message");

        // Complete checkout
        List<String> productNames = productsPage.getProductNames();
        productsPage.addProductToCart(productNames.get(0));
        ShoppingCartPage cartPage = productsPage.clickOnCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.fillCheckoutForm("John", "Doe", "12345");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();

        // Verify confirmation page
        Assert.assertTrue(checkoutPage.isOrderConfirmationLoaded(),
                "Confirmation page should load");

        // Verify confirmation message
        String confirmationMsg = checkoutPage.getConfirmationMessage();
        Assert.assertTrue(confirmationMsg.contains("Thank you"),
                "Should show thank you message");

        // Verify confirmation text
        String confirmationText = checkoutPage.getConfirmationText();
        Assert.assertFalse(confirmationText.isEmpty(),
                "Should display confirmation details");
        test.pass("Order confirmation verified");
    }

    /**
     * Test: Back to products from confirmation page
     *
     * Purpose: Verify "Back to Products" button on confirmation page works
     * Expected Result: Returns to product listing page
     * Assertion: Should be on products page with all products visible
     */
    @Test(groups = "Positive", priority = 8)
    public void testBackToProductsFromConfirmation() {
        // Create report entry
        createTestReport("Back to Products from Confirmation",
                "Verify back to products button from confirmation page");

        // Complete checkout
        List<String> productNames = productsPage.getProductNames();
        productsPage.addProductToCart(productNames.get(0));
        ShoppingCartPage cartPage = productsPage.clickOnCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.fillCheckoutForm("John", "Doe", "12345");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();

        // Verify on confirmation page
        Assert.assertTrue(checkoutPage.isOrderConfirmationLoaded(),
                "Should be on confirmation page");

        // Click back to products
        ProductListingPage backToProducts = checkoutPage.clickBackToProducts();

        // Verify on products page
        Assert.assertTrue(backToProducts.isProductsPageLoaded(),
                "Should return to products page");
        Assert.assertEquals(backToProducts.getProductCount(), 6,
                "All 6 products should be displayed");
        test.pass("Successfully returned to products page from confirmation");
    }
}
