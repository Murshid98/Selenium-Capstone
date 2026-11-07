# SauceDemo Selenium Automation Framework

A comprehensive test automation framework built with **Java**, **Selenium WebDriver**, **Maven**, and **TestNG** for automated testing of the [SauceDemo](https://www.saucedemo.com) e-commerce application.

## 📋 Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Modules](#test-modules)
- [Framework Features](#framework-features)
- [Test Reports](#test-reports)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Overview

This framework demonstrates professional-grade test automation practices using:

- **Page Object Model (POM)** - Separates UI elements from test logic
- **Explicit Waits** - Replaces hard sleeps with intelligent waiting
- **Configuration Management** - Externalized configuration for easy maintenance
- **Comprehensive Reporting** - Detailed HTML reports with screenshots
- **Well-Structured Architecture** - Scalable and maintainable codebase

### Application Under Test

**URL:** https://www.saucedemo.com

A purpose-built demo e-commerce application featuring:
- User authentication with multiple test users
- Product inventory and filtering
- Shopping cart functionality
- Checkout process
- Complete order flow

### Test Credentials

| User Type | Username | Password |
|-----------|----------|----------|
| Valid User | `standard_user` | `secret_sauce` |
| Invalid User | `invalid_user` | `wrong_password` |
| Locked User | `locked_out_user` | `secret_sauce` |

---

## ✅ Prerequisites

Before setting up the project, ensure you have:

### System Requirements
- **Java Development Kit (JDK):** Version 17 (LTS) or later
  - Set `JAVA_HOME` environment variable pointing to JDK 17
  - Verify: `java -version` (should show Java 17.x.x or later)

- **Apache Maven:** Version 3.6 or later
  - Download from: https://maven.apache.org/download.cgi
  - Verify: `mvn --version`

- **Git:** For version control
  - Download from: https://git-scm.com/

### IDE (Choose One)
- **IntelliJ IDEA Community Edition** (Recommended)
- **Eclipse IDE**
- **VS Code**

### Knowledge Requirements
- Basic Java programming and OOP concepts
- Understanding of Selenium WebDriver
- Familiarity with TestNG framework
- Basic HTML/CSS for element identification
- Maven project structure

### Java Version Info
- **Framework Java Version:** 17 (LTS)
- **Selenium Version:** 4.15.0 (Latest Selenium 4.x)
- **Compatibility:** Java 17+ with Selenium 4.0 and above

---

## 📂 Project Structure

```
Selenium Capstone/
│
├── src/test/java/
│   └── com/saucedemo/
│       ├── base/
│       │   └── BaseTest.java              # Base class for all tests
│       │
│       ├── pages/
│       │   ├── LoginPage.java             # Login page POM
│       │   ├── ProductListingPage.java    # Products listing page POM
│       │   ├── ProductDetailPage.java     # Product detail page POM
│       │   ├── ShoppingCartPage.java      # Shopping cart page POM
│       │   └── CheckoutPage.java          # Checkout process page POM
│       │
│       ├── tests/
│       │   ├── LoginTests.java            # Login functionality tests
│       │   ├── ProductTests.java          # Product browsing tests
│       │   ├── CartTests.java             # Shopping cart tests
│       │   ├── CheckoutTests.java         # Checkout process tests
│       │   ├── NavigationTests.java       # Navigation & menu tests
│       │   └── EndToEndTests.java         # End-to-end scenarios
│       │
│       └── utilities/
│           ├── ConfigReader.java          # Configuration management
│           ├── WaitUtility.java           # Explicit wait helpers
│           ├── ScreenshotUtility.java     # Screenshot capture
│           └── ExtentReportsUtility.java  # HTML reporting
│
├── src/test/resources/
│   └── config.properties                  # Configuration file
│
├── test-results/
│   ├── screenshots/                       # Screenshots on failure
│   ├── reports/                           # HTML test reports
│   └── logs/                              # Test execution logs
│
├── pom.xml                                # Maven dependencies
├── testng.xml                             # TestNG suite configuration
└── README.md                              # This file
```

---

## 🚀 Setup Instructions

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd "Selenium Capstone"
```

### Step 2: Verify Prerequisites

```bash
# Check Java version
java -version
# Expected: Java 11 or later

# Check Maven version
mvn --version
# Expected: Maven 3.6 or later
```

### Step 3: Download Dependencies

```bash
# Navigate to project directory
cd "Selenium Capstone"

# Download all Maven dependencies
mvn clean install
```

This command will:
- Clean previous builds
- Download all required JAR files (Selenium, TestNG, etc.)
- Compile the project

### Step 4: Verify Setup

```bash
# Run a quick validation
mvn test -Dtest=LoginTests#testValidLogin
```

If this runs successfully, your setup is complete!

---

## ⚙️ Configuration

### Configuration File: `config.properties`

Located at: `src/test/resources/config.properties`

Key configuration options:

```properties
# Browser type: chrome, firefox, edge
browser=chrome

# Application URL
base.url=https://www.saucedemo.com

# Explicit wait timeout in seconds
explicit.wait=15

# Test user credentials
valid.username=standard_user
valid.password=secret_sauce

# Report and screenshot paths
screenshot.path=./test-results/screenshots/
report.path=./test-results/reports/
```

### Modifying Configuration

To change any configuration:

1. Open `src/test/resources/config.properties`
2. Update the desired values
3. Save the file
4. Run tests (no recompilation needed)

### Browser Selection

To run tests with a different browser:

**Edit `config.properties`:**
```properties
# For Chrome
browser=chrome

# For Firefox
browser=firefox

# For Edge
browser=edge
```

---

## 🧪 Running Tests

### Run All Tests

```bash
# Run all tests in the project
mvn test
```

### Run Specific Test Class

```bash
# Run only Login tests
mvn test -Dtest=LoginTests

# Run only Product tests
mvn test -Dtest=ProductTests
```

### Run Specific Test Method

```bash
# Run specific test method
mvn test -Dtest=LoginTests#testValidLogin

# Run multiple specific methods
mvn test -Dtest=LoginTests#testValidLogin,LoginTests#testInvalidPassword
```

### Run Specific Test Group

```bash
# Run only positive tests
mvn test -Dgroups=Positive

# Run only negative tests
mvn test -Dgroups=Negative

# Run end-to-end tests
mvn test -Dgroups=EndToEnd
```

### Run with TestNG Suite

```bash
# Run using testng.xml configuration
mvn test -DsuiteXmlFile=testng.xml
```

### Run Tests with Different Browser

```bash
# Run tests on Firefox
mvn test -Dbrowser=firefox

# Run tests on Edge
mvn test -Dbrowser=edge
```

---

## 📊 Test Modules

### 1. **Login Module** (`LoginTests.java`)

Tests authentication and login functionality:

- ✅ **Positive:**
  - Valid login with correct credentials
  - Login → Logout → Back button flow

- ❌ **Negative:**
  - Invalid password error handling
  - Empty fields validation
  - Locked-out user prevention
  - Missing field validation

### 2. **Product Listing Module** (`ProductTests.java`)

Tests product browsing and filtering:

- ✅ **Positive:**
  - Product count verification
  - Sort by name (A→Z, Z→A)
  - Sort by price (low→high, high→low)
  - Product detail page navigation
  - Data consistency across pages
  - Add to cart from detail page

### 3. **Shopping Cart Module** (`CartTests.java`)

Tests cart functionality:

- ✅ **Positive:**
  - Add single item to cart
  - Add multiple items to cart
  - Remove item from cart
  - Verify item details in cart
  - Continue shopping functionality
  - Empty cart verification
  - Remove all items

### 4. **Checkout Module** (`CheckoutTests.java`)

Tests checkout process:

- ✅ **Positive:**
  - Complete checkout flow
  - Order summary verification
  - Price calculations
  - Order confirmation

- ❌ **Negative:**
  - Empty form validation
  - Missing field validation
  - Required field checks

### 5. **Navigation Module** (`NavigationTests.java`)

Tests navigation and menu functionality:

- ✅ **Positive:**
  - Sidebar menu open/close
  - All Items navigation
  - About link navigation
  - Logout link functionality
  - Reset App State
  - Menu accessibility

### 6. **End-to-End Module** (`EndToEndTests.java`)

Complete user journey scenarios:

- ✅ **Happy Path:**
  - Complete flow: Login → Browse → Add Items → Checkout → Confirmation
  - Single item checkout
  - Checkout via product details

---

## 🎨 Framework Features

### 1. **Page Object Model (POM)**

Each page has a dedicated class:
```
LoginPage.java          - Login page elements and methods
ProductListingPage.java - Products page elements and methods
ShoppingCartPage.java   - Cart page elements and methods
```

**Benefits:**
- Locators centralized in one place
- Easy to maintain and update
- Tests remain clean and readable

### 2. **Explicit Waits**

Uses `WebDriverWait` instead of `Thread.sleep()`:

```java
// ❌ NOT RECOMMENDED
Thread.sleep(5000);  // Hard sleep

// ✅ RECOMMENDED
waitUtility.waitForElementToBeClickable(element);  // Intelligent wait
```

**Benefits:**
- Faster test execution
- More reliable on slow connections
- Reduces flakiness

### 3. **Configuration Management**

All hardcoded values externalized:

```properties
# config.properties
base.url=https://www.saucedemo.com
implicit.wait=10
explicit.wait=15
```

Access in code:
```java
String url = ConfigReader.getBaseUrl();
```

**Benefits:**
- No code changes needed for environment switches
- Easy configuration updates
- Version control friendly

### 4. **Comprehensive Logging**

Tests include detailed logging:

```java
test.info("User logged in successfully");
test.pass("Login functionality verified");
test.fail("Login failed unexpectedly");
```

### 5. **Screenshot on Failure**

Automatic screenshot capture:

```java
// Automatically captured on test failure
// Saved to: ./test-results/screenshots/
// Filename: TestName_timestamp.png
```

### 6. **HTML Reports**

Extent Reports generate beautiful HTML reports:

- Test execution summary
- Pass/Fail/Skip statistics
- Detailed test logs
- Screenshots of failures
- System information

---

## 📈 Test Reports

### Generate Reports

Reports are automatically generated after test execution in:

```
./test-results/reports/ExtentReport_<timestamp>.html
```

### View Reports

```bash
# Windows
start test-results\reports\ExtentReport_*.html

# Mac
open test-results/reports/ExtentReport_*.html

# Linux
xdg-open test-results/reports/ExtentReport_*.html
```

### Report Contents

- **Dashboard:** Summary of all tests
- **Test Details:** Individual test results
- **Logs:** Detailed execution logs
- **Screenshots:** Failure screenshots
- **System Info:** Environment details

---

## 🐛 Troubleshooting

### Common Issues and Solutions

#### Issue 1: "JAVA_HOME is not set"

```bash
# Windows
setx JAVA_HOME "C:\Program Files\Java\jdk-11"

# Mac/Linux
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.jdk/Contents/Home
```

#### Issue 2: "Maven command not found"

```bash
# Verify Maven installation
mvn --version

# If not found, add to system PATH
# Windows: Add Maven\bin to PATH
# Mac/Linux: export PATH=$PATH:/path/to/maven/bin
```

#### Issue 3: "Chrome driver not found"

The framework uses WebDriverManager for automatic driver management. Ensure:

```bash
# Test that WebDriverManager is working
mvn test -Dtest=LoginTests#testValidLogin
```

If still failing:
```bash
# Clear driver cache
rm -rf ~/.wdm  # Mac/Linux
del %USERPROFILE%\.wdm  # Windows
```

#### Issue 4: "Tests timing out"

Increase wait times in `config.properties`:

```properties
explicit.wait=30  # Increase from default 15
page.load.timeout=30
```

#### Issue 5: "Element not found" errors

- Check if element locators have changed (website updates)
- Update locators in respective page classes
- Verify element exists in browser DevTools (F12)

---

## 💡 Best Practices

### Writing New Tests

1. **Follow POM Pattern**
   ```java
   // ✅ Correct
   ProductListingPage page = new ProductListingPage(driver);
   page.addProductToCart("Product Name");
   
   // ❌ Avoid
   driver.findElement(By.xpath("...")).click();
   ```

2. **Use Meaningful Assertions**
   ```java
   // ✅ Clear
   Assert.assertTrue(page.isProductsPageLoaded(), 
       "Products page should load after login");
   
   // ❌ Unclear
   Assert.assertTrue(true);
   ```

3. **Add Descriptive Comments**
   ```java
   // ✅ Good
   // Verify user can add product to cart from detail page
   
   // ❌ Poor
   // Add product
   ```

4. **Handle Exceptions Properly**
   ```java
   // ✅ Correct
   try {
       // Test logic
   } catch (TimeoutException e) {
       test.fail("Element not found: " + e.getMessage());
   }
   ```

---

## 📚 Additional Resources

- **Selenium Documentation:** https://www.selenium.dev/documentation/
- **TestNG Guide:** https://testng.org/doc/
- **Maven Documentation:** https://maven.apache.org/guides/
- **Extent Reports:** https://www.extentreports.com/

---

## ✍️ Author Information

**Framework Version:** 1.0.0

**Last Updated:** November 2024

This framework is designed for learning and professional automation testing practices.

---

## 📝 License

This project is provided as-is for educational and professional use.

---

## 🤝 Contributing

To contribute improvements:

1. Create a new branch
2. Make your changes
3. Add appropriate tests
4. Submit a pull request

---

## ❓ Support

For issues or questions:

1. Check the **Troubleshooting** section
2. Review test logs in `./test-results/logs/`
3. Check Extent Report for detailed failure info
4. Review browser console (F12) for errors

---

**Happy Testing! 🚀**

Remember: **Quality automation is about writing maintainable, reliable, and understandable code.**
