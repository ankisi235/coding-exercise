package nz.co.tmsandbox.webinteractivities;


import nz.co.tmsandbox.engine.IAccessor;
import org.openqa.selenium.StaleElementReferenceException;

import java.util.NoSuchElementException;

public class Select implements IWebActions {

    /**
     * Selects the text from the dropdown, text passed in the option
     *
     * @param option         text to be selected from the dropdown
     * @param webElementName dropdown web element name
     */
    public Select valueFromDropdown(String option, String webElementName) {
        try {
            while (true) {
                try {
                    IAccessor.webElementsAccessor.waitForClick(webElementName);
                    option = (option.isEmpty()) ? new org.openqa.selenium.support.ui.Select(IAccessor.webElementsAccessor.getWebElement(webElementName)).getOptions().get(1).getText() : option;
                    new org.openqa.selenium.support.ui.Select(IAccessor.webElementsAccessor.getWebElement(webElementName)).selectByVisibleText(option);
                    //debugMessageLogger.logInformation("Selected variable value from drop down- <b>" + option + "</b>", ReportCreator.StatusInReport.INFO);
                    try {
                        while (!new org.openqa.selenium.support.ui.Select(IAccessor.webElementsAccessor.getWebElement(webElementName)).getFirstSelectedOption().getText().equals(option.trim())) {
                            new org.openqa.selenium.support.ui.Select(IAccessor.webElementsAccessor.getWebElement(webElementName)).selectByVisibleText(option.trim());
                        }
                    } catch (NoSuchElementException ignored) {
                    }
                    break;
                } catch (StaleElementReferenceException e) {
                    debugMessageLogger.logInformation(e.getClass().getSimpleName());
                }
            }
        } catch (Exception e) {
            debugMessageLogger.logException("Select variable value from drop down", option, IAccessor.webElementsAccessor.getActiveElementName(), IAccessor.webElementsAccessor.getActiveElementFindbyWithValue(), e);
        }
        return this;
    }

}
