package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static org.testng.Assert.assertTrue;

public class LoginPage extends BasePage {

    //locators
    private Locator hawkeyeLogo = page.locator(".login-Logo");
    public Locator username = page.locator("#Input_UsernameVal");
    private Locator password = page.locator("#Input_PasswordVal");
    //private Locator loginButton = page.locator("//div[@class=\"login-button margin-top-xl\"]//button[@class=\"btn btn-primary btn-large login-Login-Button OSFillParent\"]");
    private Locator loginButton = page.locator(".login-Login-Button");
    private Locator errorMessages = page.locator(".validation-message");


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
        return hawkeyeLogo.isVisible();
    }

    public boolean isUsernameFieldVisible() {
        return username.isVisible();
    }

    public boolean isPasswordFieldVisible() {
        return password.isVisible();
    }

    public boolean isLoginButtonVisible() {
        return loginButton.isVisible();
    }

    public String getErrorAtPosition(int position) {
        return errorMessages.nth(position).textContent();
    }

}
