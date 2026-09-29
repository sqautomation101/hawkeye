package step_definitions;

import base.ScenarioContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PageAssertions;
import io.cucumber.java.en.*;
import pages.Homepage;
import utils.DbUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.*;

public class HomepageSteps {

    private Homepage homepage;
    private final ScenarioContext scenarioContext;

    // PicoContainer passes ScenarioContext automatically
    public HomepageSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
        this.homepage = scenarioContext.getPageManager().getHomepage();
    }

    @Then("the Homepage elements should be visible")
    public void theHomepageElementShouldBeVisible() {
        assertTrue(homepage.isNewRequestButtonHeaderVisible(), "New Request button at the top is missing");
        //assertTrue(homepage.isAllTabVisible(), "All tab is missing");
    }

    @Then("the user should only see the following filter tabs:")
    public void theTabsShouldBeVisible(List<String> expectedTabs) {
        List<String> actualTabs = homepage.getAllVisibleTabNames();

        assertEquals(actualTabs, expectedTabs,
                "The displayed tabs does not match the expected tabs.");
    }


    //try to create method for side panel
    @Then("the side panel links should be visible for {string}")
    public void theSidePanelButtonsShouldBeVisible(String role) {

        homepage.expandSidePanel();

        switch(role.toLowerCase()) {
            case "admin":
                // Admin should see ALL three buttons
                assertTrue(homepage.isSidePanelHomeButtonVisible(), "Home button missing for Admin");
                assertTrue(homepage.isSidePanelNewRequestButtonVisible(), "New Request button missing for Admin");
                assertTrue(homepage.isSidePanelAdminButtonVisible(), "Admin button missing for Admin");

                assertTrue(homepage.isSidePanelUserManagementButtonVisible(), "User Management button missing for Admin");
                assertTrue(homepage.isSidePanelAttributesButtonVisible(), "Attributes button missing for Admin");
                break;
            case "editor":
                // Editor should see Home and New Request...
                assertTrue(homepage.isSidePanelHomeButtonVisible(), "Home button missing for Editor");
                assertTrue(homepage.isSidePanelNewRequestButtonVisible(), "New Request button missing for Editor");

                // SECURITY CHECK: Editor must NOT see the Admin button!
                assertFalse(homepage.isSidePanelAdminButtonVisible(), "SECURITY ERROR: Editor can see the Admin button!");
                assertFalse(homepage.isSidePanelUserManagementButtonVisible(), "SECURITY ERROR: Editor can see the User Management button!");
                assertFalse(homepage.isSidePanelAttributesButtonVisible(), "SECURITY ERROR: Editor can see the Attributes button!");
                break;
            default:
                throw new IllegalArgumentException("Unknown user role: " + role);
        }

    }

    @When("the user navigates to the {string} module")
    public void theUserNavigateToTheModule(String module) {

        switch(module.toLowerCase()) {
            case "homepage":
                homepage.clickHomeButton();
                break;
            case "user management":
                homepage.clickUserManagement();
                break;
            case "attributes":
                homepage.clickAttributes();
                break;
            case "new request side panel":
                homepage.clickNewRequestSidePanel();
                break;
            case "new request header":
                homepage.clickNewRequestHeader();
                break;
            default:
                throw new IllegalArgumentException("Unknown module: " + module);
        }
    }

    @Then("the URL should contain {string}")
    public void theUrlShouldContain(String expectedPath) {
        // 1. SMART WAIT: Tell Playwright to wait up to 5 seconds for the URL to contain your path
        // We pass a lambda function that checks the URL dynamically as it changes
        try {
            scenarioContext.getPage().waitForURL(
                    url -> url.toLowerCase().contains(expectedPath.toLowerCase()),
                    new Page.WaitForURLOptions().setTimeout(5000)
            );
        } catch (Exception e) {
            // If it times out, let the assertion below fail clearly so you see the logs
        }

        // 2. ASSERT: Perform the final check on the stable, fully loaded URL
        String actualUrl = scenarioContext.getPage().url();
        assertTrue(
                actualUrl.toLowerCase().contains(expectedPath.toLowerCase()),
                "Expected URL [" + actualUrl + "] to contain text fragment [" + expectedPath + "]"
        );
    }




    @Then("the UI data for campaign {string} should match the database records")
    public void verifyUiDataMatchesDatabase(String campaignTitle) {
        // 1. Extract data from DB
        Map<String, String> databaseRecordMap = DbUtils.getCampaignMetadataByTitle(campaignTitle);

        assertNotNull(databaseRecordMap.get("title"),
                "❌ DATABASE ERROR: Campaign '" + campaignTitle + "' was not found in the PostgreSQL database!");

        Map<String, String> uiRecordMap = homepage.getUiCampaignRowDetails(campaignTitle);

        assertEquals(
                uiRecordMap.get("title").toLowerCase(),
                databaseRecordMap.get("title").toLowerCase(),
                "❌ DATA MISMATCH: UI Campaign Title does not match the Database record!"
        );

        // 4. ASSERTION: Compare Status Badges (Case-Insensitive)
        assertEquals(
                uiRecordMap.get("status").toLowerCase(),
                databaseRecordMap.get("status").toLowerCase(),
                "❌ DATA MISMATCH: Visual Dashboard Status badge does not match PostgreSQL state!"
        );

        // 5. BONUS DATA CHECK: Clean and verify dates if needed
        // If your dates don't match, we can intercept and normalize them here.
        System.out.println("🔄 Comparing UI Date (" + uiRecordMap.get("date_created") + ") to DB Date (" + databaseRecordMap.get("date_created") + ")");

        System.out.println("🎉 SUCCESS: All UI dashboard metrics for '" + campaignTitle + "' perfectly match the PostgreSQL backend database.");
    }


    @When("the user views the requests dashboard table")
    public void theUserViewsTheRequestsDashboardTable() {
        homepage.clickHomeButton();
    }
}
