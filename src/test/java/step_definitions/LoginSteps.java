package step_definitions;

import base.ScenarioContext;
import com.microsoft.playwright.Locator;
import pages.Homepage;
import pages.LoginPage;
import io.cucumber.java.en.*;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LoginSteps
{

    private LoginPage loginPage;
    private Homepage homepage;

    // PicoContainer passes ScenarioContext automatically
    public LoginSteps(ScenarioContext scenarioContext) {
        this.loginPage = scenarioContext.getPageManager().getLoginPage();
        this.homepage = scenarioContext.getPageManager().getHomepage();
    }


    @Given("the user navigates to Hawkeye Login Page")
    public void iAmOnTheLoginPage() {
        loginPage.navigate("https://awsuat-outsys.smadvantage.com/Hawkeye/Login");
    }

    @When("the user enters a username {string} and password {string}")
    public void iEnterUsernameAndPassword(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("the user should be redirected to the Hawkeye Homepage")
    public void iShouldBeLoggedInSuccessfully() {
        homepage.isRequestHeaderVisible();
    }

    @Then("the Login page elements should be visible")
    public void theLoginPageElementShouldBeVisible() {
        assertTrue(loginPage.isLogoVisible(), "Hawkeye logo is missing");
        assertTrue(loginPage.isUsernameFieldVisible(), "Username field is missing");
        assertTrue(loginPage.isPasswordFieldVisible(), "Password field is missing");
        assertTrue(loginPage.isLoginButtonVisible(), "Username field is missing");
    }

    @Then("the error {string} should appear at {}")
    public void theErrorMessageShouldAppearAtPosition(String expectedMessage, int position) {
        String actualMessage = loginPage.getErrorAtPosition(position);
        assertEquals(actualMessage, expectedMessage, "Incorrect error message");
    }

    @When("the user logs in with credentials from {string}")
    public void theUserLogsInWithCredentialsFrom(String csvFilename) throws Exception {

        String filepath = "src\\test\\resources\\testdata\\" + csvFilename;

        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line;
        boolean isFirstline = true;

        while ((line = reader.readLine()) != null) {

            if (isFirstline) {
                isFirstline = false;
                continue;
            }

            String[] data = line.split(",");
            String username = data[0];
            String password = data[1];

            loginPage.login(username, password);

            homepage.iAmOnTheHomepage();

            //homepage.page.pause();

            homepage.logout();

            iAmOnTheLoginPage();

            System.out.println("Login successful for user: " + username);

        }
        reader.close();


    }

}

