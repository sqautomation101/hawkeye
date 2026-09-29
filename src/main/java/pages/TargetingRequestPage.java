package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertTrue;

public class TargetingRequestPage extends BasePage {

    public TargetingRequestPage (Page page) {
        super(page);
    }

    private Locator activeGroupContainer;

    public void clickGroupTab(String tabName) {

       Locator targetTab = page.getByRole(AriaRole.TAB)
               .filter(new Locator.FilterOptions().setHas(page.getByText(tabName, new Page.GetByTextOptions().setExact(false))));

       // Locator targetTab = page.getByRole(AriaRole.TAB)
       //         .filter(new Locator.FilterOptions().setHasText((java.util.regex.Pattern.compile(".*" + tabName + "$"))));

        targetTab.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        targetTab.click();

        System.out.println("Successfully navigated to the group tab: " + tabName);
    }


    public void verifyTabIsActive(String tabName) {
        Locator targetTab = page.getByRole(AriaRole.TAB)
                .filter(new Locator.FilterOptions().setHasText(tabName));

        // Grabs the real-time class string: "tabs-header-tab ph active"
        String classAttribute = targetTab.getAttribute("class");

        assertTrue(
                classAttribute.contains("active"),
                "❌ FAILURE: Tab '" + tabName + "' was clicked but failed to switch to an active state!"
        );
    }


    public void clickSubGroupAccordion(String subGroupName) {

        Locator subgroupAccordion = page.locator("div[role='button'].section-expandable-title")
                .filter(new Locator.FilterOptions().setHasText(subGroupName));

        subgroupAccordion.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(3000));
        subgroupAccordion.click();

        System.out.println("Successfully clicked the subgroup: " + subGroupName);

    }

    public void clickAttribute(String subGroupName, String attributeName) {
        // 1. Target the clickable header handle bar using the exact text visible on screen
        // This ignores hidden HTML code variations by looking explicitly at the text content layout layer
        Locator accordionHeader = page.locator(".section-expandable-title, .accordion-item-header, div[role='button']")
                .filter(new Locator.FilterOptions().setHasText(subGroupName)).first();

        // 2. Locate the main grid content area box next to/below that specific header
        // OutSystems groups accordion containers directly beneath or inside the structural panel layout wrapper
        Locator contentGrid = page.locator(".propertySelectionAccordionContainer, .propertySelectionAccordionContent")
                .filter(new Locator.FilterOptions().setHasText(attributeName)).first();

        // 3. Conditional Toggling: Expand the accordion container drawer if it is hidden from view
        if (!contentGrid.isVisible()) {
            System.out.println("⏳ Accordion [" + subGroupName + "] is closed. Expanding it now...");

            accordionHeader.scrollIntoViewIfNeeded();
            accordionHeader.click();

            // Pause for up to 3 seconds to let OutSystems render animations finish drawing safely
            contentGrid.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(3000));
        }

        // 4. FIX: Target the SPECIFIC clickable element inside the grid using an exact text match
        // This stops Playwright from blindly clicking the middle of the large container box
        Locator targetAttributeButton = contentGrid
                .locator("span, div, button, a")
                .getByText(attributeName, new Locator.GetByTextOptions().setExact(true))
                .first();

        // Scroll to the actual item and click it directly
        targetAttributeButton.scrollIntoViewIfNeeded();
        targetAttributeButton.click();

        System.out.println("🎉 Successfully expanded [" + subGroupName + "] and clicked specific attribute [" + attributeName + "].");
    }

    public boolean isAttributeDisplayedInMainContent (String groupTabName, String attributeName) {

        String cleanTabName = groupTabName.replaceAll("[0-9.\\s]", "");

        if (cleanTabName.endsWith("s") || cleanTabName.endsWith("S")) {
            cleanTabName = cleanTabName.substring(0, cleanTabName.length() - 1);
        }

        String containerSelector = "div[id*='" + cleanTabName + "InnerContent']";
        Locator mainContentArea = page.locator(containerSelector);

        Locator attributeTitle = mainContentArea.locator("div.propertyTitleContainer")
                .filter(new Locator.FilterOptions().setHasText(attributeName));

        try {
            attributeTitle.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(3000));

            return attributeTitle.isVisible();
        } catch (Exception e) {
            System.out.println("❌ Verification Failed: Attribute [" + attributeName + "] missing from " + cleanTabName + " area.");
            return false;
        }

    }


    public void clickAndPopulateAttribute(String groupTab, String attribute, String dataType, String valueToSet, String operator) {

        System.out.println("LOG: Method createNewCampaign started. groupTab=" + groupTab + ", attribute=" + attribute + ", dataType=" + dataType);

        // --- PHASE 1: ISOLATE THE ACTIVE GROUP CONTAINER ---
        // Clean "Transactions" -> "Transaction", "Segments" -> "Segment", "Demographics" -> "Demographic"
        String cleanTabName = groupTab.replaceAll("[0-9.\\s]", "");
        if (cleanTabName.endsWith("s") || cleanTabName.endsWith("S")) {
            cleanTabName = cleanTabName.substring(0, cleanTabName.length() - 1);
        }
        System.out.println("LOG: cleanTabName resolved to: " + cleanTabName);

        // Dynamic ID template mapping to the main content zone sections
        String containerSelector = "div[id*='" + cleanTabName + "MainContent']";
        System.out.println("LOG: containerSelector is: " + containerSelector);
        Locator mainContentArea = page.locator(containerSelector);

        //Lock the attribute block
        // Find the title element inside the specific section
        Locator attributeTitle = mainContentArea.locator("div.propertyTitleContainer")
                .filter(new Locator.FilterOptions().setHasText(attribute));
        System.out.println("LOG: attributeTitle locator defined for attribute: " + attribute);

        //Check if it exists first
        attributeTitle.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        System.out.println("LOG: attributeTitle is visible.");

        // Isolate the parent block wrapper that encapsulates title, operators, and inputs
        Locator targetAttributeBlock = attributeTitle.locator("xpath=./ancestor::div[contains(@class, 'propertyOuterContainer')]");
        System.out.println("LOG: targetAttributeBlock locator defined.");

        targetAttributeBlock.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        System.out.println("LOG: targetAttributeBlock is visible.");

        page.waitForTimeout(3000);
        System.out.println("LOG: Finished waiting 3 seconds. Entering switch statement for dataType: " + dataType);

        switch (dataType.toLowerCase().trim()) {

            case "action":
                System.out.println("LOG: Entered case 'action'");
                Locator targetCheckboxContainer = targetAttributeBlock
                        .locator("div.propertyCheckboxOuterContainer")
                        .filter(new Locator.FilterOptions().setHasText(valueToSet));
                System.out.println("LOG: targetCheckboxContainer defined for: " + valueToSet);

                Locator checkbox = targetCheckboxContainer.locator("input[type='checkbox']");
                checkbox.scrollIntoViewIfNeeded();
                System.out.println("LOG: Scrolled checkbox into view.");

                if (!checkbox.isChecked()) {
                    checkbox.click();
                    System.out.println("Checkbox successfully ticked for: " + valueToSet);
                } else {
                    System.out.println("Checkbox was already ticked");
                }
                break;

            case "function":
                System.out.println("LOG: Entered case 'function'");
                // 1. Isolate the operator container block inside your active attribute section
                Locator operatorContainer = targetAttributeBlock.locator("div.propertyDropdownOperatorContainer");
                System.out.println("LOG: operatorContainer defined.");

                // 2. Target the custom Choices.js dropdown combobox element that opens the options
                Locator operatorDropdown = operatorContainer.locator("div.choices[role='combobox']");
                System.out.println("LOG: operatorDropdown defined.");

                // 3. Click to expand the operator dropdown options panel
                operatorDropdown.scrollIntoViewIfNeeded();
                operatorDropdown.click();
                System.out.println("LOG: operatorDropdown clicked.");

                // 4. Select the matching option from the dropdown popup list that overlays on screen
                // OutSystems Choices.js popups typically mount globally or within a shared list wrapper
                operatorContainer.locator(".choices__list--dropdown")
                        .getByText(operator, new Locator.GetByTextOptions().setExact(true))
                        .click();
                System.out.println("LOG: Clicked operator choice: " + operator);

                System.out.println("Operator already selected");

                // 5. Locate and fill the adjacent input field for the function value
                Locator functionInput = targetAttributeBlock.locator("input.form-control");
                System.out.println("LOG: functionInput defined.");

                functionInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                System.out.println("LOG: functionInput is visible.");

                functionInput.fill(valueToSet);
                System.out.println("LOG: functionInput filled with: " + valueToSet);

                System.out.println("Function already populated");
                break;

            default:
                System.out.println("LOG: dataType did not match action or function. Value was: " + dataType);
                break;

        }

        System.out.println("LOG: clickAndPopulateAttribute method execution completed.");

        this.activeGroupContainer = mainContentArea;
    }


    public void saveAttribute(String attribute) {

        if (activeGroupContainer == null) {
            throw new IllegalStateException("No active group container. Call clickAndPopulateAttribute() first.");
        }

        Locator saveButton = activeGroupContainer
                .locator("div.propertyActionActiveSaveIcon");

        saveButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));

        saveButton.scrollIntoViewIfNeeded();
        saveButton.click();
        System.out.println("Saved attribute." + attribute);
    }


    public void createCampaignTitle(String campaignTitle) {

        Locator titleInput = page.locator("input[name='campaignTitle']");

        titleInput.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        titleInput.fill(campaignTitle);

        System.out.println("Campaign title populated: " + campaignTitle);

    }

    public void clickSelectReferenceCampaign() {
        Locator selectReferenceCampaign = page.locator(".proceedActionActiveContainer")
                .filter(new Locator.FilterOptions().setHasText("Select Reference Campaign"));

        selectReferenceCampaign.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));

        selectReferenceCampaign.click();
        System.out.println("Clicked Select Reference Campaign button.");
    }

    public void clickWithCampaignReference() {
        Locator radio = page.getByLabel("With Campaign Reference");
        radio.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
        radio.click();
        System.out.println("Selected: With Campaign Reference.");
    }

    public void clickWithNoCampaignReference() {
        Locator radio = page.getByLabel("With No Campaign Reference");
        radio.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
        radio.click();
        System.out.println("Selected: With No Campaign Reference.");
    }

    public void clickProceed() {
        Locator proceedButton = page.locator("div[id*='PopupActiveButton']")
                .filter(new Locator.FilterOptions().setHasText("Proceed"));

        proceedButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));

        proceedButton.click();
        System.out.println("Clicked Proceed.");
    }

    public void clickBackToHome() {
        Locator backToHomeButton = page.locator("div[id*='PopupInactiveButton']")
                .filter(new Locator.FilterOptions().setHasText("Back to Home"));

        backToHomeButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(20000));

        backToHomeButton.click();
        System.out.println("Clicked Back to Home.");
    }

    public void verifyCampaignAppearsInTable(String campaignTitle) {
        Locator table = page.locator("table[id*='requestTable']");
        table.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));

        Locator campaignRow = page.getByRole(AriaRole.ROW)
                .filter(new Locator.FilterOptions().setHasText(campaignTitle))
                .first();

        campaignRow.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(15000));

        assertThat(campaignRow).isVisible();
        System.out.println("Verified campaign appears in table: " + campaignTitle);
    }
}


