# Quick Start Guide - 5 Minutes to Running Tests

Get up and running with the SauceDemo Selenium framework in minutes!

## Prerequisites Check

Before starting, verify you have:

```bash
# Check Java
java -version
# Should show: Java 17.x.x or later (LTS)

# Check Maven  
mvn --version
# Should show: Maven 3.6 or later
```

If either is missing, install from:
- **Java:** https://www.oracle.com/java/technologies/downloads/
- **Maven:** https://maven.apache.org/download.cgi

---

## 5-Minute Setup

### 1. Download Dependencies (2 minutes)

```bash
cd "C:\Users\abdulla.murshidkv\Desktop\myprojects\Selenium Capstone"
mvn clean install
```

This downloads all required libraries. You'll see lots of `[INFO]` messages - this is normal!

### 2. Verify Setup (1 minute)

```bash
# Run a quick test
mvn test -Dtest=LoginTests#testValidLogin
```

If you see `BUILD SUCCESS`, you're ready to go! ✅

### 3. Generate First Report (2 minutes)

```bash
# Run all positive tests
mvn test -Dgroups=Positive
```

Wait for it to complete, then open the report:

**On Windows:**
```bash
start test-results\reports\ExtentReport_*.html
```

**On Mac:**
```bash
open test-results/reports/ExtentReport_*.html
```

---

## Run Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test
```bash
# Single test class
mvn test -Dtest=LoginTests

# Single test method
mvn test -Dtest=LoginTests#testValidLogin
```

### Run Test Group
```bash
# Only positive tests
mvn test -Dgroups=Positive

# Only negative tests
mvn test -Dgroups=Negative

# End-to-end tests
mvn test -Dgroups="Positive" -Dtest=EndToEndTests
```

### Run on Different Browser
```bash
# Firefox
mvn test -Dbrowser=firefox

# Edge
mvn test -Dbrowser=edge
```

---

## Project Structure

```
Selenium Capstone/
│
├── src/test/java/com/saucedemo/
│   ├── base/           ← Base test setup
│   ├── pages/          ← Page objects (update locators here)
│   ├── tests/          ← Test classes (write tests here)
│   └── utilities/      ← Helper classes
│
├── src/test/resources/
│   ├── config.properties   ← Change URL, browser, wait times
│   └── log4j2.xml         ← Logging configuration
│
├── test-results/
│   ├── reports/        ← HTML reports (open in browser)
│   ├── screenshots/    ← Failure screenshots
│   └── logs/           ← Detailed logs
│
├── pom.xml             ← Dependencies (usually don't edit)
├── testng.xml          ← Test configuration (edit to organize tests)
└── README.md           ← Full documentation
```

---

## Key Files to Understand

### Pages (What tests interact with)
| File | Purpose |
|------|---------|
| `LoginPage.java` | Login page elements and methods |
| `ProductListingPage.java` | Products page |
| `ShoppingCartPage.java` | Cart page |
| `CheckoutPage.java` | Checkout pages |

### Tests (Test scenarios)
| File | Purpose |
|------|---------|
| `LoginTests.java` | Login functionality tests |
| `ProductTests.java` | Product browsing tests |
| `CartTests.java` | Shopping cart tests |
| `CheckoutTests.java` | Checkout process tests |
| `EndToEndTests.java` | Complete user journeys |

---

## First Time Customization

### Change Browser
Edit `src/test/resources/config.properties`:
```properties
browser=chrome  # Change to: firefox or edge
```

### Change Wait Times
Edit `src/test/resources/config.properties`:
```properties
explicit.wait=15  # Increase to 20 if tests timeout
```

### Change Test URL
Edit `src/test/resources/config.properties`:
```properties
base.url=https://www.saucedemo.com  # Change if testing different URL
```

---

## Common Commands

| Task | Command |
|------|---------|
| Run all tests | `mvn test` |
| Run single test | `mvn test -Dtest=LoginTests#testValidLogin` |
| Run on Firefox | `mvn test -Dbrowser=firefox` |
| Run with clean build | `mvn clean test` |
| View latest report | `start test-results\reports\ExtentReport_*.html` |

---

## Understanding Test Results

### Report Sections

1. **Dashboard** - Summary of all tests
   - Total tests run
   - Pass/Fail/Skip counts
   - Test duration

2. **Test Details** - Individual test results
   - Test name and status
   - Execution time
   - Log messages
   - Screenshots (if failed)

3. **Logs** - Detailed step-by-step logs
   - What each test did
   - Error messages
   - System information

### Interpreting Results

```
✅ PASS    - Test passed, feature works correctly
❌ FAIL    - Test failed, feature has issue
⏭️  SKIP    - Test was skipped/disabled
🖼️  Screenshot - Attached if test failed
```

---

## Troubleshooting

### "Tests taking too long"
Increase wait time in `config.properties`:
```properties
explicit.wait=20
```

### "Chrome driver not found"
The framework auto-downloads drivers. Ensure internet connection is active.

### "Can't find test results"
Results are in: `test-results/reports/`

### "Tests pass locally but fail in CI"
Check:
1. Browser compatibility
2. Network connectivity
3. Wait times (may need to increase)
4. Element locators (may have changed)

---

## Next Steps

### To Add More Tests
1. Open `src/test/java/com/saucedemo/tests/`
2. Create new test class or add to existing
3. Follow existing test patterns
4. Run: `mvn test -Dtest=YourTestClass`

### To Update Page Objects
1. Open `src/test/java/com/saucedemo/pages/`
2. Add/update locators if website changes
3. Add new methods for new page elements
4. Re-run tests

### For Detailed Guidance
See `FRAMEWORK_GUIDE.md` for comprehensive documentation

---

## Example: Writing Your First Test

1. Open `ProductTests.java`
2. Look at `testProductCount()` method - this is a simple example
3. Copy the pattern:
   ```java
   @Test(groups = "Positive", priority = 99)
   public void testYourFeature() {
       // Create report entry
       createTestReport("Your Feature", "What you're testing");
       
       // Do something
       // ...
       
       // Assert result
       // Assert.assertEquals(expected, actual, "error message");
       
       // Report result
       test.pass("Test completed successfully");
   }
   ```
4. Run: `mvn test -Dtest=ProductTests#testYourFeature`

---

## Test Data

### Login Credentials
| User | Username | Password |
|------|----------|----------|
| Valid | `standard_user` | `secret_sauce` |
| Locked | `locked_out_user` | `secret_sauce` |

All test data is in `config.properties` - update there if needed.

---

## Quick Reference - TestNG Annotations

```java
@BeforeSuite    // Runs once before all tests
@BeforeTest     // Runs before each test class
@BeforeMethod   // Runs before each test method (automatic from BaseTest)
@Test           // Marks method as a test
@AfterMethod    // Runs after each test method (automatic from BaseTest)
@AfterTest      // Runs after each test class
@AfterSuite     // Runs once after all tests
```

---

## Getting Help

### Check Documentation
- `README.md` - Full setup and usage guide
- `FRAMEWORK_GUIDE.md` - Architecture and best practices
- Code comments - Detailed explanation in code

### Check Logs
```
test-results/logs/test_execution.log
```

### Check Report
```
test-results/reports/ExtentReport_*.html
```

---

## You're Ready! 🚀

1. ✅ Setup complete
2. ✅ Tests run successfully  
3. ✅ Reports generate
4. ✅ You can write more tests

Start with:
```bash
mvn test
```

Then open the report and explore! 📊

---

**Questions?** Check `README.md` or `FRAMEWORK_GUIDE.md` for more details.

**Happy Testing!** 🎉
