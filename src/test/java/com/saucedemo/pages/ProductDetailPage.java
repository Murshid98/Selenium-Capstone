package com.saucedemo.pages;

import com.saucedemo.utilities.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * ProductDetailPage - Page Object Model (POM) class for Product Detail page
 *
 * This class encapsulates all UI elements and interactions on the individual product detail page.
 * Displays detailed information about a specific product and allows adding to cart.
 *
 * Page URL: https://www.saucedemo.com/inventory-item.html?id=[itemId]
 */
public class ProductDetailPage {

    // WebDriver instance
    private WebDriver driver;

    // WaitUtility for explicit waits
    private WaitUtility waitUtility;

    // ===== LOCATORS - All UI element locators for this page =====

    // Product name header
    private By productName = By.className("inventory_details_name");

    // Product price
    private By productPrice = By.className("inventory_details_price");

    // Product description
    private By productDescription = By.className("inventory_details_desc");

    // Add to cart button on detail page
    private By addToCartButton = By.xpath("//button[contains(text(), 'Add to cart')]");

    // Back to products button
    private By backButton = By.id("back-to-products");

    // Product image
    private By productImage = By.className("inventory_details_img");

    /**
     * Constructor - Initialize ProductDetailPage with WebDriver instance
     * @param driver - WebDriver instance
     */
    public ProductDetailPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtility = new WaitUtility(driver);
    }

    /**
     * Verify that product detail page is loaded by checking for product name
     * @return true if page is loaded, false otherwise
     */
    public boolean isProductDetailPageLoaded() {
        try {
            WebElement name = waitUtility.waitForElementToBeVisible(driver.findElement(productName));
            return name != null && name.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the product name displayed on detail page
     * @return Product name string
     */
    public String getProductName() {
        WebElement name = driver.findElement(productName);
        waitUtility.waitForElementToBeVisible(name);
        return name.getText();
    }

    /**
     * Get the product price displayed on detail page
     * @return Product price string (e.g., "$29.99")
     */
    public String getProductPrice() {
        WebElement price = driver.findElement(productPrice);
        waitUtility.waitForElementToBeVisible(price);
        return price.getText();
    }

    /**
     * Get the product description displayed on detail page
     * @return Product description text
     */
    public String getProductDescription() {
        WebElement description = driver.findElement(productDescription);
        waitUtility.waitForElementToBeVisible(description);
        return description.getText();
    }

    /**
     * Click the "Add to Cart" button on product detail page
     * Returns the same page as we stay on detail page after adding to cart
     */
    public void addToCart() {
        WebElement addBtn = driver.findElement(addToCartButton);
        waitUtility.waitForElementToBeClickable(addBtn);
        addBtn.click();
    }

    /**
     * Check if product image is displayed
     * Used to verify page has fully loaded
     * @return true if image is visible, false otherwise
     */
    public boolean isProductImageDisplayed() {
        try {
            WebElement image = driver.findElement(productImage);
            return image.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click the back button to return to products listing page
     * @return ProductListingPage instance
     */
    public ProductListingPage clickBackButton() {
        try {
            WebElement back = waitUtility.waitForElementToBeClickable(driver.findElement(backButton));
            back.click();
        } catch (Exception e) {
            // Fallback: Try to find the back button again
            WebElement back = waitUtility.waitForElementToBeVisible(driver.findElement(backButton));
            back.click();
        }
        return new ProductListingPage(driver);
    }

    /**
     * Get all product details as a combined string
     * Useful for assertion and comparison
     * @return String containing name, price, and description
     */
    public String getCompleteProductInfo() {
        String name = getProductName();
        String price = getProductPrice();
        String description = getProductDescription();
        return name + " - " + price + " - " + description;
    }
}
