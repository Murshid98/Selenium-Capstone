package com.saucedemo.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ExtentReportsUtility - Utility class for Extent Reports HTML reporting
 *
 * This class initializes and manages Extent Reports for generating comprehensive test reports.
 * It creates detailed HTML reports with test results, screenshots, and statistics.
 * Purpose: Provide visual, detailed reporting of test execution for stakeholders and debugging.
 */
public class ExtentReportsUtility {

    // Static instances - used across the test suite
    private static ExtentReports extentReports;
    private static ExtentSparkReporter sparkReporter;

    /**
     * Initialize Extent Reports with configuration
     * Should be called once before all tests are executed
     * @return ExtentReports instance
     */
    public static ExtentReports initializeExtentReports() {
        // Create reports directory if it doesn't exist
        String reportPath = ConfigReader.getReportPath();
        File reportDir = new File(reportPath);
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }

        // Generate unique report filename with timestamp
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportFileName = reportPath + "ExtentReport_" + timestamp + ".html";

        // Create ExtentSparkReporter (new reporter for Extent Reports 5.x)
        sparkReporter = new ExtentSparkReporter(reportFileName);

        // Configure report settings
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("SauceDemo Test Report");
        sparkReporter.config().setReportName("Selenium Automation Test Report");

        // Create ExtentReports instance and attach reporter
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);

        // Add system information
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("Browser", ConfigReader.getBrowser());
        extentReports.setSystemInfo("Application URL", ConfigReader.getBaseUrl());

        System.out.println("Extent Reports initialized: " + reportFileName);
        return extentReports;
    }

    /**
     * Get the ExtentReports instance
     * @return ExtentReports object
     */
    public static ExtentReports getExtentReports() {
        if (extentReports == null) {
            initializeExtentReports();
        }
        return extentReports;
    }

    /**
     * Flush/close the Extent Reports - finalizes the report generation
     * Should be called after all tests are completed
     */
    public static void flushExtentReports() {
        if (extentReports != null) {
            extentReports.flush();
            System.out.println("Extent Reports generated successfully");
        }
    }
}
