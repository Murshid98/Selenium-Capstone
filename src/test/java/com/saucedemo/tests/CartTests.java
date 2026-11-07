package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductListingPage;
import com.saucedemo.pages.ShoppingCartPage;
import com.saucedemo.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.List;

/**
 * CartTests - Test class for Shopping Cart module
 *
 * This class contains all test cases related to shopping cart functionality.
 * Test categories:
 * - Add single item to cart
 * - Add multiple items to cart
 * - Remove items from cart
 * - Verify cart item details
 * - Continue shopping button
 */
public class CartTests extends BaseTest {

    // ProductListingPage instance - initialized in beforeMethod
    protected ProductListingPage productsPage;

    /**
     * BeforeMethod - Common setup for cart tests
     * Logs in user and navigates to products page before each test
     */
    @BeforeMethod
    public void beforeCartTest() {
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
     * Test: Add single item to cart
     *
     * Purpose: Verify adding one product to cart works correctly
     * Expected Result: Item appears in cart with correct details
     * Assertion: Cart should contain the added item
     */
    @Test(groups = "Positive", priority = 1)
    public void testAddSingleItemToCart() {
        // Create report entry
        createTestReport("Add Single Item to Cart",
                "Verify adding one product to cart works correctly");

        // Get first product name and price
        List<String> productNames = productsPage.getProductNames();
        List<String> productPrices = productsPage.getProductPrices();
        String selectedProduct = productNames.get(0);
        String selectedPrice = productPrices.get(0);

        // Add product to cart
        productsPage.addProductToCart(selectedProduct);
        test.info("Added product to cart: " + selectedProduct);

        // Verify cart badge shows 1 item
        int cartCount = productsPage.getCartItemCount();
        Assert.assertEquals(cartCount, 1, "Cart should contain 1 item");

        // Navigate to cart
        ShoppingCartPage cartPage = productsPage.clickOnCart();

        // Verify item is in cart
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page should load");
        Assert.assertTrue(cartPage.isItemInCart(selectedProduct),
                "Added product should be in cart");

        // Verify item details
        String[] itemDetails = cartPage.getItemDetails(selectedProduct);
        Assert.assertEquals(itemDetails[0], selectedProduct,
                "Item name should match");
        Assert.assertEquals(itemDetails[2], selectedPrice,
                "Item price should match");
        test.pass("Single item successfully added to cart with correct details");
    }

    /**
     * Test: Add multiple items to cart
     *
     * Purpose: Verify adding multiple products to cart works correctly
     * Expected Result: All items appear in cart
     * Assertion: Cart count should match number of items added
     */
    @Test(groups = "Positive", priority = 2)
    public void testAddMultipleItemsToCart() {
        // Create report entry
        createTestReport("Add Multiple Items to Cart",
                "Verify adding multiple products to cart works");

        // Add first 3 products to cart
        List<String> productNames = productsPage.getProductNames();
        for (int i = 0; i < 3; i++) {
            productsPage.addProductToCart(productNames.get(i));
        }
        test.info("Added 3 products to cart");

        // Verify cart badge shows 3 items
        int cartCount = productsPage.getCartItemCount();
        Assert.assertEquals(cartCount, 3, "Cart should contain 3 items");

        // Navigate to cart
        ShoppingCartPage cartPage = productsPage.clickOnCart();

        // Verify all items are in cart
        int cartItemCount = cartPage.getCartItemCount();
        Assert.assertEquals(cartItemCount, 3,
                "All 3 items should be in cart");

        // Verify each item exists
        for (int i = 0; i < 3; i++) {
            Assert.assertTrue(cartPage.isItemInCart(productNames.get(i)),
                    "Item " + (i + 1) + " should be in cart");
        }
        test.pass("Successfully added 3 items to cart");
    }

    /**
     * Test: Remove single item from cart
     *
     * Purpose: Verify removing an item from cart works correctly
     * Expected Result: Item is removed, cart count decreases
     * Assertion: Item should no longer be in cart
     */
    @Test(groups = "Positive", priority = 3)
    public void testRemoveItemFromCart() {
        // Create report entry
        createTestReport("Remove Item from Cart",
                "Verify removing product from cart works correctly");

        // Add 2 products to cart
        List<String> productNames = productsPage.getProductNames();
        String firstProduct = productNames.get(0);
        String secondProduct = productNames.get(1);

        productsPage.addProductToCart(firstProduct);
        productsPage.addProductToCart(secondProduct);
        test.info("Added 2 products to cart");

        // Navigate to cart
        ShoppingCartPage cartPage = productsPage.clickOnCart();

        // Verify both items in cart
        Assert.assertEquals(cartPage.getCartItemCount(), 2,
                "Cart should have 2 items");

        // Remove first item
        cartPage.removeItemFromCart(firstProduct);
        test.info("Removed " + firstProduct + " from cart");

        // Verify item removed
        Assert.assertEquals(cartPage.getCartItemCount(), 1,
                "Cart should have 1 item after removal");
        Assert.assertFalse(cartPage.isItemInCart(firstProduct),
                "Removed item should not be in cart");
        Assert.assertTrue(cartPage.isItemInCart(secondProduct),
                "Other item should still be in cart");
        test.pass("Successfully removed item from cart");
    }

    /**
     * Test: Verify item details in cart
     *
     * Purpose: Ensure item details (name, price, quantity) display correctly in cart
     * Expected Result: All item details should match what was added
     * Assertion: Cart item details should be accurate
     */
    @Test(groups = "Positive", priority = 4)
    public void testVerifyCartItemDetails() {
        // Create report entry
        createTestReport("Verify Cart Item Details",
                "Verify product details display correctly in cart");

        // Get product details
        List<String> productNames = productsPage.getProductNames();
        List<String> productPrices = productsPage.getProductPrices();
        String selectedProduct = productNames.get(0);
        String selectedPrice = productPrices.get(0);

        // Add product to cart
        productsPage.addProductToCart(selectedProduct);

        // Navigate to cart
        ShoppingCartPage cartPage = productsPage.clickOnCart();

        // Get item details from cart
        List<String> cartItemNames = cartPage.getCartItemNames();
        List<String> cartItemPrices = cartPage.getCartItemPrices();
        List<String> cartItemQuantities = cartPage.getCartItemQuantities();

        // Verify details
        Assert.assertEquals(cartItemNames.get(0), selectedProduct,
                "Item name should match");
        Assert.assertEquals(cartItemPrices.get(0), selectedPrice,
                "Item price should match");
        Assert.assertEquals(cartItemQuantities.get(0), "1",
                "Item quantity should be 1");
        test.pass("Cart item details verified successfully");
    }

    /**
     * Test: Continue Shopping button functionality
     *
     * Purpose: Verify "Continue Shopping" button returns to products page
     * Expected Result: User navigated back to product listing
     * Assertion: Should be on products page with products visible
     */
    @Test(groups = "Positive", priority = 5)
    public void testContinueShoppingButton() {
        // Create report entry
        createTestReport("Continue Shopping Button",
                "Verify continue shopping button returns to products page");

        // Add item to cart
        List<String> productNames = productsPage.getProductNames();
        productsPage.addProductToCart(productNames.get(0));

        // Navigate to cart
        ShoppingCartPage cartPage = productsPage.clickOnCart();
        Assert.assertTrue(cartPage.isCartPageLoaded(),
                "Should be on cart page");

        // Click continue shopping
        ProductListingPage backToProducts = cartPage.clickContinueShopping();

        // Verify back on products page
        Assert.assertTrue(backToProducts.isProductsPageLoaded(),
                "Should return to products page");
        Assert.assertEquals(backToProducts.getProductCount(), 6,
                "All products should be visible");
        test.pass("Continue shopping button works correctly");
    }

    /**
     * Test: Empty cart state
     *
     * Purpose: Verify empty cart displays correctly
     * Expected Result: Cart shows 0 items
     * Assertion: Cart should be empty
     */
    @Test(groups = "Positive", priority = 6)
    public void testEmptyCart() {
        // Create report entry
        createTestReport("Empty Cart",
                "Verify empty cart state displays correctly");

        // Navigate to empty cart without adding items
        ShoppingCartPage cartPage = productsPage.clickOnCart();

        // Verify cart is empty
        Assert.assertTrue(cartPage.isCartPageLoaded(),
                "Cart page should load");
        Assert.assertTrue(cartPage.isCartEmpty(),
                "Cart should be empty");
        Assert.assertEquals(cartPage.getCartItemCount(), 0,
                "Cart should contain 0 items");
        test.pass("Empty cart verified");
    }

    /**
     * Test: Remove all items from cart
     *
     * Purpose: Verify cart becomes empty after removing all items
     * Expected Result: Cart shows 0 items
     * Assertion: Cart should be empty
     */
    @Test(groups = "Positive", priority = 7)
    public void testRemoveAllItemsFromCart() {
        // Create report entry
        createTestReport("Remove All Items from Cart",
                "Verify cart becomes empty after removing all items");

        // Add 2 products
        List<String> productNames = productsPage.getProductNames();
        productsPage.addProductToCart(productNames.get(0));
        productsPage.addProductToCart(productNames.get(1));

        // Navigate to cart
        ShoppingCartPage cartPage = productsPage.clickOnCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2,
                "Should have 2 items");

        // Remove both items
        List<String> itemsInCart = cartPage.getCartItemNames();
        for (String item : itemsInCart) {
            cartPage.removeItemFromCart(item);
        }

        // Verify cart is empty
        Assert.assertTrue(cartPage.isCartEmpty(),
                "Cart should be empty");
        test.pass("All items removed successfully, cart is empty");
    }
}
