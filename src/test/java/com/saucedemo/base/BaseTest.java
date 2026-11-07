package com.saucedemo.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.saucedemo.utilities.ConfigReader;
import com.saucedemo.utilities.ExtentReportsUtility;
import com.saucedemo.utilities.ScreenshotUtility;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

/**
 * BaseTest - Base class for all test classes in the framework
 *
 * This class contains common setup and teardown operations that all tests require.
 * All test classes should extend this class to inherit browser initialization,
 * driver management, and reporting functionality.
 *
 * Key features:
 * - Browser initialization before each test
 * - Browser cleanup after each test
 * - Screenshot capture on test failure
 * - Extent Reports integration
 * - Error logging and reporting
 */
public class BaseTest {

    // Protected so child classes can access the driver
    protected WebDriver driver;

    // Extent Reports objects for test reporting
    protected static ExtentReports extentReports;
    protected ExtentTest test;

    /**
     * BeforeSuite - Executes once before all tests in the suite
     * Initializes Extent Reports for the entire test run
     */
    @BeforeSuite
    public void beforeSuite() {
        System.out.println("========== Test Suite Started ==========");
        extentReports = ExtentReportsUtility.initializeExtentReports();
    }

    /**
     * BeforeMethod - Executes before each individual test
     * Initializes WebDriver, navigates to base URL, and creates test report entry
     */
    @BeforeMethod
    public void beforeMethod() {
        // Initialize WebDriver based on browser type from config
        initializeDriver();

        // Navigate to application base URL
        driver.get(ConfigReader.getBaseUrl());
        System.out.println("Application launched: " + ConfigReader.getBaseUrl());

        // Create test entry in Extent Reports
        // Note: Test name is set dynamically by TestNG based on method name
        // (This will be updated in individual test methods for proper naming)
    }

    /**
     * AfterMethod - Executes after each individual test
     * Captures screenshot on failure, logs results, and closes browser
     *
     * @param result - ITestResult object containing test execution results
     */
    @AfterMethod
    public void afterMethod(ITestResult result) {
        // Check if test failed
        if (result.getStatus() == ITestResult.FAILURE) {
            // Capture screenshot on failure
            String screenshotPath = ScreenshotUtility.captureScreenshot(driver, result.getName());

            // Log failure details
            if (test != null) {
                test.fail("Test Failed: " + result.getThrowable().getMessage());
                if (screenshotPath != null) {
                    // Add screenshot to report
                    test.addScreenCaptureFromPath(screenshotPath);
                }
            }
            System.out.println("TEST FAILED: " + result.getName());
        }
        // Check if test passed
        else if (result.getStatus() == ITestResult.SUCCESS) {
            if (test != null) {
                test.pass("Test Passed");
            }
            System.out.println("TEST PASSED: " + result.getName());
        }
        // Check if test was skipped
        else if (result.getStatus() == ITestResult.SKIP) {
            if (test != null) {
                test.skip("Test Skipped: " + result.getThrowable().getMessage());
            }
            System.out.println("TEST SKIPPED: " + result.getName());
        }

        // Close the browser after each test
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("Browser closed");
            } catch (Exception e) {
                // Quit timeout - try to close gracefully
                try {
                    driver.quit();
                } catch (Exception ex) {
                    System.out.println("Warning: Browser close timeout - " + ex.getMessage());
                }
            }
        }
    }

    /**
     * AfterSuite - Executes once after all tests in the suite
     * Finalizes Extent Reports generation
     */
    @AfterSuite
    public void afterSuite() {
        // Flush reports to generate final HTML report
        ExtentReportsUtility.flushExtentReports();
        System.out.println("========== Test Suite Completed ==========");
    }

    /**
     * Initialize WebDriver based on browser type from config.properties
     * Supports Chrome, Firefox, and Edge browsers
     * Automatically downloads appropriate WebDriver using WebDriverManager
     */
    private void initializeDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase();

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(createCleanChromeOptions());
                System.out.println("Chrome driver initialized with comprehensive popup suppression");
                break;

            case "firefox":
                // Use WebDriverManager to automatically download GeckoDriver
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                System.out.println("Firefox browser launched");
                break;

            case "edge":
                // Use WebDriverManager to automatically download EdgeDriver
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                System.out.println("Edge browser launched");
                break;

            default:
                System.out.println("Browser '" + browser + "' not recognized. Using Chrome by default.");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(createCleanChromeOptions());
                System.out.println("Chrome driver initialized with comprehensive popup suppression");
        }

        // Set implicit wait - applied globally to all element lookups
        driver.manage().timeouts().implicitlyWait(
                java.time.Duration.ofSeconds(ConfigReader.getImplicitWait())
        );

        // Maximize browser window for consistent UI rendering
        driver.manage().window().maximize();
        System.out.println("Browser window maximized");
    }

    /**
     * Create Chrome options with Guest Mode for complete popup suppression
     * Guest mode runs Chrome in a completely sandboxed profile with zero persistent data
     * This prevents all password manager popups and other user-data-dependent popups
     * @return Configured ChromeOptions instance
     */
    private ChromeOptions createCleanChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // Enable Guest Mode - Chrome runs with zero stored data/passwords/profile
        // This is the most reliable way to suppress all password manager popups
        options.addArguments("--guest");

        // Disable automation detection
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        // Additional popup suppression for safety (backup measures)
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.addArguments("--no-first-run");
        options.addArguments("--disable-extensions");

        // Add headless mode if enabled in config
        if (ConfigReader.isHeadlessMode()) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            System.out.println("Using HEADLESS mode (faster execution)");
        } else {
            System.out.println("Using HEADED mode (visual feedback)");
        }

        System.out.println("Guest Mode enabled - all password manager popups eliminated");
        return options;
    }

    /**
     * Create Extent Test entry for reporting
     * Should be called at the start of each test method
     * @param testName - Name of the test
     * @param description - Description of what the test does
     */
    protected void createTestReport(String testName, String description) {
        if (extentReports == null) {
            extentReports = ExtentReportsUtility.initializeExtentReports();
        }
        test = extentReports.createTest(testName, description);
        System.out.println("Test: " + testName + " - Started");
    }
}
