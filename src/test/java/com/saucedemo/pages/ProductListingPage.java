package com.saucedemo.pages;

import com.saucedemo.utilities.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProductListingPage - Page Object Model (POM) class for the Products Listing page
 *
 * This class encapsulates all UI elements and interactions on the products inventory page.
 * Handles product display, filtering, sorting, and navigation operations.
 *
 * Page URL: https://www.saucedemo.com/inventory.html
 */
public class ProductListingPage {

    // WebDriver instance
    private WebDriver driver;

    // WaitUtility for explicit waits
    private WaitUtility waitUtility;

    // ===== LOCATORS - All UI element locators for this page =====

    // Product container locator (parent div containing all products)
    private By productContainer = By.className("inventory_list");

    // Individual product item locators
    private By productItems = By.className("inventory_item");

    // Product name locator (relative to each product item)
    private By productName = By.className("inventory_item_name");

    // Product price locator (relative to each product item)
    private By productPrice = By.className("inventory_item_price");

    // Add to cart button (relative to each product item)
    private By addToCartButton = By.xpath(".//button[contains(text(), 'Add to cart')]");

    // Sort dropdown locator
    private By sortDropdown = By.className("product_sort_container");

    // Cart badge showing number of items in cart
    private By cartBadge = By.className("shopping_cart_badge");

    // Cart icon/link
    private By cartIcon = By.className("shopping_cart_link");

    // Menu button to open sidebar
    private By menuButton = By.id("react-burger-menu-btn");

    // Sidebar menu
    private By sidebar = By.className("bm-menu");

    // All Items link in sidebar
    private By allItemsLink = By.id("inventory_sidebar_link");

    // About link in sidebar
    private By aboutLink = By.id("about_sidebar_link");

    // Logout link in sidebar
    private By logoutLink = By.id("logout_sidebar_link");

    // Reset App State link in sidebar
    private By resetAppStateLink = By.id("reset_sidebar_link");

    // Menu close button
    private By menuCloseButton = By.id("react-burger-cross-btn");

    /**
     * Constructor - Initialize ProductListingPage with WebDriver instance
     * @param driver - WebDriver instance passed from test or page class
     */
    public ProductListingPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtility = new WaitUtility(driver);
    }

    /**
     * Verify that products page is loaded by checking for product container
     * @return true if page is loaded, false otherwise
     */
    public boolean isProductsPageLoaded() {
        try {
            WebElement container = driver.findElement(productContainer);
            return container.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get count of all products displayed on the page
     * @return Number of products visible
     */
    public int getProductCount() {
        List<WebElement> products = driver.findElements(productItems);
        return products.size();
    }

    /**
     * Get list of all product names currently displayed
     * @return List of product name strings
     */
    public List<String> getProductNames() {
        List<WebElement> products = driver.findElements(productItems);
        return products.stream()
                .map(product -> product.findElement(productName).getText())
                .collect(Collectors.toList());
    }

    /**
     * Get list of all product prices currently displayed
     * @return List of product price strings
     */
    public List<String> getProductPrices() {
        List<WebElement> products = driver.findElements(productItems);
        return products.stream()
                .map(product -> product.findElement(productPrice).getText())
                .collect(Collectors.toList());
    }

    /**
     * Sort products by a specific option from dropdown
     * Available options: "Name (A to Z)", "Name (Z to A)", "Price (low to high)", "Price (high to low)"
     * @param sortOption - The sorting option to select
     */
    public void sortProducts(String sortOption) {
        WebElement dropdown = driver.findElement(sortDropdown);
        Select select = new Select(dropdown);
        select.selectByVisibleText(sortOption);
        // Wait a moment for sorting to complete
        try {
            Thread.sleep(500); // Brief pause to allow sorting animation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Click on a product to navigate to its detail page
     * @param productName - Name of the product to click
     * @return ProductDetailPage instance
     */
    public ProductDetailPage clickOnProduct(String productName) {
        List<WebElement> products = driver.findElements(productItems);
        for (WebElement product : products) {
            WebElement name = product.findElement(this.productName);
            if (name.getText().equals(productName)) {
                waitUtility.waitForElementToBeClickable(name);
                name.click();
                return new ProductDetailPage(driver);
            }
        }
        throw new IllegalArgumentException("Product not found: " + productName);
    }

    /**
     * Add a product to cart by product name
     * @param productName - Name of the product to add to cart
     */
    public void addProductToCart(String productName) {
        List<WebElement> products = driver.findElements(productItems);
        for (WebElement product : products) {
            WebElement name = product.findElement(this.productName);
            if (name.getText().equals(productName)) {
                WebElement addButton = product.findElement(addToCartButton);
                waitUtility.waitForElementToBeClickable(addButton);
                addButton.click();
                return;
            }
        }
        throw new IllegalArgumentException("Product not found: " + productName);
    }

    /**
     * Get the current cart item count from the badge
     * @return Number of items in cart, or 0 if badge is not displayed
     */
    public int getCartItemCount() {
        try {
            WebElement badge = driver.findElement(cartBadge);
            String countText = badge.getText();
            return Integer.parseInt(countText);
        } catch (Exception e) {
            // Badge not present means 0 items
            return 0;
        }
    }

    /**
     * Click on the cart icon to navigate to shopping cart
     * @return ShoppingCartPage instance
     */
    public ShoppingCartPage clickOnCart() {
        WebElement cart = waitUtility.waitForElementToBeClickable(driver.findElement(cartIcon));
        cart.click();
        return new ShoppingCartPage(driver);
    }

    /**
     * Open the sidebar menu
     */
    public void openMenu() {
        WebElement menu = driver.findElement(menuButton);
        waitUtility.waitForElementToBeClickable(menu);
        menu.click();
        // Wait for menu to appear
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Close the sidebar menu
     */
    public void closeMenu() {
        try {
            WebElement closeBtn = driver.findElement(menuCloseButton);
            waitUtility.waitForElementToBeClickable(closeBtn);
            closeBtn.click();
        } catch (Exception e) {
            // Menu might already be closed
        }
    }

    /**
     * Click on "All Items" link in sidebar menu
     * Remains on same page (navigates to inventory.html if not already there)
     */
    public void clickAllItemsLink() {
        openMenu();
        WebElement allItems = driver.findElement(allItemsLink);
        waitUtility.waitForElementToBeClickable(allItems);
        allItems.click();
        closeMenu();
    }

    /**
     * Click on "About" link in sidebar menu
     * Navigates away from the application
     * @return This page object (browser would navigate away)
     */
    public void clickAboutLink() {
        openMenu();
        WebElement about = driver.findElement(aboutLink);
        waitUtility.waitForElementToBeClickable(about);
        about.click();
    }

    /**
     * Click on "Logout" link in sidebar menu
     * Navigates back to login page
     * @return LoginPage instance
     */
    public LoginPage clickLogoutLink() {
        openMenu();
        WebElement logout = driver.findElement(logoutLink);
        waitUtility.waitForElementToBeClickable(logout);
        logout.click();
        return new LoginPage(driver);
    }

    /**
     * Click on "Reset App State" link in sidebar menu
     * Resets cart and any applied filters
     */
    public void clickResetAppStateLink() {
        openMenu();
        WebElement resetAppState = driver.findElement(resetAppStateLink);
        waitUtility.waitForElementToBeClickable(resetAppState);
        resetAppState.click();
        closeMenu();
    }

    /**
     * Check if a product is in the list by name
     * @param productName - Name of product to search for
     * @return true if product found, false otherwise
     */
    public boolean isProductInList(String productName) {
        List<String> productNames = getProductNames();
        return productNames.contains(productName);
    }
}
