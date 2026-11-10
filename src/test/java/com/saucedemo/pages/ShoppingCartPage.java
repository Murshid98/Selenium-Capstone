package com.saucedemo.pages;

import com.saucedemo.utilities.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ShoppingCartPage - Page Factory model for the Shopping Cart page
 *
 * Page URL: https://www.saucedemo.com/cart.html
 */
public class ShoppingCartPage {

    private WebDriver driver;
    private WaitUtility waitUtility;

    // ===== PAGE FACTORY ELEMENTS =====

    @FindBy(className = "cart_list")
    private WebElement cartContainer;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(xpath = "//button[contains(text(), 'Checkout')]")
    private WebElement checkoutButton;

    @FindBy(className = "title")
    private WebElement cartPageTitle;

    // Relative selectors used within individual cart item elements
    private static final By ITEM_NAME_SELECTOR     = By.className("inventory_item_name");
    private static final By ITEM_QUANTITY_SELECTOR = By.className("cart_quantity");
    private static final By ITEM_PRICE_SELECTOR    = By.className("inventory_item_price");
    private static final By REMOVE_BUTTON_SELECTOR = By.xpath(".//button[contains(text(), 'Remove')]");

    public ShoppingCartPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtility = new WaitUtility(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean isCartPageLoaded() {
        try {
            waitUtility.waitForElementToBeVisible(cartContainer);
            return cartContainer.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getCartItemCount() {
        try {
            waitUtility.waitForElementToBeVisible(cartContainer);
        } catch (Exception e) {
            // Container might not exist if cart is empty
        }
        return cartItems.size();
    }

    public List<String> getCartItemNames() {
        try {
            waitUtility.waitForElementToBeVisible(cartContainer);
        } catch (Exception e) {
            return Collections.emptyList();
        }
        return cartItems.stream()
                .map(item -> item.findElement(ITEM_NAME_SELECTOR).getText())
                .collect(Collectors.toList());
    }

    public List<String> getCartItemPrices() {
        try {
            waitUtility.waitForElementToBeVisible(cartContainer);
        } catch (Exception e) {
            return Collections.emptyList();
        }
        return cartItems.stream()
                .map(item -> item.findElement(ITEM_PRICE_SELECTOR).getText())
                .collect(Collectors.toList());
    }

    public List<String> getCartItemQuantities() {
        try {
            waitUtility.waitForElementToBeVisible(cartContainer);
        } catch (Exception e) {
            return Collections.emptyList();
        }
        return cartItems.stream()
                .map(item -> item.findElement(ITEM_QUANTITY_SELECTOR).getText())
                .collect(Collectors.toList());
    }

    public void removeItemFromCart(String itemName) {
        for (WebElement item : cartItems) {
            WebElement name = item.findElement(ITEM_NAME_SELECTOR);
            if (name.getText().equals(itemName)) {
                WebElement removeBtn = item.findElement(REMOVE_BUTTON_SELECTOR);
                waitUtility.waitForElementToBeClickable(removeBtn);
                removeBtn.click();
                return;
            }
        }
        throw new IllegalArgumentException("Item not found in cart: " + itemName);
    }

    public boolean isItemInCart(String itemName) {
        return getCartItemNames().contains(itemName);
    }

    public String[] getItemDetails(String itemName) {
        for (WebElement item : cartItems) {
            WebElement name = item.findElement(ITEM_NAME_SELECTOR);
            if (name.getText().equals(itemName)) {
                String quantity = item.findElement(ITEM_QUANTITY_SELECTOR).getText();
                String price    = item.findElement(ITEM_PRICE_SELECTOR).getText();
                return new String[]{itemName, quantity, price};
            }
        }
        throw new IllegalArgumentException("Item not found in cart: " + itemName);
    }

    public ProductListingPage clickContinueShopping() {
        waitUtility.waitForElementToBeClickable(continueShoppingButton);
        continueShoppingButton.click();
        return new ProductListingPage(driver);
    }

    public CheckoutPage clickCheckout() {
        try {
            waitUtility.waitForElementToBeClickable(checkoutButton);
            checkoutButton.click();
        } catch (Exception e) {
            throw new RuntimeException("Checkout button not found or not clickable. Ensure items are in cart.");
        }
        return new CheckoutPage(driver);
    }

    public boolean isCartEmpty() {
        return getCartItemCount() == 0;
    }
}
