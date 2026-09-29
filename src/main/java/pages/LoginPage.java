package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static org.testng.Assert.assertTrue;

public class LoginPage extends BasePage {

    //locators
    private Locator hawkeyeLogo = page.locator("img.login-Logo");
    public Locator username = page.getByPlaceholder("ADID");
    private Locator password = page.getByPlaceholder("Password");
    private Locator loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));

    //receives the page and passes it up to the BasePage that's why super. BasePage now uses this.
    public LoginPage(Page page) {
        super(page);
    }

    // types the credentials and clicks login
    public void login(String user, String pass) {
        type(username, user);
        type(password, pass);
        forceClick(loginButton);   // move click back here
    }

    // checks the Login page content
    public boolean isLogoVisible() {
        waitForElement(hawkeyeLogo);
        return hawkeyeLogo.isVisible();
    }

    public boolean isUsernameFieldVisible() {
        waitForElement(username);
        return username.isVisible();
    }

    public boolean isPasswordFieldVisible() {
        waitForElement(password);
        return password.isVisible();
    }

    public boolean isLoginButtonVisible() {
        waitForElement(loginButton);
        return loginButton.isVisible();
    }

    public String getErrorMessage(String fieldName) {
        String inputClass = fieldName.equalsIgnoreCase("username") ? "input-text" : "input-password";

        Locator error = page.locator("span." + inputClass + " span.validation-message");

        return error.textContent();
    }

}
