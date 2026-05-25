package base;

import com.microsoft.playwright.*;

public class BasePage {
    public Page page; // Page is a Playwright interface that represents one browser tab.
    // It gives us access to everything happening on that tab.

    // Constructor — every class that extends BasePage needs to pass in a Page object.
    // We store it as this.page so all the methods below can use it.
    // "this.page" refers to the Page belonging to THIS class.


    //the page stops here - BasePage stores it
    //this.page will ensure that the methods below can use it
    public BasePage(Page page) {
        this.page = page;
    }

    // Clicks on a given element on the page.
    // Locator is Playwright's way of pointing to a specific element (like a button or link).
    public void click(Locator locator) {
        locator.click();
    }

    // Types a value into a given input field.
    // fill() clears the field first, then types the value — safer than typing character by character.
    public void type(Locator locator, String value) {
        locator.fill(value);
    }


    public void hover(Locator locator) {
        locator.hover();
    }

    // Returns the visible text content of a given element.
    // Useful for reading labels, messages, or any text on the page.
    public String getText(Locator locator) {
        return locator.textContent();
    }

    // Navigates the browser tab to the given URL.
    // This is called on page (the tab), not on a locator (an element).
    public void navigate(String url) {
        page.navigate(url);
    }

    // Waits for an element to appear on the page before continuing.
    // Useful when a page is still loading and the element isn't visible yet.
    public void waitForElement(Locator locator) {
        locator.waitFor();
    }

    // Forces a click by dispatching the event directly to the element.
    // Used when a normal click() fails because another element (like an overlay or div)
    // is sitting on top of the button and intercepting the click.
    public void forceClick(Locator locator) {
        locator.dispatchEvent("click");
    }


}