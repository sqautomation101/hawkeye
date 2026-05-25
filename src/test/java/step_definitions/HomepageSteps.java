package step_definitions;

import base.ScenarioContext;
import pages.Homepage;

import static org.testng.Assert.assertTrue;

public class HomepageSteps {

    private Homepage homepage;

    // PicoContainer passes ScenarioContext automatically
    public HomepageSteps(ScenarioContext scenarioContext) {
        this.homepage = scenarioContext.getPageManager().getHomepage();
    }


}
