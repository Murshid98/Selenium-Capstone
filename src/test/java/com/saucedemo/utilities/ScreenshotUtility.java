package com.saucedemo.utilities;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtility - Utility class for capturing screenshots on test failure
 *
 * This class provides functionality to capture screenshots with timestamp.
 * Screenshots are saved with test name and timestamp to avoid file conflicts.
 * Purpose: Provide visual evidence of test failures for debugging and reporting.
 */
public class ScreenshotUtility {

    /**
     * Capture screenshot with test name and timestamp
     * @param driver - WebDriver instance
     * @param testName - Name of the test for filename
     * @return Path to the saved screenshot file
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            // Create screenshots directory if it doesn't exist
            String screenshotPath = ConfigReader.getScreenshotPath();
            File screenshotDir = new File(screenshotPath);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate timestamp for unique filename
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = screenshotPath + fileName;

            // Capture screenshot using Selenium's TakesScreenshot interface
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);

            // Copy screenshot to destination
            Files.copy(srcFile.toPath(), Paths.get(filePath));

            System.out.println("Screenshot captured: " + filePath);
            return filePath;

        } catch (IOException e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get screenshot as Base64 string for Extent Reports
     * This allows embedding screenshots directly in HTML reports without external files
     * @param driver - WebDriver instance
     * @return Screenshot in Base64 format
     */
    public static String getScreenshotAsBase64(WebDriver driver) {
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        return screenshot.getScreenshotAs(OutputType.BASE64);
    }
}
