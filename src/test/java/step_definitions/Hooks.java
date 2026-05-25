package step_definitions;

import base.ScenarioContext;
import com.microsoft.playwright.Page;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    private final ScenarioContext scenarioContext;

    // Cucumber automatically passes the same ScenarioContext here
    // and into LoginSteps — they share the same instance
    public Hooks(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("Starting: " + scenario.getName());
        // browser is already open at this point
        // ScenarioContext constructor ran when Cucumber created the object
    }
    /*
    @After
    public void tearDown(Scenario scenario) {
        System.out.println("Finished: " + scenario.getName());
        scenarioContext.teardown(); // close browser, context, playwright
    }
    */

    @After
    public void tearDown(Scenario scenario) {
        // take screenshot if scenario fails
        if (scenario.isFailed()) {
            // capture screenshot as bytes
            byte[] screenshot = scenarioContext
                    .getPage()
                    .screenshot(new Page.ScreenshotOptions().setFullPage(true));

            // attach screenshot to the report
            scenario.attach(screenshot, "image/png", scenario.getName());
        }

        System.out.println("Finished: " + scenario.getName());
        scenarioContext.teardown();
    }
}