package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static org.testng.Assert.assertTrue;

public class Homepage extends BasePage {

    public Homepage (Page page) {
        super(page);
    }

    private Locator requestHeader = page.getByText("RequestNew Request");
    private Locator logoutButton = page.getByText("Logout");
    private Locator sidePanel = page.locator("#b1-SidebarContainer i");


    //move this to homepage
    public void logout() {
        //hover(sidePanel);
        //sidePanel.waitFor();
        //forceClick(logoutButton);
        page.evaluate("document.querySelector('.fa-power-off').click()");
    }

    public boolean isRequestHeaderVisible() {
        requestHeader.waitFor(); // wait for it to appear first
        return requestHeader.isVisible();
    }

    public void iAmOnTheHomepage() {
        assertTrue(isRequestHeaderVisible(), "User is not in the Homepage");
    }
}
