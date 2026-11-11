package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductListingPage;
import com.saucedemo.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestWorkFlow extends BaseTest {

    @Test
    public void testLoginAndClose() {
        createTestReport("Workflow Test - Login and Close!",
                "Verify login works and browser closes cleanly");

        LoginPage loginPage = new LoginPage(driver);

        Assert.assertTrue(loginPage.isLoginPageLoaded(),
                "Login page should be displayed");

        ProductListingPage productsPage = loginPage.login(
                ConfigReader.getValidUsername(),
                ConfigReader.getValidPassword()
        );

        Assert.assertTrue(productsPage.isProductsPageLoaded(),
                "Products page should be displayed after login");

        // Browser is closed automatically by @AfterMethod in BaseTest
        test.pass("Login successful and browser will be closed by teardown");
    }
}
