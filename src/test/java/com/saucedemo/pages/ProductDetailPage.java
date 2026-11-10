package com.saucedemo.pages;

import com.saucedemo.utilities.WaitUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * ProductDetailPage - Page Factory model for the Product Detail page
 *
 * Page URL: https://www.saucedemo.com/inventory-item.html?id=[itemId]
 */
public class ProductDetailPage {

    private WebDriver driver;
    private WaitUtility waitUtility;

    // ===== PAGE FACTORY ELEMENTS =====

    @FindBy(className = "inventory_details_name")
    private WebElement productName;

    @FindBy(className = "inventory_details_price")
    private WebElement productPrice;

    @FindBy(className = "inventory_details_desc")
    private WebElement productDescription;

    @FindBy(xpath = "//button[contains(text(), 'Add to cart')]")
    private WebElement addToCartButton;

    @FindBy(id = "back-to-products")
    private WebElement backButton;

    @FindBy(className = "inventory_details_img")
    private WebElement productImage;

    public ProductDetailPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtility = new WaitUtility(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean isProductDetailPageLoaded() {
        try {
            waitUtility.waitForElementToBeVisible(productName);
            return productName.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getProductName() {
        waitUtility.waitForElementToBeVisible(productName);
        return productName.getText();
    }

    public String getProductPrice() {
        waitUtility.waitForElementToBeVisible(productPrice);
        return productPrice.getText();
    }

    public String getProductDescription() {
        waitUtility.waitForElementToBeVisible(productDescription);
        return productDescription.getText();
    }

    public void addToCart() {
        waitUtility.waitForElementToBeClickable(addToCartButton);
        addToCartButton.click();
    }

    public boolean isProductImageDisplayed() {
        try {
            return productImage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public ProductListingPage clickBackButton() {
        try {
            waitUtility.waitForElementToBeClickable(backButton);
            backButton.click();
        } catch (Exception e) {
            waitUtility.waitForElementToBeVisible(backButton);
            backButton.click();
        }
        return new ProductListingPage(driver);
    }

    public String getCompleteProductInfo() {
        return getProductName() + " - " + getProductPrice() + " - " + getProductDescription();
    }
}
