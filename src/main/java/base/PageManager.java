package base;

import com.microsoft.playwright.Page;
import pages.*;

public class PageManager {
    private Page page;
    private LoginPage loginPage;
    private Homepage homepage;
    private TargetingRequestPage targetingRequestPage;

    //Receive the page created and passed by ScenarioContext
    public PageManager (Page page){
        this.page = page;
    }


    // This method ensures the page is only created when called
    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(page); // creates LoginPage and passes the page into it
        }
        return loginPage;
    }

    public Homepage getHomepage() {
        if (homepage == null) {
            homepage = new Homepage(page);
        }
        return homepage;
    }

    public TargetingRequestPage getTargetingRequestPage() {
        if (targetingRequestPage == null) {
            targetingRequestPage = new TargetingRequestPage(page);
        }
        return targetingRequestPage;
    }
}
