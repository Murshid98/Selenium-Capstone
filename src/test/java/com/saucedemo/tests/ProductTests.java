package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductListingPage;
import com.saucedemo.pages.ProductDetailPage;
import com.saucedemo.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.List;

/**
 * ProductTests - Test class for Product Listing and Product Detail modules
 *
 * This class contains all test cases related to product browsing, sorting, filtering,
 * and viewing product details.
 * Test categories:
 * - Product count verification
 * - Sorting functionality (by name, by price)
 * - Product detail page verification
 * - Add to cart from detail page
 */
public class ProductTests extends BaseTest {

    // ProductListingPage instance - initialized in beforeMethod
    protected ProductListingPage productsPage;

    /**
     * BeforeMethod - Common setup for product tests
     * Logs in user before each product test
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeProductTest() {
        // Login before product tests
        LoginPage loginPage = new LoginPage(driver);
        productsPage = loginPage.login(
                ConfigReader.getValidUsername(),
                ConfigReader.getValidPassword()
        );
    }

    /**
     * Test: Verify product count on listing page
     *
     * Purpose: Ensure correct number of products are displayed
     * Expected Result: 6 products should be displayed on the page
     * Assertion: Product count should equal 6
     */
    @Test(groups = "Positive", priority = 1)
    public void testProductCount() {
        // Create report entry
        createTestReport("Product Count Verification",
                "Verify correct number of products displayed on listing page");

        // Assert products page is loaded
        Assert.assertTrue(productsPage.isProductsPageLoaded(),
                "Products page should be loaded");

        // Get actual product count
        int productCount = productsPage.getProductCount();

        // Verify product count matches expected value
        Assert.assertEquals(productCount, 6,
                "There should be exactly 6 products displayed");
        test.pass("Product count verified: " + productCount + " products found");
    }

    /**
     * Test: Sort products by name A→Z (alphabetical ascending)
     *
     * Purpose: Verify sorting functionality works correctly for ascending names
     * Expected Result: Products sorted alphabetically from A to Z
     * Assertion: First product starts with letter closer to A than last product
     */
    @Test(groups = "Positive", priority = 2)
    public void testSortByNameAtoZ() {
        // Create report entry
        createTestReport("Sort by Name (A-Z)",
                "Verify products are sorted alphabetically from A to Z");

        // Sort products by name A-Z
        productsPage.sortProducts("Name (A to Z)");

        // Get sorted product names
        List<String> productNames = productsPage.getProductNames();

        // Verify list is not empty
        Assert.assertFalse(productNames.isEmpty(),
                "Product list should not be empty");

        // Verify sorting by checking first and last products
        // First product should come before last alphabetically
        String firstProduct = productNames.get(0);
        String lastProduct = productNames.get(productNames.size() - 1);

        Assert.assertTrue(firstProduct.compareTo(lastProduct) <= 0,
                "Products should be sorted A-Z: " + firstProduct + " to " + lastProduct);
        test.pass("Products correctly sorted A-Z: " + firstProduct + " ... " + lastProduct);
    }

    /**
     * Test: Sort products by name Z→A (alphabetical descending)
     *
     * Purpose: Verify reverse alphabetical sorting
     * Expected Result: Products sorted alphabetically from Z to A
     * Assertion: First product comes after last product alphabetically
     */
    @Test(groups = "Positive", priority = 3)
    public void testSortByNameZtoA() {
        // Create report entry
        createTestReport("Sort by Name (Z-A)",
                "Verify products are sorted alphabetically from Z to A");

        // Sort products by name Z-A
        productsPage.sortProducts("Name (Z to A)");

        // Get sorted product names
        List<String> productNames = productsPage.getProductNames();

        // Verify sorting
        String firstProduct = productNames.get(0);
        String lastProduct = productNames.get(productNames.size() - 1);

        Assert.assertTrue(firstProduct.compareTo(lastProduct) >= 0,
                "Products should be sorted Z-A: " + firstProduct + " to " + lastProduct);
        test.pass("Products correctly sorted Z-A: " + firstProduct + " ... " + lastProduct);
    }

    /**
     * Test: Sort products by price (low to high)
     *
     * Purpose: Verify price sorting in ascending order
     * Expected Result: Products sorted from lowest to highest price
     * Assertion: Verify price ordering is correct
     */
    @Test(groups = "Positive", priority = 4)
    public void testSortByPriceLowToHigh() {
        // Create report entry
        createTestReport("Sort by Price (Low to High)",
                "Verify products sorted from lowest to highest price");

        // Sort by price low to high
        productsPage.sortProducts("Price (low to high)");

        // Get prices as strings and convert to double for comparison
        List<String> prices = productsPage.getProductPrices();

        // Convert price strings to doubles (remove $ symbol)
        double[] priceValues = new double[prices.size()];
        for (int i = 0; i < prices.size(); i++) {
            String priceStr = prices.get(i).replace("$", "");
            priceValues[i] = Double.parseDouble(priceStr);
        }

        // Verify prices are in ascending order
        for (int i = 0; i < priceValues.length - 1; i++) {
            Assert.assertTrue(priceValues[i] <= priceValues[i + 1],
                    "Prices should be in ascending order");
        }
        test.pass("Prices correctly sorted low to high: " + prices.get(0) +
                " to " + prices.get(prices.size() - 1));
    }

    /**
     * Test: Sort products by price (high to low)
     *
     * Purpose: Verify price sorting in descending order
     * Expected Result: Products sorted from highest to lowest price
     * Assertion: Verify price ordering is correct
     */
    @Test(groups = "Positive", priority = 5)
    public void testSortByPriceHighToLow() {
        // Create report entry
        createTestReport("Sort by Price (High to Low)",
                "Verify products sorted from highest to lowest price");

        // Sort by price high to low
        productsPage.sortProducts("Price (high to low)");

        // Get prices
        List<String> prices = productsPage.getProductPrices();

        // Convert to doubles for comparison
        double[] priceValues = new double[prices.size()];
        for (int i = 0; i < prices.size(); i++) {
            String priceStr = prices.get(i).replace("$", "");
            priceValues[i] = Double.parseDouble(priceStr);
        }

        // Verify prices are in descending order
        for (int i = 0; i < priceValues.length - 1; i++) {
            Assert.assertTrue(priceValues[i] >= priceValues[i + 1],
                    "Prices should be in descending order");
        }
        test.pass("Prices correctly sorted high to low: " + prices.get(0) +
                " to " + prices.get(prices.size() - 1));
    }

    /**
     * Test: Navigate to product detail page
     *
     * Purpose: Verify clicking on product takes to detail page
     * Expected Result: Product detail page should load with correct product info
     * Assertion: Product name on detail page matches clicked product
     */
    @Test(groups = "Positive", priority = 6)
    public void testNavigateToProductDetail() {
        // Create report entry
        createTestReport("Navigate to Product Detail",
                "Verify clicking product navigates to detail page with correct info");

        // Get first product name
        List<String> productNames = productsPage.getProductNames();
        String selectedProduct = productNames.get(0);

        // Click on the product to view details
        ProductDetailPage detailPage = productsPage.clickOnProduct(selectedProduct);

        // Verify detail page loaded
        Assert.assertTrue(detailPage.isProductDetailPageLoaded(),
                "Product detail page should be loaded");

        // Verify product name matches
        String detailPageProductName = detailPage.getProductName();
        Assert.assertEquals(detailPageProductName, selectedProduct,
                "Product name on detail page should match selected product");
        test.pass("Navigated to product detail page: " + selectedProduct);
    }

    /**
     * Test: Verify product information on detail page matches listing page
     *
     * Purpose: Ensure product data consistency between listing and detail views
     * Expected Result: Name and price should match between pages
     * Assertion: Product details should be consistent
     */
    @Test(groups = "Positive", priority = 7)
    public void testProductDetailDataConsistency() {
        // Create report entry
        createTestReport("Product Detail Data Consistency",
                "Verify product info consistency between listing and detail pages");

        // Get first product from listing
        List<String> listingNames = productsPage.getProductNames();
        List<String> listingPrices = productsPage.getProductPrices();
        String selectedProductName = listingNames.get(0);
        String selectedProductPrice = listingPrices.get(0);

        // Navigate to detail page
        ProductDetailPage detailPage = productsPage.clickOnProduct(selectedProductName);

        // Get product info from detail page
        String detailName = detailPage.getProductName();
        String detailPrice = detailPage.getProductPrice();

        // Verify consistency
        Assert.assertEquals(detailName, selectedProductName,
                "Product name should match between pages");
        Assert.assertEquals(detailPrice, selectedProductPrice,
                "Product price should match between pages");

        // Verify description exists
        String description = detailPage.getProductDescription();
        Assert.assertFalse(description.isEmpty(),
                "Product description should be displayed");
        test.pass("Product details verified: " + selectedProductName + " - " + selectedProductPrice);
    }

    /**
     * Test: Add product to cart from detail page
     *
     * Purpose: Verify "Add to Cart" button works on product detail page
     * Expected Result: Product added to cart, cart count should increase
     * Assertion: Item should be added to cart successfully
     */
    @Test(groups = "Positive", priority = 8)
    public void testAddToCartFromDetailPage() {
        // Create report entry
        createTestReport("Add to Cart from Detail Page",
                "Verify adding product to cart from detail page works");

        // Get initial cart count
        int initialCartCount = productsPage.getCartItemCount();

        // Navigate to product detail
        List<String> productNames = productsPage.getProductNames();
        String selectedProduct = productNames.get(0);
        ProductDetailPage detailPage = productsPage.clickOnProduct(selectedProduct);

        // Add product to cart from detail page
        detailPage.addToCart();
        test.info("Product added to cart from detail page");

        // Go back to products page to verify cart count
        ProductListingPage updatedProductsPage = detailPage.clickBackButton();
        int updatedCartCount = updatedProductsPage.getCartItemCount();

        // Verify cart count increased
        Assert.assertEquals(updatedCartCount, initialCartCount + 1,
                "Cart count should increase by 1");
        test.pass("Successfully added product to cart from detail page");
    }

    /**
     * Test: Return to products from detail page using back button
     *
     * Purpose: Verify back button correctly returns to product listing
     * Expected Result: Back button should return to products page
     * Assertion: Should be on product listing page with products visible
     */
    @Test(groups = "Positive", priority = 9)
    public void testBackButtonFromDetailPage() {
        // Create report entry
        createTestReport("Back Button from Detail Page",
                "Verify back button returns to product listing page");

        // Navigate to detail page
        List<String> productNames = productsPage.getProductNames();
        ProductDetailPage detailPage = productsPage.clickOnProduct(productNames.get(0));

        // Verify on detail page
        Assert.assertTrue(detailPage.isProductDetailPageLoaded(),
                "Should be on detail page");

        // Click back button
        ProductListingPage backToProducts = detailPage.clickBackButton();

        // Verify back on products page
        Assert.assertTrue(backToProducts.isProductsPageLoaded(),
                "Should return to products page");
        Assert.assertEquals(backToProducts.getProductCount(), 6,
                "All 6 products should be displayed");
        test.pass("Successfully returned to product listing page using back button");
    }
}
