# Framework Architecture & Usage Guide

## Table of Contents

1. [Framework Architecture](#framework-architecture)
2. [Design Patterns Used](#design-patterns-used)
3. [How to Add New Tests](#how-to-add-new-tests)
4. [How to Add New Pages](#how-to-add-new-pages)
5. [Understanding the Code](#understanding-the-code)
6. [Best Practices](#best-practices)

---

## Framework Architecture

### High-Level Overview

```
┌─────────────────────────────────────────────────────────┐
│                    TEST CLASSES                         │
│  (LoginTests, ProductTests, CartTests, etc.)            │
├─────────────────────────────────────────────────────────┤
│                    BASE TEST CLASS                      │
│   (Browser setup, teardown, reporting)                  │
├─────────────────────────────────────────────────────────┤
│                   PAGE OBJECT CLASSES                   │
│  (LoginPage, ProductPage, CartPage, etc.)               │
├─────────────────────────────────────────────────────────┤
│                   UTILITY CLASSES                       │
│ (ConfigReader, WaitUtility, ScreenshotUtility, etc.)    │
├─────────────────────────────────────────────────────────┤
│                 SELENIUM WEBDRIVER                      │
│              (Browser interactions)                     │
└─────────────────────────────────────────────────────────┘
```

### Component Responsibilities

| Component | Responsibility |
|-----------|-----------------|
| **Test Classes** | Contain test logic and assertions |
| **Base Test Class** | Handles driver initialization and cleanup |
| **Page Classes** | Encapsulate page elements and interactions |
| **Utilities** | Provide reusable functionality |
| **Configuration** | Store externalized settings |

---

## Design Patterns Used

### 1. Page Object Model (POM)

**Purpose:** Separate UI element locators from test logic

**Structure:**
```
Each page has:
├── Locators (By objects for each element)
├── Constructor (initialize WebDriver)
└── Methods (interactions with elements)
```

**Example:**
```java
public class LoginPage {
    // Locators
    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    
    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    
    // Methods
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }
}
```

**Benefits:**
- Locators changed in one place
- Tests remain clean and readable
- Easy to maintain

### 2. Base Test Class Pattern

**Purpose:** Centralize common setup/teardown operations

**Includes:**
- Driver initialization
- Navigation to base URL
- Test reporting
- Screenshot capture
- Browser cleanup

```java
public class BaseTest {
    protected WebDriver driver;
    
    @BeforeMethod
    public void beforeMethod() {
        // Initialize driver
        // Navigate to URL
        // Create test report entry
    }
    
    @AfterMethod
    public void afterMethod(ITestResult result) {
        // Take screenshot on failure
        // Close browser
    }
}
```

**Benefits:**
- Eliminates code duplication
- Consistent setup across all tests
- Easy to modify common operations

### 3. Utility Classes Pattern

**Purpose:** Provide reusable helper methods

**Common Utilities:**
```
ConfigReader     → Read configuration values
WaitUtility      → Explicit wait operations
ScreenshotUtility → Capture screenshots
ExtentReportsUtility → Report generation
```

**Example:**
```java
// Instead of repeating wait code
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
wait.until(ExpectedConditions.visibilityOf(element));

// Use utility
WaitUtility waitUtil = new WaitUtility(driver);
waitUtil.waitForElementToBeVisible(element);
```

---

## How to Add New Tests

### Step 1: Identify the Page

Determine which page(s) your test will interact with.

```
Example: Testing "Add to Cart" button
Pages involved: ProductListingPage, ShoppingCartPage
```

### Step 2: Create Test Class (if needed)

If testing a new module, create a new test class:

```java
package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.YourPage;
import org.testng.annotations.Test;
import org.testng.Assert;

public class YourFeatureTests extends BaseTest {
    
    @Test(groups = "Positive")
    public void testYourFeature() {
        // Test implementation
    }
}
```

### Step 3: Write Test Method

Structure:
```
1. Setup (create page object)
2. Action (interact with page)
3. Assert (verify expected result)
4. Report (log results)
```

**Example:**
```java
@Test(groups = "Positive", priority = 1)
public void testAddProductToCart() {
    // Create report entry
    createTestReport("Add Product to Cart", 
            "Verify product can be added to cart");
    
    // Step 1: Setup
    LoginPage loginPage = new LoginPage(driver);
    ProductListingPage productsPage = loginPage.login(
            ConfigReader.getValidUsername(), 
            ConfigReader.getValidPassword()
    );
    
    // Step 2: Action
    productsPage.addProductToCart("Product Name");
    int cartCount = productsPage.getCartItemCount();
    
    // Step 3: Assert
    Assert.assertEquals(cartCount, 1, 
            "Cart should contain 1 item");
    
    // Step 4: Report
    test.pass("Product successfully added to cart");
}
```

### Step 4: Add to testng.xml

Update `testng.xml` to include your new test class:

```xml
<test name="Your Test Suite">
    <classes>
        <class name="com.saucedemo.tests.YourFeatureTests">
            <methods>
                <include name="testYourFeature" />
            </methods>
        </class>
    </classes>
</test>
```

### Step 5: Run and Verify

```bash
mvn test -Dtest=YourFeatureTests
```

---

## How to Add New Pages

### Step 1: Create Page Class

Create new file in `src/test/java/com/saucedemo/pages/`:

```java
package com.saucedemo.pages;

import com.saucedemo.utilities.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class YourNewPage {
    // WebDriver instance
    private WebDriver driver;
    private WaitUtility waitUtility;
    
    // Locators
    private By element1 = By.id("element-id");
    private By element2 = By.className("element-class");
    
    // Constructor
    public YourNewPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtility = new WaitUtility(driver);
    }
    
    // Methods
    public void interactWithElement() {
        WebElement elem = driver.findElement(element1);
        waitUtility.waitForElementToBeClickable(elem);
        elem.click();
    }
}
```

### Step 2: Add Locators

Identify all UI elements and add their locators:

```java
// Navigation elements
private By header = By.className("page-header");
private By backButton = By.id("back-btn");

// Form elements
private By inputField = By.xpath("//input[@name='fieldName']");
private By submitButton = By.css(".submit-btn");

// Error messages
private By errorMessage = By.xpath("//div[@class='error']");
```

### Step 3: Add Helper Methods

Create methods for common interactions:

```java
// Verification methods
public boolean isPageLoaded() {
    try {
        return driver.findElement(header).isDisplayed();
    } catch (Exception e) {
        return false;
    }
}

// Interaction methods
public void fillForm(String fieldName, String value) {
    WebElement field = driver.findElement(inputField);
    waitUtility.waitForElementToBeVisible(field);
    field.clear();
    field.sendKeys(value);
}

// Navigation methods
public void clickSubmit() {
    WebElement btn = driver.findElement(submitButton);
    waitUtility.waitForElementToBeClickable(btn);
    btn.click();
}
```

### Step 4: Use in Tests

```java
public void testYourFeature() {
    YourNewPage page = new YourNewPage(driver);
    
    // Use page methods
    Assert.assertTrue(page.isPageLoaded());
    page.fillForm("username", "testuser");
    page.clickSubmit();
}
```

---

## Understanding the Code

### Reading a Test

```java
@Test(groups = "Positive", priority = 1)
public void testAddSingleItemToCart() {
    // 1. Report entry - logs test in HTML report
    createTestReport("Add Single Item to Cart", 
            "Verify adding one product to cart works");

    // 2. Get test data
    List<String> productNames = productsPage.getProductNames();
    String selectedProduct = productNames.get(0);

    // 3. Perform action
    productsPage.addProductToCart(selectedProduct);

    // 4. Verify result
    int cartCount = productsPage.getCartItemCount();
    Assert.assertEquals(cartCount, 1, 
            "Cart should contain 1 item");

    // 5. Log result
    test.pass("Item successfully added to cart");
}
```

**Key Points:**
- `@Test` annotation marks this as a test method
- `groups = "Positive"` categorizes the test
- `priority` controls execution order
- Assertions verify expected behavior
- `test.pass/fail/info` logs to report

### Reading a Page Object

```java
public class LoginPage {
    // Instance variables
    private WebDriver driver;
    private WaitUtility waitUtility;

    // Locators - By objects for element identification
    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");

    // Constructor - Initializes page with driver
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtility = new WaitUtility(driver);
    }

    // Public methods - What tests can do
    public void enterUsername(String username) {
        WebElement field = driver.findElement(usernameField);
        waitUtility.waitForElementToBeVisible(field);
        field.clear();
        field.sendKeys(username);
    }

    // Private helper methods (if needed)
    private void clearField(WebElement element) {
        element.clear();
    }
}
```

**Key Points:**
- Locators are private (only page knows about them)
- Methods are public (tests call these)
- WaitUtility ensures elements are ready
- No test logic in page objects

---

## Best Practices

### ✅ DO

1. **Use Page Objects for All Interactions**
   ```java
   // Good
   ProductListingPage page = new ProductListingPage(driver);
   page.addProductToCart("Product");
   ```

2. **Use Explicit Waits**
   ```java
   // Good
   waitUtility.waitForElementToBeClickable(element);
   
   // Avoid
   Thread.sleep(5000);
   ```

3. **Use Meaningful Assertions**
   ```java
   // Good
   Assert.assertEquals(cartCount, 1, 
       "Cart should contain exactly 1 item");
   
   // Avoid
   Assert.assertTrue(true);
   ```

4. **Externalize Configuration**
   ```java
   // Good
   String url = ConfigReader.getBaseUrl();
   
   // Avoid
   String url = "https://www.saucedemo.com";
   ```

5. **Add Descriptive Comments**
   ```java
   // Good - Explains WHY
   // Wait for async load to complete before checking count
   
   // Avoid - States WHAT (code already does this)
   // Wait for element
   ```

### ❌ DON'T

1. **Hardcode Locators in Tests**
   ```java
   // Bad
   driver.findElement(By.xpath("//button[@id='submit']")).click();
   
   // Good
   loginPage.clickLoginButton();
   ```

2. **Use Hard Sleeps**
   ```java
   // Bad
   Thread.sleep(10000);
   
   // Good
   waitUtility.waitForElementToBeVisible(element);
   ```

3. **Hardcode Test Data**
   ```java
   // Bad
   loginPage.login("standard_user", "secret_sauce");
   
   // Good
   loginPage.login(ConfigReader.getValidUsername(),
                   ConfigReader.getValidPassword());
   ```

4. **Ignore Test Failures**
   ```java
   // Bad
   try {
       // test logic
   } catch (Exception e) {
       // Silently fail
   }
   
   // Good
   try {
       // test logic
   } catch (TimeoutException e) {
       test.fail("Element not found: " + e.getMessage());
       throw e;
   }
   ```

5. **Create Duplicate Methods**
   ```java
   // Bad - Duplicate code
   public void method1() { /* code */ }
   public void method2() { /* same code */ }
   
   // Good - Reuse common code
   private void commonLogic() { /* code */ }
   public void method1() { commonLogic(); }
   public void method2() { commonLogic(); }
   ```

---

## Troubleshooting Guide

### Test Fails with "Element not found"

**Cause:** Locator is incorrect or element takes time to load

**Solution:**
```java
// Add explicit wait
waitUtility.waitForElementToBeVisible(element);

// Or verify locator in browser DevTools (F12)
```

### Test Hangs/Times Out

**Cause:** Waiting for element that doesn't appear

**Solution:**
```java
// Check element visibility in browser
// Verify network requests complete
// Increase wait timeout in config.properties
explicit.wait=20
```

### Report Not Generated

**Cause:** Missing report directory or ExtentReports not initialized

**Solution:**
```bash
# Ensure directory exists
mkdir -p test-results/reports

# Verify ExtentReports initialization in BaseTest
```

### Screenshot Not Captured

**Cause:** Test didn't fail (screenshots only on failure)

**Solution:**
```java
// Add screenshot manually if needed
ScreenshotUtility.captureScreenshot(driver, "testName");
```

---

## Quick Reference

### Common Methods

| Task | Code |
|------|------|
| Wait for element visible | `waitUtility.waitForElementToBeVisible(element)` |
| Wait for element clickable | `waitUtility.waitForElementToBeClickable(element)` |
| Get configuration value | `ConfigReader.getBaseUrl()` |
| Create test report | `createTestReport("Title", "Description")` |
| Log to report | `test.info("Message")` or `test.pass("Message")` |

### File Locations

| Item | Location |
|------|----------|
| Page Objects | `src/test/java/com/saucedemo/pages/` |
| Tests | `src/test/java/com/saucedemo/tests/` |
| Utilities | `src/test/java/com/saucedemo/utilities/` |
| Configuration | `src/test/resources/config.properties` |
| Test Reports | `test-results/reports/` |
| Screenshots | `test-results/screenshots/` |

---

**Remember:** Good automation code is clean, maintainable, and easy to understand for the next person who reads it.

Happy Coding! 🎉
