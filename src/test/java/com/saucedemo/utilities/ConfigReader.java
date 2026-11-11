package com.saucedemo.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Utility class to read configuration properties from config.properties file
 *
 * This class provides a centralized way to access configuration values across the framework.
 * It loads the properties file once and provides methods to retrieve values.
 * Purpose: Avoid hard-coded values and allow easy configuration changes without code modifications.
 */
public class ConfigReader {
    // Static Properties object to store all configuration values
    private static Properties properties;

    static {
        // Static block - Executed once when the class is loaded
        // Load the properties file from the resources folder
        try {
            String configPath = "src/test/resources/config.properties";
            FileInputStream file = new FileInputStream(configPath);
            properties = new Properties();
            properties.load(file);
            file.close();
        } catch (IOException e) {
            System.out.println("Config file not found: " + e.getMessage());
        }
    }

    /**
     * Get a configuration value by key
     * @param key - The property key to retrieve
     * @return The value associated with the key, or null if not found
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get browser type from config
     * @return Browser type (chrome, firefox, edge, etc.)
     */
    public static String getBrowser() {
        return getProperty("browser");
    }

    /**
     * Get base URL from config
     * @return Application base URL
     */
    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    /**
     * Get implicit wait time from config
     * @return Wait time in seconds as integer
     */
    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait"));
    }

    /**
     * Get explicit wait time from config
     * @return Wait time in seconds as integer
     */
    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait"));
    }

    /**
     * Get valid username for positive test scenarios
     * @return Valid username
     */
    public static String getValidUsername() {
        return getProperty("valid.username");
    }

    /**
     * Get valid password for positive test scenarios
     * @return Valid password
     */
    public static String getValidPassword() {
        return getProperty("valid.password");
    }

    /**
     * Get invalid username for negative test scenarios
     * @return Invalid username
     */
    public static String getInvalidUsername() {
        return getProperty("invalid.username");
    }

    /**
     * Get invalid password for negative test scenarios
     * @return Invalid password
     */
    public static String getInvalidPassword() {
        return getProperty("invalid.password");
    }

    /**
     * Get locked-out user username
     * @return Locked-out username
     */
    public static String getLockedUsername() {
        return getProperty("locked.username");
    }

    /**
     * Get locked-out user password
     * @return Locked-out password
     */
    public static String getLockedPassword() {
        return getProperty("locked.password");
    }

    /**
     * Get screenshot storage path
     * @return Path where screenshots should be saved
     */
    public static String getScreenshotPath() {
        return getProperty("screenshot.path");
    }

    /**
     * Get report output directory path
     * @return Path for test reports
     */
    public static String getReportPath() {
        return getProperty("report.path");
    }

    /**
     * Get headless mode setting from config
     * @return true if headless mode enabled, false for headed mode
     */
    public static boolean isHeadlessMode() {
        // Maven -Dheadless.mode=true (used in CI) takes priority over config.properties
        String sysProp = System.getProperty("headless.mode");
        if (sysProp != null) {
            return Boolean.parseBoolean(sysProp);
        }
        return Boolean.parseBoolean(getProperty("headless.mode"));
    }
}
