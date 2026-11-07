package com.saucedemo.pages;

import com.saucedemo.utilities.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CheckoutPage - Page Object Model (POM) class for Checkout pages
 *
 * This class encapsulates UI elements and interactions for checkout flow including:
 * - Checkout Step One: Shipping Information (First Name, Last Name, Postal Code)
 * - Checkout Step Two: Order Review
 * - Checkout Complete: Confirmation
 *
 * Note: SauceDemo has multiple checkout steps accessed via different URLs
 */
public class CheckoutPage {

    // WebDriver instance
    private WebDriver driver;

    // WaitUtility for explicit waits
    private WaitUtility waitUtility;

    // ===== LOCATORS - CHECKOUT STEP ONE =====

    // First name input field
    private By firstNameField = By.id("first-name");

    // Last name input field
    private By lastNameField = By.id("last-name");

    // Postal code input field
    private By postalCodeField = By.id("postal-code");

    // Continue button to proceed to step two
    private By continueButton = By.id("continue");

    // Error message on checkout step one
    private By checkoutErrorMessage = By.xpath("//h3[@data-test='error']");

    // ===== LOCATORS - CHECKOUT STEP TWO =====

    // Payment info section
    private By paymentInfo = By.className("payment_info");

    // Shipping info section
    private By shippingInfo = By.className("shipping_info");

    // Item total text
    private By itemTotal = By.xpath("//div[contains(text(), 'Item total')]//following-sibling::div");

    // Tax amount text
    private By taxAmount = By.xpath("//div[contains(text(), 'Tax')]//following-sibling::div");

    // Total amount text
    private By totalAmount = By.xpath("//div[contains(text(), 'Total')]//following-sibling::div");

    // Finish button to complete order
    private By finishButton = By.id("finish");

    // Cancel button
    private By cancelButton = By.id("cancel");

    // ===== LOCATORS - CHECKOUT COMPLETE =====

    // Order confirmation message
    private By confirmationMessage = By.xpath("//h2[contains(text(), 'Thank you')]");

    // Confirmation text body
    private By confirmationText = By.className("complete-text");

    // Back home button on confirmation page
    private By backHomeButton = By.id("back-to-products");

    // Cart items list on step two
    private By cartItems = By.className("cart_item");

    // Item name in cart (step two)
    private By itemName = By.className("inventory_item_name");

    /**
     * Constructor - Initialize CheckoutPage with WebDriver instance
     * @param driver - WebDriver instance
     */
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtility = new WaitUtility(driver);
    }

    // ============ CHECKOUT STEP ONE METHODS ============

    /**
     * Verify checkout step one page is loaded
     * @return true if page is loaded, false otherwise
     */
    public boolean isCheckoutStepOneLoaded() {
        try {
            WebElement firstName = driver.findElement(firstNameField);
            return firstName.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Enter first name in checkout form
     * @param firstName - First name to enter
     */
    public void enterFirstName(String firstName) {
        WebElement field = driver.findElement(firstNameField);
        waitUtility.waitForElementToBeVisible(field);
        field.clear();
        field.sendKeys(firstName);
    }

    /**
     * Enter last name in checkout form
     * @param lastName - Last name to enter
     */
    public void enterLastName(String lastName) {
        WebElement field = driver.findElement(lastNameField);
        waitUtility.waitForElementToBeVisible(field);
        field.clear();
        field.sendKeys(lastName);
    }

    /**
     * Enter postal code in checkout form
     * @param postalCode - Postal code to enter
     */
    public void enterPostalCode(String postalCode) {
        WebElement field = driver.findElement(postalCodeField);
        waitUtility.waitForElementToBeVisible(field);
        field.clear();
        field.sendKeys(postalCode);
    }

    /**
     * Fill all checkout step one fields
     * @param firstName - First name
     * @param lastName - Last name
     * @param postalCode - Postal code
     */
    public void fillCheckoutForm(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }

    /**
     * Click continue button to proceed to step two
     * @return CheckoutPage instance (same page as step transitions)
     */
    public CheckoutPage clickContinue() {
        WebElement continueBtn = waitUtility.waitForElementToBeClickable(driver.findElement(continueButton));
        continueBtn.click();
        return this;
    }

    /**
     * Get error message displayed on checkout step one
     * @return Error message text
     */
    public String getCheckoutError() {
        try {
            WebElement error = driver.findElement(checkoutErrorMessage);
            waitUtility.waitForElementToBeVisible(error);
            return error.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if error message is displayed on checkout
     * @return true if error visible, false otherwise
     */
    public boolean isCheckoutErrorDisplayed() {
        try {
            WebElement error = driver.findElement(checkoutErrorMessage);
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ============ CHECKOUT STEP TWO METHODS ============

    /**
     * Verify checkout step two page is loaded (review order page)
     * @return true if page is loaded, false otherwise
     */
    public boolean isCheckoutStepTwoLoaded() {
        try {
            WebElement finish = waitUtility.waitForElementToBeVisible(driver.findElement(finishButton));
            return finish != null && finish.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get item total from order summary
     * @return Item total amount as string
     */
    public String getItemTotal() {
        try {
            WebElement total = driver.findElement(itemTotal);
            waitUtility.waitForElementToBeVisible(total);
            return total.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get tax amount from order summary
     * @return Tax amount as string
     */
    public String getTaxAmount() {
        try {
            WebElement tax = driver.findElement(taxAmount);
            waitUtility.waitForElementToBeVisible(tax);
            return tax.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get total amount from order summary (item total + tax)
     * @return Total amount as string
     */
    public String getTotalAmount() {
        try {
            WebElement total = driver.findElement(totalAmount);
            waitUtility.waitForElementToBeVisible(total);
            return total.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get list of items in order review (step two)
     * @return List of item names
     */
    public List<String> getOrderReviewItems() {
        List<WebElement> items = driver.findElements(cartItems);
        return items.stream()
                .map(item -> item.findElement(itemName).getText())
                .collect(Collectors.toList());
    }

    /**
     * Click Finish button to complete the order
     * @return CheckoutPage instance (will navigate to confirmation page)
     */
    public CheckoutPage clickFinish() {
        WebElement finishBtn = driver.findElement(finishButton);
        waitUtility.waitForElementToBeClickable(finishBtn);
        finishBtn.click();
        return this;
    }

    /**
     * Click Cancel button to go back to cart
     * @return ShoppingCartPage instance
     */
    public ShoppingCartPage clickCancel() {
        WebElement cancelBtn = driver.findElement(cancelButton);
        waitUtility.waitForElementToBeClickable(cancelBtn);
        cancelBtn.click();
        return new ShoppingCartPage(driver);
    }

    // ============ CHECKOUT COMPLETE (CONFIRMATION) METHODS ============

    /**
     * Verify order confirmation page is loaded
     * @return true if confirmation page is displayed, false otherwise
     */
    public boolean isOrderConfirmationLoaded() {
        try {
            WebElement confirmation = driver.findElement(confirmationMessage);
            return confirmation.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get confirmation message from completion page
     * @return Confirmation message text
     */
    public String getConfirmationMessage() {
        try {
            WebElement message = driver.findElement(confirmationMessage);
            waitUtility.waitForElementToBeVisible(message);
            return message.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get confirmation text body with order details
     * @return Confirmation text content
     */
    public String getConfirmationText() {
        try {
            WebElement text = driver.findElement(confirmationText);
            waitUtility.waitForElementToBeVisible(text);
            return text.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Click back to products button on confirmation page
     * @return ProductListingPage instance
     */
    public ProductListingPage clickBackToProducts() {
        WebElement backBtn = driver.findElement(backHomeButton);
        waitUtility.waitForElementToBeClickable(backBtn);
        backBtn.click();
        return new ProductListingPage(driver);
    }
}
