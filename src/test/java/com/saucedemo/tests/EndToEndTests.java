package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductListingPage;
import com.saucedemo.pages.ShoppingCartPage;
import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

/**
 * EndToEndTests - End-to-End test scenarios
 *
 * This class contains comprehensive end-to-end test scenarios that verify
 * complete user workflows across multiple pages and features.
 *
 * The happy path scenario tests the main user journey:
 * Login → Browse Products → Add Items → View Cart → Checkout → Order Confirmation
 */
public class EndToEndTests extends BaseTest {

    /**
     * Test: Complete Happy Path Scenario
     *
     * Purpose: Verify the complete user journey from login to order confirmation
     * This is the primary happy path scenario covering the main user workflow.
     *
     * Flow:
     * 1. Login with valid credentials
     * 2. Browse products on listing page
     * 3. Add multiple items to cart
     * 4. Navigate to shopping cart
     * 5. Proceed to checkout
     * 6. Fill in shipping information
     * 7. Review order summary
     * 8. Complete the order
     * 9. Verify order confirmation
     *
     * Expected Result: Order completed successfully with confirmation message
     * Assertion: User should see order confirmation page
     */
    @Test(groups = "Positive", priority = 1)
    public void testCompleteHappyPathScenario() {
        // Create detailed report entry for this comprehensive scenario
        createTestReport("Complete Happy Path Scenario",
                "End-to-End: Login → Browse → Add Items → Checkout → Confirmation");

        // ===== STEP 1: LOGIN =====
        System.out.println("\n========== STEP 1: LOGIN ==========");
        LoginPage loginPage = new LoginPage(driver);

        // Verify login page is displayed
        Assert.assertTrue(loginPage.isLoginPageLoaded(),
                "Login page should be loaded");
        test.info("Step 1a: Login page loaded");

        // Login with valid credentials
        ProductListingPage productsPage = loginPage.login(
                ConfigReader.getValidUsername(),
                ConfigReader.getValidPassword()
        );

        // Verify successful login
        Assert.assertTrue(productsPage.isProductsPageLoaded(),
                "Products page should load after login");
        test.pass("Step 1: Login successful");

        // ===== STEP 2: BROWSE PRODUCTS =====
        System.out.println("\n========== STEP 2: BROWSE PRODUCTS ==========");

        // Verify product listing page content
        int productCount = productsPage.getProductCount();
        Assert.assertEquals(productCount, 6,
                "Should display 6 products");
        test.info("Step 2a: " + productCount + " products displayed");

        // Get product names and prices
        List<String> productNames = productsPage.getProductNames();
        List<String> productPrices = productsPage.getProductPrices();
        Assert.assertFalse(productNames.isEmpty(),
                "Products list should not be empty");
        test.info("Step 2b: Product names retrieved");

        // Test sorting functionality
        productsPage.sortProducts("Price (low to high)");
        test.info("Step 2c: Products sorted by price (low to high)");

        test.pass("Step 2: Product browsing and sorting verified");

        // ===== STEP 3: ADD ITEMS TO CART =====
        System.out.println("\n========== STEP 3: ADD ITEMS TO CART ==========");

        // Add first product to cart
        String firstProduct = productNames.get(0);
        productsPage.addProductToCart(firstProduct);
        test.info("Step 3a: Added '" + firstProduct + "' to cart");

        // Add second product to cart
        String secondProduct = productNames.get(1);
        productsPage.addProductToCart(secondProduct);
        test.info("Step 3b: Added '" + secondProduct + "' to cart");

        // Verify cart count updated
        int cartCount = productsPage.getCartItemCount();
        Assert.assertEquals(cartCount, 2,
                "Cart should contain 2 items");
        test.pass("Step 3: Successfully added 2 items to cart (Count: " + cartCount + ")");

        // ===== STEP 4: VIEW SHOPPING CART =====
        System.out.println("\n========== STEP 4: VIEW SHOPPING CART ==========");

        // Navigate to cart
        ShoppingCartPage cartPage = productsPage.clickOnCart();
        Assert.assertTrue(cartPage.isCartPageLoaded(),
                "Cart page should load");
        test.info("Step 4a: Navigated to shopping cart");

        // Verify cart contents
        int cartItemCount = cartPage.getCartItemCount();
        Assert.assertEquals(cartItemCount, 2,
                "Cart should have 2 items");
        test.info("Step 4b: Cart contains " + cartItemCount + " items");

        // Verify item details
        Assert.assertTrue(cartPage.isItemInCart(firstProduct),
                "First product should be in cart");
        Assert.assertTrue(cartPage.isItemInCart(secondProduct),
                "Second product should be in cart");
        test.info("Step 4c: All items verified in cart");

        // Get cart item details for verification
        String[] firstItemDetails = cartPage.getItemDetails(firstProduct);
        String[] secondItemDetails = cartPage.getItemDetails(secondProduct);
        test.info("Step 4d: Item 1 - " + firstItemDetails[0] + " (Qty: " + firstItemDetails[1] + ", Price: " + firstItemDetails[2] + ")");
        test.info("Step 4e: Item 2 - " + secondItemDetails[0] + " (Qty: " + secondItemDetails[1] + ", Price: " + secondItemDetails[2] + ")");

        test.pass("Step 4: Cart verified with all items and details");

        // ===== STEP 5: PROCEED TO CHECKOUT =====
        System.out.println("\n========== STEP 5: PROCEED TO CHECKOUT ==========");

        // Click checkout button
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutPage.isCheckoutStepOneLoaded(),
                "Checkout step one should load");
        test.info("Step 5a: Navigated to checkout page (Step 1)");

        test.pass("Step 5: Checkout process initiated");

        // ===== STEP 6: FILL SHIPPING INFORMATION =====
        System.out.println("\n========== STEP 6: FILL SHIPPING INFORMATION ==========");

        // Fill checkout form with test data
        String firstName = "John";
        String lastName = "Doe";
        String postalCode = "12345";

        checkoutPage.fillCheckoutForm(firstName, lastName, postalCode);
        test.info("Step 6a: Filled shipping form - Name: " + firstName + " " + lastName + ", Postal Code: " + postalCode);

        // Proceed to step two
        checkoutPage.clickContinue();
        Assert.assertTrue(checkoutPage.isCheckoutStepTwoLoaded(),
                "Checkout step two should load");
        test.info("Step 6b: Proceeded to order review (Step 2)");

        test.pass("Step 6: Shipping information filled and validated");

        // ===== STEP 7: REVIEW ORDER SUMMARY =====
        System.out.println("\n========== STEP 7: REVIEW ORDER SUMMARY ==========");

        // Verify order review page
        List<String> reviewItems = checkoutPage.getOrderReviewItems();
        Assert.assertEquals(reviewItems.size(), 2,
                "Order review should show 2 items");
        test.info("Step 7a: Order review shows " + reviewItems.size() + " items");

        // Get price information
        String itemTotal = checkoutPage.getItemTotal();
        String taxAmount = checkoutPage.getTaxAmount();
        String totalAmount = checkoutPage.getTotalAmount();

        Assert.assertFalse(itemTotal.isEmpty(),
                "Item total should be displayed");
        Assert.assertFalse(taxAmount.isEmpty(),
                "Tax amount should be displayed");
        Assert.assertFalse(totalAmount.isEmpty(),
                "Total amount should be displayed");

        test.info("Step 7b: Order Summary - Item Total: " + itemTotal);
        test.info("Step 7c: Order Summary - Tax: " + taxAmount);
        test.info("Step 7d: Order Summary - Final Total: " + totalAmount);

        // Verify items in review match added items
        Assert.assertTrue(reviewItems.contains(firstProduct),
                "First product should be in order review");
        Assert.assertTrue(reviewItems.contains(secondProduct),
                "Second product should be in order review");
        test.info("Step 7e: All added items verified in order review");

        test.pass("Step 7: Order summary reviewed and verified");

        // ===== STEP 8: COMPLETE ORDER =====
        System.out.println("\n========== STEP 8: COMPLETE ORDER ==========");

        // Click finish to complete order
        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOrderConfirmationLoaded(),
                "Order confirmation page should load");
        test.info("Step 8a: Order completed successfully");

        test.pass("Step 8: Order finalized");

        // ===== STEP 9: VERIFY ORDER CONFIRMATION =====
        System.out.println("\n========== STEP 9: VERIFY ORDER CONFIRMATION ==========");

        // Get confirmation message
        String confirmationMsg = checkoutPage.getConfirmationMessage();
        Assert.assertTrue(confirmationMsg.contains("Thank you"),
                "Confirmation should contain thank you message");
        test.info("Step 9a: Confirmation message: " + confirmationMsg);

        // Get confirmation details
        String confirmationText = checkoutPage.getConfirmationText();
        Assert.assertFalse(confirmationText.isEmpty(),
                "Confirmation should contain order details");
        test.info("Step 9b: Confirmation details displayed");

        // Verify order confirmation page has back button
        ProductListingPage finalProductsPage = checkoutPage.clickBackToProducts();
        Assert.assertTrue(finalProductsPage.isProductsPageLoaded(),
                "Should return to products page");
        test.info("Step 9c: Returned to products page from confirmation");

        test.pass("Step 9: Order confirmation verified");

        // ===== COMPLETE HAPPY PATH =====
        System.out.println("\n========== HAPPY PATH COMPLETE ==========");
        test.pass("🎉 END-TO-END HAPPY PATH SCENARIO COMPLETED SUCCESSFULLY 🎉");
    }

    /**
     * Test: Happy Path with Single Item
     *
     * Purpose: Verify complete checkout flow with single product
     * (Simplified version of complete happy path)
     *
     * Flow: Login → Add 1 Item → Checkout → Confirmation
     */
    @Test(groups = "Positive", priority = 2)
    public void testHappyPathSingleItem() {
        // Create report entry
        createTestReport("Happy Path - Single Item",
                "Complete checkout flow with single product");

        // Login
        LoginPage loginPage = new LoginPage(driver);
        ProductListingPage productsPage = loginPage.login(
                ConfigReader.getValidUsername(),
                ConfigReader.getValidPassword()
        );
        test.info("Step 1: Logged in successfully");

        // Add one item
        List<String> productNames = productsPage.getProductNames();
        String selectedProduct = productNames.get(0);
        productsPage.addProductToCart(selectedProduct);
        test.info("Step 2: Added " + selectedProduct + " to cart");

        // Go to cart and checkout
        ShoppingCartPage cartPage = productsPage.clickOnCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        test.info("Step 3: Navigated to checkout");

        // Fill form and complete
        checkoutPage.fillCheckoutForm("Jane", "Smith", "54321");
        checkoutPage.clickContinue();
        test.info("Step 4: Filled checkout form");

        checkoutPage.clickFinish();
        test.info("Step 5: Completed order");

        // Verify confirmation
        Assert.assertTrue(checkoutPage.isOrderConfirmationLoaded(),
                "Should show confirmation page");
        String confirmationMsg = checkoutPage.getConfirmationMessage();
        Assert.assertTrue(confirmationMsg.contains("Thank you"),
                "Should show thank you message");

        test.pass("Single item checkout completed successfully");
    }

    /**
     * Test: Happy Path with Product Detail Navigation
     *
     * Purpose: Verify happy path including product detail page navigation
     *
     * Flow: Login → View Product Details → Add from Detail → Checkout → Confirmation
     */
    @Test(groups = "Positive", priority = 3)
    public void testHappyPathWithProductDetails() {
        // Create report entry
        createTestReport("Happy Path - via Product Details",
                "Complete checkout flow navigating through product detail page");

        // Login
        LoginPage loginPage = new LoginPage(driver);
        ProductListingPage productsPage = loginPage.login(
                ConfigReader.getValidUsername(),
                ConfigReader.getValidPassword()
        );
        test.info("Step 1: Logged in successfully");

        // Navigate to product detail
        List<String> productNames = productsPage.getProductNames();
        String selectedProduct = productNames.get(0);

        com.saucedemo.pages.ProductDetailPage detailPage = productsPage.clickOnProduct(selectedProduct);
        Assert.assertTrue(detailPage.isProductDetailPageLoaded(),
                "Product detail page should load");
        test.info("Step 2: Navigated to product detail: " + selectedProduct);

        // Get product info from detail page
        String detailProductName = detailPage.getProductName();
        String detailProductPrice = detailPage.getProductPrice();
        test.info("Step 3: Product info - " + detailProductName + " : " + detailProductPrice);

        // Add to cart from detail page
        detailPage.addToCart();
        test.info("Step 4: Added product to cart from detail page");

        // Return to products and go to cart
        productsPage = detailPage.clickBackButton();
        ShoppingCartPage cartPage = productsPage.clickOnCart();
        test.info("Step 5: Navigated to shopping cart");

        // Verify item in cart
        Assert.assertTrue(cartPage.isItemInCart(selectedProduct),
                "Product should be in cart");
        test.info("Step 6: Verified product in cart");

        // Complete checkout
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.fillCheckoutForm("Bob", "Wilson", "99999");
        checkoutPage.clickContinue();
        test.info("Step 7: Filled checkout form");

        checkoutPage.clickFinish();
        test.info("Step 8: Completed order");

        // Verify confirmation
        Assert.assertTrue(checkoutPage.isOrderConfirmationLoaded(),
                "Should show confirmation page");
        test.pass("Happy path with product detail navigation completed successfully");
    }
}
