package com.saucedemo.pages;

import com.saucedemo.utilities.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProductListingPage - Page Factory model for the Products Listing page
 *
 * Page URL: https://www.saucedemo.com/inventory.html
 */
public class ProductListingPage {

    private WebDriver driver;
    private WaitUtility waitUtility;

    // ===== PAGE FACTORY ELEMENTS =====

    @FindBy(className = "inventory_list")
    private WebElement productContainer;

    @FindBy(className = "inventory_item")
    private List<WebElement> productItems;

    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(className = "bm-menu")
    private WebElement sidebar;

    @FindBy(id = "inventory_sidebar_link")
    private WebElement allItemsLink;

    @FindBy(id = "about_sidebar_link")
    private WebElement aboutLink;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(id = "reset_sidebar_link")
    private WebElement resetAppStateLink;

    @FindBy(id = "react-burger-cross-btn")
    private WebElement menuCloseButton;

    // These selectors are used relative to individual product elements, not page-level.
    private static final By PRODUCT_NAME_SELECTOR    = By.className("inventory_item_name");
    private static final By PRODUCT_PRICE_SELECTOR   = By.className("inventory_item_price");
    private static final By ADD_TO_CART_SELECTOR     = By.xpath(".//button[contains(text(), 'Add to cart')]");

    public ProductListingPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtility = new WaitUtility(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean isProductsPageLoaded() {
        try {
            return productContainer.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getProductCount() {
        return productItems.size();
    }

    public List<String> getProductNames() {
        return productItems.stream()
                .map(product -> product.findElement(PRODUCT_NAME_SELECTOR).getText())
                .collect(Collectors.toList());
    }

    public List<String> getProductPrices() {
        return productItems.stream()
                .map(product -> product.findElement(PRODUCT_PRICE_SELECTOR).getText())
                .collect(Collectors.toList());
    }

    public void sortProducts(String sortOption) {
        Select select = new Select(sortDropdown);
        select.selectByVisibleText(sortOption);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public ProductDetailPage clickOnProduct(String productName) {
        for (WebElement product : productItems) {
            WebElement name = product.findElement(PRODUCT_NAME_SELECTOR);
            if (name.getText().equals(productName)) {
                waitUtility.waitForElementToBeClickable(name);
                name.click();
                return new ProductDetailPage(driver);
            }
        }
        throw new IllegalArgumentException("Product not found: " + productName);
    }

    public void addProductToCart(String productName) {
        for (WebElement product : productItems) {
            WebElement name = product.findElement(PRODUCT_NAME_SELECTOR);
            if (name.getText().equals(productName)) {
                WebElement addButton = product.findElement(ADD_TO_CART_SELECTOR);
                waitUtility.waitForElementToBeClickable(addButton);
                addButton.click();
                return;
            }
        }
        throw new IllegalArgumentException("Product not found: " + productName);
    }

    public int getCartItemCount() {
        try {
            return Integer.parseInt(cartBadge.getText());
        } catch (Exception e) {
            return 0;
        }
    }

    public ShoppingCartPage clickOnCart() {
        waitUtility.waitForElementToBeClickable(cartIcon);
        cartIcon.click();
        return new ShoppingCartPage(driver);
    }

    public void openMenu() {
        waitUtility.waitForElementToBeClickable(menuButton);
        menuButton.click();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void closeMenu() {
        try {
            waitUtility.waitForElementToBeClickable(menuCloseButton);
            menuCloseButton.click();
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.invisibilityOf(sidebar));
        } catch (Exception e) {
            // Menu might already be closed
        }
    }

    public void clickAllItemsLink() {
        openMenu();
        waitUtility.waitForElementToBeClickable(allItemsLink);
        allItemsLink.click();
        closeMenu();
    }

    public void clickAboutLink() {
        openMenu();
        waitUtility.waitForElementToBeClickable(aboutLink);
        aboutLink.click();
    }

    public LoginPage clickLogoutLink() {
        openMenu();
        waitUtility.waitForElementToBeClickable(logoutLink);
        logoutLink.click();
        return new LoginPage(driver);
    }

    public void clickResetAppStateLink() {
        openMenu();
        waitUtility.waitForElementToBeClickable(resetAppStateLink);
        resetAppStateLink.click();
        closeMenu();
    }

    public boolean isProductInList(String productName) {
        return getProductNames().contains(productName);
    }
}
