package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertTrue;

public class Homepage extends BasePage {

    public Homepage (Page page) {
        super(page);
    }

    private Locator requestHeader = page.locator("div[id*='RequestText']");
    private Locator logoutButton = page.locator(".sidebar-LogoutContainer").getByText("Logout");
    private Locator newRequestButtonHeader = page.locator(".newRequestTextContainer").getByText("New Request");

    private Locator sidePanelHomeButton = page.locator(".sidebar-SideMenuText").getByText("Home", new Locator.GetByTextOptions().setExact(true));

    // Matches the New Request layout block containing the plus icon
    private Locator sidePanelNewRequestButton = page.locator(".sidebar-SideMenuText").getByText("New Request", new Locator.GetByTextOptions().setExact(true));

    // REMOVE or UPDATE the old sidePanelAdminButton and adminAccordionHeader
    // FIX: Targets the exact active side menu item that contains the visible word 'Admin'
    private Locator sidePanelAdminButton = page.locator(".sidebar-SideMenuText").getByText("Admin", new Locator.GetByTextOptions().setExact(true));

    // NEW LOCATOR: Direct text targeting handles dynamic underlying tag shifts easily
    //private Locator adminMenuTextElement = page.locator("span:has-text('Admin')");

    // FIX: Targets standard HTML links/buttons and matches the complete string exactly
    private Locator userManagementSubLink = page.getByRole(com.microsoft.playwright.options.AriaRole.LINK)
            .filter(new Locator.FilterOptions().setHasText(java.util.regex.Pattern.compile("^User Management$")));

    private Locator attributesSubLink = page.getByRole(com.microsoft.playwright.options.AriaRole.LINK)
            .filter(new Locator.FilterOptions().setHasText(java.util.regex.Pattern.compile("^Attributes$")));

    private Locator sidePanel = page.locator("#b1-SideNav");




    public void logout() {
        expandSidePanel();
        logoutButton.click();
        //page.evaluate("document.querySelector('.fa-power-off').click()");
    }

    public boolean isRequestHeaderVisible() {
        requestHeader.waitFor(); // wait for it to appear first
        return requestHeader.isVisible();
    }

    public boolean isNewRequestButtonHeaderVisible() {
        newRequestButtonHeader.waitFor();
        return newRequestButtonHeader.isVisible();
    }

    public void iAmOnTheHomepage() {
        assertTrue(isRequestHeaderVisible(), "User is not in the Homepage");
    }

    public List<String> getAllVisibleTabNames() {

        Locator tabContainers = page.locator(".activeRequestFilterContainer, .inactiveRequestFilterContainer");

        //wait to at least display the first tab
        tabContainers.first().waitFor();

        //extract the inner span text entries safely
        return tabContainers.locator("span").allTextContents();
    }

    //try to create method for side panel
    public boolean isSidePanelHomeButtonVisible() {
        try {
            sidePanelHomeButton.waitFor(new Locator.WaitForOptions().setTimeout(3000));
            return sidePanelHomeButton.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSidePanelNewRequestButtonVisible() {
        try {
            sidePanelNewRequestButton.waitFor(new Locator.WaitForOptions().setTimeout(3000));
            return sidePanelNewRequestButton.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSidePanelAdminButtonVisible() {
        try {
            sidePanelAdminButton.waitFor(new Locator.WaitForOptions().setTimeout(3000));
            return sidePanelAdminButton.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAdminButton() {
        expandSidePanel();
        sidePanelAdminButton.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(3000));
        sidePanelAdminButton.click();
        System.out.println("User clicked the admin button successfully");
    }

    public boolean isSidePanelUserManagementButtonVisible() {
        try {
            // GUARD RAIL: If the Admin section button is missing (like for an Editor), stop immediately!
            if (!isSidePanelAdminButtonVisible()) {
                return false;
            }

            // If the child menu item isn't visible yet, click the main Admin panel container to reveal it
            if (!userManagementSubLink.isVisible()) {
                sidePanelAdminButton.click();
                // Wait for the slide action animation to complete layout positioning
                userManagementSubLink.waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(3000));
            }
            return userManagementSubLink.isVisible();
        } catch (Exception e) {
            System.out.println("User Management sublink not revealed: " + e.getMessage());
            // Fallback click bypass injection if native layout intercept fails
            try {
                sidePanelAdminButton.dispatchEvent("click");
                return userManagementSubLink.isVisible();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public void clickUserManagement() {
        try {
            // CRITICAL FIX: Only click the Admin button if the sublink isn't visible yet!
            if (!userManagementSubLink.isVisible()) {
                clickAdminButton();
            }

            // Ensure the item is visible and fully drawn on screen before interacting
            userManagementSubLink.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(3000));

            userManagementSubLink.click(new Locator.ClickOptions().setForce(true));
            System.out.println("Successfully clicked User Management.");
        } catch (Exception e) {
            System.out.println("Failed to click User Management sub-menu link: " + e.getMessage());
            userManagementSubLink.dispatchEvent("click");
        }
    }

    public boolean isSidePanelAttributesButtonVisible() {
        try {
            sidePanelAdminButton.waitFor(new Locator.WaitForOptions().setTimeout(3000));

            if (!attributesSubLink.isVisible()) {
                sidePanelAdminButton.click(new Locator.ClickOptions().setForce(true));
                attributesSubLink.waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(3000));
            }
            return attributesSubLink.isVisible();
        } catch (Exception e) {
            System.out.println("Attributes sublink not revealed: " + e.getMessage());
            return false;
        }
    }

    public void clickAttributes() {
        try {
            // CRITICAL FIX: Only click the Admin button if the sublink isn't visible yet!
            if (!attributesSubLink.isVisible()) {
                clickAdminButton();
            }

            // Ensure the item is visible and fully drawn on screen before interacting
            attributesSubLink.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(3000));

            attributesSubLink.click(new Locator.ClickOptions().setForce(true));
            System.out.println("Successfully clicked Attributes.");
        } catch (Exception e) {
            System.out.println("Failed to click Attributes sub-menu link: " + e.getMessage());
            attributesSubLink.dispatchEvent("click");
        }
    }


    public void expandSidePanel() {
        sidePanel.waitFor();
        sidePanel.hover();
        page.waitForTimeout(300); // Allow sidebar slide layout to finish position
       // page.pause();
    }


    public void clickHomeButton() {
        try {
            sidePanelHomeButton.waitFor(new Locator.WaitForOptions().setTimeout(3000));
            sidePanelHomeButton.click();

            System.out.println("Successfully clicked Home button.");
        } catch (Exception e) {
            System.out.println("Failed to click Home button: \" + e.getMessage())");
            sidePanelHomeButton.click();
        }
    }

    public void clickNewRequestSidePanel() {
        try {
            sidePanelNewRequestButton.waitFor(new Locator.WaitForOptions().setTimeout(3000));
            sidePanelNewRequestButton.click();

            System.out.println("Successfully clicked New Request from side panel.");
        } catch (Exception e) {
            System.out.println("Failed to click New Request from side panel: \" + e.getMessage())");
            sidePanelNewRequestButton.dispatchEvent("click");
        }
    }

    public void clickNewRequestHeader() {
        try {
            newRequestButtonHeader.waitFor(new Locator.WaitForOptions().setTimeout(3000));
            newRequestButtonHeader.click();

            System.out.println("Successfully clicked New Request from homepage header.");
        } catch (Exception e) {
            System.out.println("Failed to click New Request from homepage header: \" + e.getMessage())");
            newRequestButtonHeader.dispatchEvent("click");
        }
    }

    public Map<String, String> getUiCampaignRowDetails(String campaignTitle) {
        Map<String, String> uiRecordsMap = new HashMap<>();

        Locator targetRow = page.getByRole(AriaRole.ROW)
                .filter(new Locator.FilterOptions().setHasText(campaignTitle))
                .first();

        targetRow.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));

        String uiTitle = targetRow.locator("td[data-header='Title']").innerText().trim();
        String uiLastUpdated = targetRow.locator("td[data-header='Last Updated']").innerText().trim();
        String uiDateCreated = targetRow.locator("td[data-header='Date Created']").innerText().trim();
        String uiStatus = targetRow.locator("td[data-header='Status']").innerText().trim();

        uiRecordsMap.put("title", uiTitle);
        uiRecordsMap.put("last_updated", uiLastUpdated);
        uiRecordsMap.put("date_created", uiDateCreated);
        uiRecordsMap.put("status", uiStatus);

        return uiRecordsMap;

    }


}
