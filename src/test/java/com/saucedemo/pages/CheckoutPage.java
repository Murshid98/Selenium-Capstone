package com.saucedemo.pages;

import com.saucedemo.utilities.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CheckoutPage - Page Factory model for Checkout pages
 *
 * Covers all three checkout steps:
 *   Step One  – Shipping Information form
 *   Step Two  – Order Review summary
 *   Complete  – Order Confirmation
 */
public class CheckoutPage {

    private WebDriver driver;
    private WaitUtility waitUtility;

    // ===== CHECKOUT STEP ONE =====

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(xpath = "//h3[@data-test='error']")
    private WebElement checkoutErrorMessage;

    // ===== CHECKOUT STEP TWO =====

    @FindBy(className = "payment_info")
    private WebElement paymentInfo;

    @FindBy(className = "shipping_info")
    private WebElement shippingInfo;

    @FindBy(xpath = "//div[contains(text(), 'Item total')]//following-sibling::div")
    private WebElement itemTotal;

    @FindBy(xpath = "//div[contains(text(), 'Tax')]//following-sibling::div")
    private WebElement taxAmount;

    @FindBy(xpath = "//div[contains(text(), 'Total')]//following-sibling::div")
    private WebElement totalAmount;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    // ===== CHECKOUT COMPLETE =====

    @FindBy(xpath = "//h2[contains(text(), 'Thank you')]")
    private WebElement confirmationMessage;

    @FindBy(className = "complete-text")
    private WebElement confirmationText;

    @FindBy(id = "back-to-products")
    private WebElement backHomeButton;

    // Order review items (step two)
    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    // Relative selector used within individual cart item elements
    private static final By ITEM_NAME_SELECTOR = By.className("inventory_item_name");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtility = new WaitUtility(driver);
        PageFactory.initElements(driver, this);
    }

    // ── Step One ─────────────────────────────────────────────────────────────

    public boolean isCheckoutStepOneLoaded() {
        try {
            return firstNameField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterFirstName(String firstName) {
        waitUtility.waitForElementToBeVisible(firstNameField);
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        waitUtility.waitForElementToBeVisible(lastNameField);
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
    }

    public void enterPostalCode(String postalCode) {
        waitUtility.waitForElementToBeVisible(postalCodeField);
        postalCodeField.clear();
        postalCodeField.sendKeys(postalCode);
    }

    public void fillCheckoutForm(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }

    public CheckoutPage clickContinue() {
        waitUtility.waitForElementToBeClickable(continueButton);
        continueButton.click();
        return this;
    }

    public String getCheckoutError() {
        try {
            waitUtility.waitForElementToBeVisible(checkoutErrorMessage);
            return checkoutErrorMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isCheckoutErrorDisplayed() {
        try {
            return checkoutErrorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ── Step Two ─────────────────────────────────────────────────────────────

    public boolean isCheckoutStepTwoLoaded() {
        try {
            waitUtility.waitForElementToBeVisible(finishButton);
            return finishButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getItemTotal() {
        try {
            waitUtility.waitForElementToBeVisible(itemTotal);
            return itemTotal.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getTaxAmount() {
        try {
            waitUtility.waitForElementToBeVisible(taxAmount);
            return taxAmount.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getTotalAmount() {
        try {
            waitUtility.waitForElementToBeVisible(totalAmount);
            return totalAmount.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public List<String> getOrderReviewItems() {
        return cartItems.stream()
                .map(item -> item.findElement(ITEM_NAME_SELECTOR).getText())
                .collect(Collectors.toList());
    }

    public CheckoutPage clickFinish() {
        waitUtility.waitForElementToBeClickable(finishButton);
        finishButton.click();
        return this;
    }

    public ShoppingCartPage clickCancel() {
        waitUtility.waitForElementToBeClickable(cancelButton);
        cancelButton.click();
        return new ShoppingCartPage(driver);
    }

    // ── Confirmation ─────────────────────────────────────────────────────────

    public boolean isOrderConfirmationLoaded() {
        try {
            return confirmationMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getConfirmationMessage() {
        try {
            waitUtility.waitForElementToBeVisible(confirmationMessage);
            return confirmationMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getConfirmationText() {
        try {
            waitUtility.waitForElementToBeVisible(confirmationText);
            return confirmationText.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public ProductListingPage clickBackToProducts() {
        waitUtility.waitForElementToBeClickable(backHomeButton);
        backHomeButton.click();
        return new ProductListingPage(driver);
    }
}
