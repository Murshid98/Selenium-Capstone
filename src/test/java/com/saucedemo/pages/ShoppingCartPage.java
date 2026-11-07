package com.saucedemo.pages;

import com.saucedemo.utilities.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ShoppingCartPage - Page Object Model (POM) class for Shopping Cart page
 *
 * This class encapsulates all UI elements and interactions on the shopping cart page.
 * Handles cart item display, removal, and navigation to checkout.
 *
 * Page URL: https://www.saucedemo.com/cart.html
 */
public class ShoppingCartPage {

    // WebDriver instance
    private WebDriver driver;

    // WaitUtility for explicit waits
    private WaitUtility waitUtility;

    // ===== LOCATORS - All UI element locators for this page =====

    // Cart container holding all cart items
    private By cartContainer = By.className("cart_list");

    // Individual cart item elements
    private By cartItems = By.className("cart_item");

    // Item name within cart item
    private By itemName = By.className("inventory_item_name");

    // Item quantity within cart item
    private By itemQuantity = By.className("cart_quantity");

    // Item price within cart item
    private By itemPrice = By.className("inventory_item_price");

    // Remove button for each cart item
    private By removeButton = By.xpath(".//button[contains(text(), 'Remove')]");

    // Continue Shopping button
    private By continueShoppingButton = By.id("continue-shopping");

    // Checkout button - using xpath for reliability as button text is "Checkout"
    private By checkoutButton = By.xpath("//button[contains(text(), 'Checkout')]");

    // Page title/header
    private By cartPageTitle = By.className("title");

    /**
     * Constructor - Initialize ShoppingCartPage with WebDriver instance
     * @param driver - WebDriver instance
     */
    public ShoppingCartPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtility = new WaitUtility(driver);
    }

    /**
     * Verify that cart page is loaded by checking for cart container
     * @return true if page is loaded, false otherwise
     */
    public boolean isCartPageLoaded() {
        try {
            WebElement container = waitUtility.waitForElementToBeVisible(driver.findElement(cartContainer));
            return container != null && container.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get count of items currently in the cart
     * @return Number of items in cart
     */
    public int getCartItemCount() {
        try {
            waitUtility.waitForElementToBeVisible(driver.findElement(cartContainer));
        } catch (Exception e) {
            // Container might not exist if cart is empty
        }
        List<WebElement> items = driver.findElements(cartItems);
        return items.size();
    }

    /**
     * Get list of all item names in the cart
     * @return List of item name strings
     */
    public List<String> getCartItemNames() {
        try {
            waitUtility.waitForElementToBeVisible(driver.findElement(cartContainer));
        } catch (Exception e) {
            // Container might not exist if cart is empty, return empty list
            return java.util.Collections.emptyList();
        }
        List<WebElement> items = driver.findElements(cartItems);
        return items.stream()
                .map(item -> item.findElement(itemName).getText())
                .collect(Collectors.toList());
    }

    /**
     * Get list of all item prices in the cart
     * @return List of item price strings
     */
    public List<String> getCartItemPrices() {
        try {
            waitUtility.waitForElementToBeVisible(driver.findElement(cartContainer));
        } catch (Exception e) {
            // Container might not exist if cart is empty, return empty list
            return java.util.Collections.emptyList();
        }
        List<WebElement> items = driver.findElements(cartItems);
        return items.stream()
                .map(item -> item.findElement(itemPrice).getText())
                .collect(Collectors.toList());
    }

    /**
     * Get list of all item quantities in the cart
     * @return List of quantity values
     */
    public List<String> getCartItemQuantities() {
        try {
            waitUtility.waitForElementToBeVisible(driver.findElement(cartContainer));
        } catch (Exception e) {
            // Container might not exist if cart is empty, return empty list
            return java.util.Collections.emptyList();
        }
        List<WebElement> items = driver.findElements(cartItems);
        return items.stream()
                .map(item -> item.findElement(itemQuantity).getText())
                .collect(Collectors.toList());
    }

    /**
     * Remove a specific item from cart by its name
     * @param itemName - Name of the item to remove
     */
    public void removeItemFromCart(String itemName) {
        List<WebElement> items = driver.findElements(cartItems);
        for (WebElement item : items) {
            WebElement name = item.findElement(this.itemName);
            if (name.getText().equals(itemName)) {
                WebElement removeBtn = item.findElement(removeButton);
                waitUtility.waitForElementToBeClickable(removeBtn);
                removeBtn.click();
                return;
            }
        }
        throw new IllegalArgumentException("Item not found in cart: " + itemName);
    }

    /**
     * Check if a specific item is in the cart
     * @param itemName - Name of item to search for
     * @return true if item found in cart, false otherwise
     */
    public boolean isItemInCart(String itemName) {
        List<String> cartItemNames = getCartItemNames();
        return cartItemNames.contains(itemName);
    }

    /**
     * Get details of a specific item in the cart
     * @param itemName - Name of the item
     * @return Array containing [name, quantity, price]
     */
    public String[] getItemDetails(String itemName) {
        List<WebElement> items = driver.findElements(cartItems);
        for (WebElement item : items) {
            WebElement name = item.findElement(this.itemName);
            if (name.getText().equals(itemName)) {
                String quantity = item.findElement(itemQuantity).getText();
                String price = item.findElement(itemPrice).getText();
                return new String[]{itemName, quantity, price};
            }
        }
        throw new IllegalArgumentException("Item not found in cart: " + itemName);
    }

    /**
     * Click "Continue Shopping" button to return to products page
     * @return ProductListingPage instance
     */
    public ProductListingPage clickContinueShopping() {
        WebElement continueBtn = driver.findElement(continueShoppingButton);
        waitUtility.waitForElementToBeClickable(continueBtn);
        continueBtn.click();
        return new ProductListingPage(driver);
    }

    /**
     * Click "Checkout" button to proceed to checkout page
     * @return CheckoutPage instance
     */
    public CheckoutPage clickCheckout() {
        try {
            WebElement checkoutBtn = waitUtility.waitForElementToBeClickable(driver.findElement(checkoutButton));
            checkoutBtn.click();
        } catch (Exception e) {
            throw new RuntimeException("Checkout button not found or not clickable. Ensure items are in cart.");
        }
        return new CheckoutPage(driver);
    }

    /**
     * Verify cart is empty (no items displayed)
     * @return true if cart is empty, false if items present
     */
    public boolean isCartEmpty() {
        return getCartItemCount() == 0;
    }
}
