"""
Generate SauceDemo Test Cases Excel Sheet
Produces: docs/SauceDemo_Test_Cases.xlsx
"""

from openpyxl import Workbook
from openpyxl.styles import (Font, PatternFill, Alignment, Border, Side,
                              GradientFill)
from openpyxl.utils import get_column_letter

wb = Workbook()
ws = wb.active
ws.title = "Test Cases"

# ── Colour palette ──────────────────────────────────────────────────────────
HEADER_BG   = "1F3864"   # dark navy
HEADER_FG   = "FFFFFF"
POS_BG      = "E2EFDA"   # light green
NEG_BG      = "FCE4D6"   # light red/orange
E2E_BG      = "DDEBF7"   # light blue
ALT_BG      = "F2F2F2"   # light grey (alternating rows)
WHITE       = "FFFFFF"
MOD_COLORS  = {
    "Login":       "D6E4F0",
    "Products":    "D5F5E3",
    "Cart":        "FEF9E7",
    "Checkout":    "FDEDEC",
    "Navigation":  "F4ECF7",
    "End-to-End":  "E8DAEF",
}
PRIORITY_COLORS = {
    "Critical": "FF0000",
    "High":     "FF6600",
    "Medium":   "FFC000",
    "Low":      "92D050",
}

def fill(hex_color):
    return PatternFill("solid", fgColor=hex_color)

def thin_border():
    s = Side(style="thin", color="BFBFBF")
    return Border(left=s, right=s, top=s, bottom=s)

def center(wrap=True):
    return Alignment(horizontal="center", vertical="center", wrap_text=wrap)

def left(wrap=True):
    return Alignment(horizontal="left", vertical="center", wrap_text=wrap)

# ── Column definitions ───────────────────────────────────────────────────────
COLUMNS = [
    ("Test ID",        12),
    ("Module",         13),
    ("Test Class",     22),
    ("Test Method",    35),
    ("Test Title",     32),
    ("Test Type",      12),
    ("Priority",       10),
    ("Pre-conditions", 35),
    ("Test Steps",     55),
    ("Expected Result",40),
    ("Assertions",     45),
    ("Status",         12),
]

# ── Write header ─────────────────────────────────────────────────────────────
for col_idx, (col_name, col_width) in enumerate(COLUMNS, start=1):
    cell = ws.cell(row=1, column=col_idx, value=col_name)
    cell.font      = Font(bold=True, color=HEADER_FG, name="Calibri", size=11)
    cell.fill      = fill(HEADER_BG)
    cell.alignment = center()
    cell.border    = thin_border()
    ws.column_dimensions[get_column_letter(col_idx)].width = col_width

ws.row_dimensions[1].height = 30
ws.freeze_panes = "A2"

# ── Test case data ────────────────────────────────────────────────────────────
test_cases = [

    # ──────────────── LOGIN TESTS ────────────────
    {
        "id":    "TC_001",
        "module":"Login",
        "class": "LoginTests",
        "method":"testValidLogin",
        "title": "Valid Login",
        "type":  "Positive",
        "priority": "High",
        "pre":   "Application is open on login page (https://www.saucedemo.com)",
        "steps": "1. Open SauceDemo login page\n2. Enter valid username (standard_user)\n3. Enter valid password (secret_sauce)\n4. Click the Login button",
        "expected": "User is successfully redirected to the Products listing page",
        "assertions": "productsPage.isProductsPageLoaded() returns true",
    },
    {
        "id":    "TC_002",
        "module":"Login",
        "class": "LoginTests",
        "method":"testInvalidPassword",
        "title": "Invalid Password Login",
        "type":  "Negative",
        "priority": "High",
        "pre":   "Application is open on login page",
        "steps": "1. Open login page\n2. Enter valid username (standard_user)\n3. Enter invalid password (wrong_pass)\n4. Click the Login button",
        "expected": "Login fails; error message is displayed; user remains on login page",
        "assertions": "isErrorMessageDisplayed() = true\ngetErrorMessage() contains 'Username and password do not match'",
    },
    {
        "id":    "TC_003",
        "module":"Login",
        "class": "LoginTests",
        "method":"testEmptyFieldsLogin",
        "title": "Empty Fields Login",
        "type":  "Negative",
        "priority": "High",
        "pre":   "Application is open on login page",
        "steps": "1. Open login page\n2. Leave username and password fields empty\n3. Click the Login button",
        "expected": "Validation error is displayed; user remains on login page",
        "assertions": "isErrorMessageDisplayed() = true\ngetErrorMessage() contains 'Username is required'",
    },
    {
        "id":    "TC_004",
        "module":"Login",
        "class": "LoginTests",
        "method":"testLockedOutUserLogin",
        "title": "Locked Out User Login",
        "type":  "Negative",
        "priority": "High",
        "pre":   "Application is open on login page",
        "steps": "1. Open login page\n2. Enter locked-out username (locked_out_user)\n3. Enter password (secret_sauce)\n4. Click the Login button",
        "expected": "Error message is displayed preventing access for locked account",
        "assertions": "isErrorMessageDisplayed() = true\ngetErrorMessage() contains 'locked out'",
    },
    {
        "id":    "TC_005",
        "module":"Login",
        "class": "LoginTests",
        "method":"testLoginLogoutBackButton",
        "title": "Login → Logout → Back Button Security",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "Application is open on login page",
        "steps": "1. Login with valid credentials\n2. Verify products page is displayed\n3. Click Logout from sidebar menu\n4. Verify return to login page\n5. Click browser Back button",
        "expected": "Browser back button does not bypass the login screen; user stays on login page",
        "assertions": "After logout: isLoginPageLoaded() = true\nAfter back: isLoginPageLoaded() = true",
    },
    {
        "id":    "TC_006",
        "module":"Login",
        "class": "LoginTests",
        "method":"testOnlyUsernameFilledLogin",
        "title": "Only Username Filled",
        "type":  "Negative",
        "priority": "Medium",
        "pre":   "Application is open on login page",
        "steps": "1. Open login page\n2. Enter only the username (standard_user)\n3. Leave password field empty\n4. Click the Login button",
        "expected": "Validation error displayed for missing password",
        "assertions": "isErrorMessageDisplayed() = true",
    },
    {
        "id":    "TC_007",
        "module":"Login",
        "class": "LoginTests",
        "method":"testOnlyPasswordFilledLogin",
        "title": "Only Password Filled",
        "type":  "Negative",
        "priority": "Medium",
        "pre":   "Application is open on login page",
        "steps": "1. Open login page\n2. Leave username field empty\n3. Enter only the password (secret_sauce)\n4. Click the Login button",
        "expected": "Validation error displayed for missing username",
        "assertions": "isErrorMessageDisplayed() = true\ngetErrorMessage() contains 'Username is required'",
    },

    # ──────────────── PRODUCT TESTS ────────────────
    {
        "id":    "TC_008",
        "module":"Products",
        "class": "ProductTests",
        "method":"testProductCount",
        "title": "Product Count Verification",
        "type":  "Positive",
        "priority": "High",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login with valid credentials\n2. Count all visible product items on the inventory page",
        "expected": "Exactly 6 products are displayed on the listing page",
        "assertions": "getProductCount() == 6",
    },
    {
        "id":    "TC_009",
        "module":"Products",
        "class": "ProductTests",
        "method":"testSortByNameAtoZ",
        "title": "Sort Products by Name (A to Z)",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Select 'Name (A to Z)' from the sort dropdown\n3. Read the first and last product names",
        "expected": "Products are displayed in ascending alphabetical order (A→Z)",
        "assertions": "firstProduct.compareTo(lastProduct) <= 0",
    },
    {
        "id":    "TC_010",
        "module":"Products",
        "class": "ProductTests",
        "method":"testSortByNameZtoA",
        "title": "Sort Products by Name (Z to A)",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Select 'Name (Z to A)' from the sort dropdown\n3. Read the first and last product names",
        "expected": "Products are displayed in descending alphabetical order (Z→A)",
        "assertions": "firstProduct.compareTo(lastProduct) >= 0",
    },
    {
        "id":    "TC_011",
        "module":"Products",
        "class": "ProductTests",
        "method":"testSortByPriceLowToHigh",
        "title": "Sort Products by Price (Low to High)",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Select 'Price (low to high)' from the sort dropdown\n3. Read all product prices",
        "expected": "Products are displayed from lowest to highest price",
        "assertions": "For every consecutive pair: priceValues[i] <= priceValues[i+1]",
    },
    {
        "id":    "TC_012",
        "module":"Products",
        "class": "ProductTests",
        "method":"testSortByPriceHighToLow",
        "title": "Sort Products by Price (High to Low)",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Select 'Price (high to low)' from the sort dropdown\n3. Read all product prices",
        "expected": "Products are displayed from highest to lowest price",
        "assertions": "For every consecutive pair: priceValues[i] >= priceValues[i+1]",
    },
    {
        "id":    "TC_013",
        "module":"Products",
        "class": "ProductTests",
        "method":"testNavigateToProductDetail",
        "title": "Navigate to Product Detail Page",
        "type":  "Positive",
        "priority": "High",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Note the first product name from the listing\n3. Click on that product",
        "expected": "Product detail page loads with correct product information",
        "assertions": "isProductDetailPageLoaded() = true\ndetailPage.getProductName() == selectedProduct",
    },
    {
        "id":    "TC_014",
        "module":"Products",
        "class": "ProductTests",
        "method":"testProductDetailDataConsistency",
        "title": "Product Detail Data Consistency",
        "type":  "Positive",
        "priority": "High",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Record first product name and price from listing\n3. Click on that product\n4. Compare name, price, and description on detail page",
        "expected": "Product name and price on detail page match the listing page; description is non-empty",
        "assertions": "detailName == listingName\ndetailPrice == listingPrice\ndescription.isEmpty() = false",
    },
    {
        "id":    "TC_015",
        "module":"Products",
        "class": "ProductTests",
        "method":"testAddToCartFromDetailPage",
        "title": "Add to Cart from Product Detail Page",
        "type":  "Positive",
        "priority": "High",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Record initial cart count\n3. Click on first product\n4. Click 'Add to Cart' button on detail page\n5. Click Back to Products button",
        "expected": "Product is added to cart; cart badge count increases by 1",
        "assertions": "updatedCartCount == initialCartCount + 1",
    },
    {
        "id":    "TC_016",
        "module":"Products",
        "class": "ProductTests",
        "method":"testBackButtonFromDetailPage",
        "title": "Back Button from Product Detail Page",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Click on first product to open detail page\n3. Click 'Back to Products' button",
        "expected": "Returns to product listing page with all 6 products visible",
        "assertions": "isProductsPageLoaded() = true\ngetProductCount() == 6",
    },

    # ──────────────── CART TESTS ────────────────
    {
        "id":    "TC_017",
        "module":"Cart",
        "class": "CartTests",
        "method":"testAddSingleItemToCart",
        "title": "Add Single Item to Cart",
        "type":  "Positive",
        "priority": "High",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Click 'Add to Cart' for the first product\n3. Verify cart badge shows 1\n4. Navigate to shopping cart",
        "expected": "Product is in cart with correct name and price; cart badge shows 1",
        "assertions": "cartCount == 1\nisItemInCart(selectedProduct) = true\nitemName matches\nitemPrice matches",
    },
    {
        "id":    "TC_018",
        "module":"Cart",
        "class": "CartTests",
        "method":"testAddMultipleItemsToCart",
        "title": "Add Multiple Items to Cart",
        "type":  "Positive",
        "priority": "High",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Add first 3 products to cart\n3. Verify cart badge shows 3\n4. Navigate to shopping cart",
        "expected": "All 3 products appear in cart; cart badge shows 3",
        "assertions": "cartBadge == 3\ncartPage.getCartItemCount() == 3\nAll 3 items present in cart",
    },
    {
        "id":    "TC_019",
        "module":"Cart",
        "class": "CartTests",
        "method":"testRemoveItemFromCart",
        "title": "Remove Item from Cart",
        "type":  "Positive",
        "priority": "High",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Add 2 products to cart\n3. Navigate to cart\n4. Click Remove on the first product",
        "expected": "First product is removed; second product remains; cart count is 1",
        "assertions": "getCartItemCount() == 1\nisItemInCart(firstProduct) = false\nisItemInCart(secondProduct) = true",
    },
    {
        "id":    "TC_020",
        "module":"Cart",
        "class": "CartTests",
        "method":"testVerifyCartItemDetails",
        "title": "Verify Cart Item Details",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Record first product name and price from listing\n3. Add product to cart\n4. Navigate to cart\n5. Check item name, price, and quantity",
        "expected": "Item name, price, and quantity (1) are correctly displayed in cart",
        "assertions": "cartItemNames[0] == selectedProduct\ncartItemPrices[0] == selectedPrice\ncartItemQuantities[0] == '1'",
    },
    {
        "id":    "TC_021",
        "module":"Cart",
        "class": "CartTests",
        "method":"testContinueShoppingButton",
        "title": "Continue Shopping Button",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Add a product to cart\n3. Navigate to cart\n4. Click 'Continue Shopping' button",
        "expected": "Returns to Products listing page with all 6 products visible",
        "assertions": "isProductsPageLoaded() = true\ngetProductCount() == 6",
    },
    {
        "id":    "TC_022",
        "module":"Cart",
        "class": "CartTests",
        "method":"testEmptyCart",
        "title": "Empty Cart State",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Navigate directly to the cart without adding any items",
        "expected": "Cart page loads and displays empty state with 0 items",
        "assertions": "isCartPageLoaded() = true\nisCartEmpty() = true\ngetCartItemCount() == 0",
    },
    {
        "id":    "TC_023",
        "module":"Cart",
        "class": "CartTests",
        "method":"testRemoveAllItemsFromCart",
        "title": "Remove All Items from Cart",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Add 2 products to cart\n3. Navigate to cart\n4. Remove each item one by one",
        "expected": "Cart becomes empty after all items are removed",
        "assertions": "isCartEmpty() = true",
    },

    # ──────────────── CHECKOUT TESTS ────────────────
    {
        "id":    "TC_024",
        "module":"Checkout",
        "class": "CheckoutTests",
        "method":"testCompleteCheckoutFlow",
        "title": "Complete Checkout Flow",
        "type":  "Positive",
        "priority": "High",
        "pre":   "User is logged in; product is available to add to cart",
        "steps": "1. Login\n2. Add first product to cart\n3. Navigate to cart\n4. Click Checkout\n5. Fill form: First='John', Last='Doe', Postal='12345'\n6. Click Continue\n7. Verify order review page\n8. Click Finish",
        "expected": "Order is completed; confirmation page is displayed with 'Thank you' message",
        "assertions": "isCheckoutStepOneLoaded() = true\nisCheckoutStepTwoLoaded() = true\nisOrderConfirmationLoaded() = true\ngetConfirmationMessage() contains 'Thank you'",
    },
    {
        "id":    "TC_025",
        "module":"Checkout",
        "class": "CheckoutTests",
        "method":"testCheckoutEmptyFormSubmission",
        "title": "Checkout Empty Form Submission",
        "type":  "Negative",
        "priority": "High",
        "pre":   "User is logged in; on checkout step 1 page",
        "steps": "1. Login\n2. Add product to cart\n3. Navigate to cart\n4. Click Checkout\n5. Leave all form fields empty\n6. Click Continue",
        "expected": "Validation error displayed; checkout cannot proceed without required fields",
        "assertions": "isCheckoutErrorDisplayed() = true\ngetCheckoutError() contains 'First Name is required'",
    },
    {
        "id":    "TC_026",
        "module":"Checkout",
        "class": "CheckoutTests",
        "method":"testCheckoutMissingLastName",
        "title": "Checkout - Missing Last Name",
        "type":  "Negative",
        "priority": "High",
        "pre":   "User is logged in; on checkout step 1 page",
        "steps": "1. Login\n2. Add product\n3. Go to cart\n4. Click Checkout\n5. Enter only first name ('John')\n6. Click Continue",
        "expected": "Validation error displayed for missing Last Name",
        "assertions": "isCheckoutErrorDisplayed() = true\ngetCheckoutError() contains 'Last Name'",
    },
    {
        "id":    "TC_027",
        "module":"Checkout",
        "class": "CheckoutTests",
        "method":"testCheckoutMissingPostalCode",
        "title": "Checkout - Missing Postal Code",
        "type":  "Negative",
        "priority": "High",
        "pre":   "User is logged in; on checkout step 1 page",
        "steps": "1. Login\n2. Add product\n3. Go to cart\n4. Click Checkout\n5. Enter first name and last name only\n6. Click Continue",
        "expected": "Validation error displayed for missing Postal Code",
        "assertions": "isCheckoutErrorDisplayed() = true\ngetCheckoutError() contains 'Postal Code'",
    },
    {
        "id":    "TC_028",
        "module":"Checkout",
        "class": "CheckoutTests",
        "method":"testOrderSummaryVerification",
        "title": "Order Summary Verification",
        "type":  "Positive",
        "priority": "High",
        "pre":   "User is logged in; product is available to add to cart",
        "steps": "1. Login\n2. Record first product name and price\n3. Add to cart\n4. Checkout\n5. Fill form and click Continue\n6. Review order summary page",
        "expected": "Order review page shows the correct product and total amount",
        "assertions": "isCheckoutStepTwoLoaded() = true\nreviewItems.contains(selectedProduct) = true\ngetTotalAmount().isEmpty() = false",
    },
    {
        "id":    "TC_029",
        "module":"Checkout",
        "class": "CheckoutTests",
        "method":"testPriceCalculationVerification",
        "title": "Price Calculation Verification",
        "type":  "Positive",
        "priority": "High",
        "pre":   "User is logged in; 2 products added to cart",
        "steps": "1. Login\n2. Add 2 products\n3. Navigate to cart\n4. Checkout\n5. Fill form and click Continue\n6. Read itemTotal, taxAmount, totalAmount from summary",
        "expected": "Item total, tax, and final total are all displayed and non-empty",
        "assertions": "getItemTotal().isEmpty() = false\ngetTaxAmount().isEmpty() = false\ngetTotalAmount().isEmpty() = false",
    },
    {
        "id":    "TC_030",
        "module":"Checkout",
        "class": "CheckoutTests",
        "method":"testOrderConfirmationMessage",
        "title": "Order Confirmation Message",
        "type":  "Positive",
        "priority": "High",
        "pre":   "User is logged in; product available",
        "steps": "1. Login\n2. Add product\n3. Complete full checkout flow\n4. Verify confirmation page content",
        "expected": "Confirmation page displays 'Thank you' message and order details",
        "assertions": "isOrderConfirmationLoaded() = true\ngetConfirmationMessage() contains 'Thank you'\ngetConfirmationText().isEmpty() = false",
    },
    {
        "id":    "TC_031",
        "module":"Checkout",
        "class": "CheckoutTests",
        "method":"testBackToProductsFromConfirmation",
        "title": "Back to Products from Confirmation",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User has completed a checkout and is on the confirmation page",
        "steps": "1. Complete full checkout flow\n2. Verify on confirmation page\n3. Click 'Back Home' / 'Back to Products' button",
        "expected": "Returns to Products listing page with all 6 products visible",
        "assertions": "isProductsPageLoaded() = true\ngetProductCount() == 6",
    },

    # ──────────────── NAVIGATION TESTS ────────────────
    {
        "id":    "TC_032",
        "module":"Navigation",
        "class": "NavigationTests",
        "method":"testOpenSidebarMenu",
        "title": "Open Sidebar Menu",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Click the hamburger (≡) menu button in the top-left",
        "expected": "Sidebar navigation menu opens and becomes visible",
        "assertions": "No exception thrown; menu element is visible after click",
    },
    {
        "id":    "TC_033",
        "module":"Navigation",
        "class": "NavigationTests",
        "method":"testCloseSidebarMenu",
        "title": "Close Sidebar Menu",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Open sidebar menu\n3. Click the close (✕) button on the menu",
        "expected": "Sidebar menu closes and is no longer visible",
        "assertions": "No exception thrown; menu is hidden after close",
    },
    {
        "id":    "TC_034",
        "module":"Navigation",
        "class": "NavigationTests",
        "method":"testAllItemsLink",
        "title": "All Items Link Navigation",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Open sidebar menu\n3. Click 'All Items' link",
        "expected": "Remains on (or returns to) Products listing page with all 6 products",
        "assertions": "isProductsPageLoaded() = true\ngetProductCount() == 6",
    },
    {
        "id":    "TC_035",
        "module":"Navigation",
        "class": "NavigationTests",
        "method":"testAboutLink",
        "title": "About Link Navigation",
        "type":  "Positive",
        "priority": "Low",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Record current URL\n3. Open sidebar menu\n4. Click 'About' link",
        "expected": "Browser navigates to the external Sauce Labs About page",
        "assertions": "getCurrentUrl() != originalUrl\ngetCurrentUrl() contains 'saucelabs.com'",
    },
    {
        "id":    "TC_036",
        "module":"Navigation",
        "class": "NavigationTests",
        "method":"testLogoutLink",
        "title": "Logout Link Navigation",
        "type":  "Positive",
        "priority": "High",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Open sidebar menu\n3. Click 'Logout' link",
        "expected": "User is logged out and redirected to the Login page",
        "assertions": "loginPage.isLoginPageLoaded() = true",
    },
    {
        "id":    "TC_037",
        "module":"Navigation",
        "class": "NavigationTests",
        "method":"testResetAppState",
        "title": "Reset App State",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in; 2 items added to cart",
        "steps": "1. Login\n2. Add 'Sauce Labs Backpack' and 'Sauce Labs Bike Light' to cart\n3. Verify cart count = 2\n4. Open sidebar and click 'Reset App State'\n5. Close sidebar",
        "expected": "Cart is emptied; products page remains loaded with 6 items",
        "assertions": "cartCountAfter == 0\nisProductsPageLoaded() = true\ngetProductCount() == 6",
    },
    {
        "id":    "TC_038",
        "module":"Navigation",
        "class": "NavigationTests",
        "method":"testSidebarMenuAccessibility",
        "title": "Sidebar Menu Accessibility",
        "type":  "Positive",
        "priority": "Medium",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Open sidebar menu\n3. Close sidebar menu",
        "expected": "Sidebar menu opens and closes without throwing exceptions",
        "assertions": "No Exception thrown during open/close operations",
    },
    {
        "id":    "TC_039",
        "module":"Navigation",
        "class": "NavigationTests",
        "method":"testMultipleMenuOpenCloseCycles",
        "title": "Multiple Menu Open/Close Cycles",
        "type":  "Positive",
        "priority": "Low",
        "pre":   "User is logged in and on the Products listing page",
        "steps": "1. Login\n2. Open and close sidebar menu 3 consecutive times with 200ms delay between each",
        "expected": "Menu functions correctly across all 3 cycles; still on products page",
        "assertions": "isProductsPageLoaded() = true after 3 cycles",
    },

    # ──────────────── END-TO-END TESTS ────────────────
    {
        "id":    "TC_040",
        "module":"End-to-End",
        "class": "EndToEndTests",
        "method":"testCompleteHappyPathScenario",
        "title": "Complete Happy Path Scenario",
        "type":  "Positive",
        "priority": "Critical",
        "pre":   "Application is open on login page; browser is fresh",
        "steps": "1. Login with standard_user\n2. Verify 6 products displayed\n3. Sort by Price (low to high)\n4. Add 2 products (index 0 & 1) to cart\n5. Navigate to shopping cart\n6. Verify cart has 2 items with correct details\n7. Click Checkout\n8. Fill form: John, Doe, 12345\n9. Click Continue\n10. Verify order review shows 2 items + pricing\n11. Click Finish\n12. Verify confirmation message\n13. Click Back to Products",
        "expected": "Complete purchase flow succeeds end-to-end; confirmation page shown with 'Thank you'",
        "assertions": "Every step loads correctly\ngetConfirmationMessage() contains 'Thank you'\nFinal products page loaded",
    },
    {
        "id":    "TC_041",
        "module":"End-to-End",
        "class": "EndToEndTests",
        "method":"testHappyPathSingleItem",
        "title": "Happy Path - Single Item Purchase",
        "type":  "Positive",
        "priority": "High",
        "pre":   "Application is open on login page",
        "steps": "1. Login with standard_user\n2. Add first product to cart\n3. Navigate to cart\n4. Click Checkout\n5. Fill form: Jane, Smith, 54321\n6. Click Continue\n7. Click Finish",
        "expected": "Single item checkout completes successfully with confirmation",
        "assertions": "isOrderConfirmationLoaded() = true\ngetConfirmationMessage() contains 'Thank you'",
    },
    {
        "id":    "TC_042",
        "module":"End-to-End",
        "class": "EndToEndTests",
        "method":"testHappyPathWithProductDetails",
        "title": "Happy Path via Product Detail Page",
        "type":  "Positive",
        "priority": "High",
        "pre":   "Application is open on login page",
        "steps": "1. Login with standard_user\n2. Click first product to open detail page\n3. Verify detail page loaded\n4. Add product to cart from detail page\n5. Click Back to Products\n6. Navigate to cart\n7. Verify product is in cart\n8. Click Checkout\n9. Fill form: Bob, Wilson, 99999\n10. Click Continue\n11. Click Finish",
        "expected": "Checkout flow via product detail page completes successfully",
        "assertions": "isProductDetailPageLoaded() = true\nisItemInCart(selectedProduct) = true\nisOrderConfirmationLoaded() = true",
    },
]

# ── Write test case rows ───────────────────────────────────────────────────────
for row_idx, tc in enumerate(test_cases, start=2):
    module   = tc["module"]
    tc_type  = tc["type"]
    priority = tc["priority"]

    # Choose background colour
    if tc_type == "Negative":
        row_bg = NEG_BG
    elif module == "End-to-End":
        row_bg = E2E_BG
    elif row_idx % 2 == 0:
        row_bg = MOD_COLORS.get(module, WHITE)
    else:
        row_bg = WHITE

    values = [
        tc["id"],
        module,
        tc["class"],
        tc["method"],
        tc["title"],
        tc_type,
        priority,
        tc["pre"],
        tc["steps"],
        tc["expected"],
        tc["assertions"],
        "",  # Status – left blank for manual tracking
    ]

    for col_idx, value in enumerate(values, start=1):
        cell = ws.cell(row=row_idx, column=col_idx, value=value)
        cell.fill      = fill(row_bg)
        cell.border    = thin_border()
        cell.font      = Font(name="Calibri", size=10)

        # Alignment
        if col_idx in (1, 6, 7, 12):   # centred columns
            cell.alignment = center()
        else:
            cell.alignment = left()

        # Colour-code Test Type
        if col_idx == 6:
            if tc_type == "Positive":
                cell.font = Font(name="Calibri", size=10, bold=True, color="375623")
            else:
                cell.font = Font(name="Calibri", size=10, bold=True, color="9C0006")

        # Colour-code Priority
        if col_idx == 7:
            p_color = PRIORITY_COLORS.get(priority, "000000")
            cell.font = Font(name="Calibri", size=10, bold=True, color=p_color)

    # Row height – taller for multi-line steps
    ws.row_dimensions[row_idx].height = 90

# ── Add a summary sheet ────────────────────────────────────────────────────────
ws2 = wb.create_sheet("Summary")

summary_data = [
    ("Module",     "Total", "Positive", "Negative"),
    ("Login",      7, 2, 5),
    ("Products",   9, 9, 0),
    ("Cart",       7, 7, 0),
    ("Checkout",   8, 5, 3),
    ("Navigation", 8, 8, 0),
    ("End-to-End", 3, 3, 0),
    ("TOTAL",     42,34, 8),
]

for r_idx, row in enumerate(summary_data, start=1):
    for c_idx, val in enumerate(row, start=1):
        cell = ws2.cell(row=r_idx, column=c_idx, value=val)
        cell.border    = thin_border()
        cell.alignment = center()
        if r_idx == 1:
            cell.font = Font(bold=True, color=HEADER_FG, name="Calibri", size=11)
            cell.fill = fill(HEADER_BG)
        elif r_idx == len(summary_data):
            cell.font = Font(bold=True, name="Calibri", size=11)
            cell.fill = fill("D9E1F2")
        else:
            cell.font = Font(name="Calibri", size=10)
            cell.fill = fill(MOD_COLORS.get(row[0], WHITE))

for c in range(1, 5):
    ws2.column_dimensions[get_column_letter(c)].width = 16

# ── Save ──────────────────────────────────────────────────────────────────────
OUT = r"C:\Users\abdulla.murshidkv\Desktop\myprojects\Selenium Capstone\docs\SauceDemo_Test_Cases.xlsx"
wb.save(OUT)
print(f"Saved: {OUT}")
print(f"Total test cases written: {len(test_cases)}")
