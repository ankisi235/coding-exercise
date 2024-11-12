package nz.co.tmsandbox.webinteractivities;

import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.StaleElementReferenceException;

public class Set implements IWebActions {

    /**
     * Enters value in the text box
     *
     * @param val            value to be entered
     * @param webElementName web element name
     * @return
     */
    public Set enterValueInTextbox(String val, String webElementName) {
        if (val == null) {
            debugMessageLogger.logInformation("Value is null", webElementsAccessor.getActiveElementName(), webElementsAccessor.getActiveElementFindbyWithValue());
            return this;
        }
        try {
            while (true) {
                try {
                    //webElementsAccessor.waitUntilElementEnabled(webElementName);
                    webElementsAccessor.waitForClick(webElementName);
                    webElementsAccessor.getWebElement(webElementName).clear();
                    webElementsAccessor.getWebElement(webElementName).click();
                    webElementsAccessor.getWebElement(webElementName).sendKeys(val);
                    String text = webElementsAccessor.getWebElement(webElementName).getAttribute("value");
                    String txt = get.getTextOf(webElementName);
                    if ((text != null && text.equals(val)) || (txt.equals(val)) || (txt.contains(","))) {
                        debugMessageLogger.logInformation("Entered variable value in text box = " + val);
                    } else {
                        webElementsAccessor.getWebElement(webElementName).sendKeys(val);
                        debugMessageLogger.logInformation("Entered variable value again in text box = " + val);
                    }
                    break;
                } catch (StaleElementReferenceException ignored) {

                }
            }
        } catch (ElementNotInteractableException e) {
            webElementsAccessor.scrollToViewAndClick(webElementName);
            webElementsAccessor.getWebElement(webElementName).sendKeys(val);
            debugMessageLogger.logInformation(e.getClass().getSimpleName(), val, webElementsAccessor.getActiveElementName(), webElementsAccessor.getActiveElementFindbyWithValue());
        } catch (IllegalArgumentException e) {
            debugMessageLogger.logInformation(e.getClass().getSimpleName(), val, webElementsAccessor.getActiveElementName(), webElementsAccessor.getActiveElementFindbyWithValue());
        } catch (Exception e) {
            debugMessageLogger.logException("Enter variable value in text box", val, webElementsAccessor.getActiveElementName(), webElementsAccessor.getActiveElementFindbyWithValue(), e);
        }
        return this;
    }

}
