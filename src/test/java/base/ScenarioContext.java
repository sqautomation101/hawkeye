package base;

import com.microsoft.playwright.*;
import org.testng.annotations.*;

import java.util.List;

public class ScenarioContext {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected PageManager pageManager;

    // This runs once when Cucumber creates this object (before each scenario)
    public ScenarioContext(){

        playwright = Playwright.create(); //start engine

        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                    .setHeadless(false)
                    .setArgs(List.of("--start-maximized"))
        );

        context = browser.newContext(); //fresh session
        page = context.newPage(); //create a PAGE (TAB) to be passed to PageManager
        pageManager = new PageManager(page); //pass the page you created into PageManager

    }
    // Hooks will call this to get the PageManager
    public PageManager getPageManager() {
        return pageManager;
    }

    // ScenarioContext.java — add this method
    public Page getPage() {
        return page;
    }

    // Hooks will call this after each scenario to clean up
    public void teardown () {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

}
