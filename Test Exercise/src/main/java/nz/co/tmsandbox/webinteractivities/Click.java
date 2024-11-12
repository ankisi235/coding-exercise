package nz.co.tmsandbox.webinteractivities;


import nz.co.tmsandbox.engine.IAccessor;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnhandledAlertException;

public class Click implements IWebActions {
    /**
     * Clicks a webelement
     *
     * @param elementName name
     * @param elementType type
     */
    public void clicks(String elementName, String elementType) {
        try {
            while (true) {
                try {
                    IAccessor.webElementsAccessor.waitForClick(elementName);
                    IAccessor.webElementsAccessor.getWebElement(elementName).click();
                    //debugMessageLogger.logInformation("Clicked " + elementName, ReportCreator.StatusInReport.INFO);
                    break;
                } catch (StaleElementReferenceException ignored) {
                }
            }
        } catch (ElementNotInteractableException e) {
            IAccessor.webElementsAccessor.getWebElement(elementName);
            IAccessor.webElementsAccessor.scrollToViewAndClick(elementName);
        } catch (UnhandledAlertException ignored) {

        } catch (Exception e) {
            debugMessageLogger.logException("Click  " + elementType + " " + IAccessor.webElementsAccessor.getActiveElementName() + IAccessor.webElementsAccessor.getActiveElementFindbyWithValue(), e);
        }
    }


    /**
     * Clicks a button
     *
     * @param buttonName button name
     */
    public Click button(String buttonName) {
        clicks(buttonName, "button");
        return this;
    }


}
