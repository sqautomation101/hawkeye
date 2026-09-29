package step_definitions;

import base.ScenarioContext;
import com.microsoft.playwright.Locator;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.TargetingRequestPage;

import static org.testng.Assert.assertTrue;

public class TargetingRequestPageSteps {

    private TargetingRequestPage targetingRequestPage;
    private ScenarioContext scenarioContext;

    public TargetingRequestPageSteps (ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
        this.targetingRequestPage = scenarioContext.getPageManager().getTargetingRequestPage();
    }

    @When("the user navigates to the {string} Group tab")
    public void theUserNavigateToTheGroupTab(String tabName) {
        targetingRequestPage.clickGroupTab(tabName);
    }

    /*
    @Then("the user sees the {string} Group tab")
    public void theUserSeesTheGroupTab(String tabName) {
        targetingRequestPage.verifyTabIsActive(tabName);
    }
    */

    @When("the user selects the {string} attribute from {string} subgroup")
    public void theUserSelectsAttribute(String attributeName, String subGroupName) {
        targetingRequestPage.clickAttribute(subGroupName, attributeName);
    }

    @Then("the attribute {string} should be visible in the {string} main content area")
    public void verifyAttributeOnMainContentArea(String attributeName, String groupTabName) {
        // Passes the tab context directly to verify the correct center zone
        assertTrue(
                targetingRequestPage.isAttributeDisplayedInMainContent(groupTabName, attributeName),
                "❌ UI ERROR: '" + attributeName + "' attribute did not appear inside the " + groupTabName + " main content area!"
        );
        System.out.println("🎉 Successfully verified '" + attributeName + "' on the '" + groupTabName + "' main content area.");
    }


    @When("the user populates the attribute data under {string} with name {string}, type {string}, value {string}, and operator {string}")
    public void theUserPopulatesTheAttributeData(String groupTab, String attribute, String dataType, String valueToSet, String operator) {
        // 1. Execute your populating method logic
        targetingRequestPage.clickAndPopulateAttribute(groupTab, attribute, dataType, valueToSet, operator);

        // 2. Immediate baseline logging verification
        System.out.println("🤖 Automation Action: Successfully processed field execution workflow for " + attribute);
    }

    @When("the user saves the {string} attribute")
    public void theUserSavesAttribute(String attribute) {
        targetingRequestPage.saveAttribute(attribute);
    }

    @When("the user populates the campaign title with {string}")
    public void theUserPopulatesTheCampaignTitle(String campaignTitle) {
        targetingRequestPage.createCampaignTitle(campaignTitle);
    }

    @When("the user clicks the \"Select Reference Campaign\" button")
    public void theUserClicksSelectReferenceCampaign() {
        targetingRequestPage.clickSelectReferenceCampaign();
    }

    @When("the user clicks the \"With No Campaign Reference\"")
    public void theUserClicksWithNoCampaignReference() {
        targetingRequestPage.clickWithNoCampaignReference();
    }

    @When("the user clicks the \"Proceed\" button")
    public void theUserClicksProceed() {
        targetingRequestPage.clickProceed();
    }

    @When("the user clicks the \"Back to Home\" button")
    public void theUserClicksBackToHome() {
        targetingRequestPage.clickBackToHome();
    }

    @Then("the campaign {string} should appear in the request table")
    public void theCampaignShouldAppearInTheRequestTable(String campaignTitle) {
        targetingRequestPage.verifyCampaignAppearsInTable(campaignTitle);
    }

}
