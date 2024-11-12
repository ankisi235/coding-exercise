package nz.co.tmsandbox.webinteractivities;

import nz.co.tmsandbox.engine.IAccessor;
import org.junit.Assert;

public class Validate implements IWebActions {


    /**
     * Verifies that the element value is equal to the expected passed value
     *
     * @param expected expected value
     * @param actual   actual value
     */
    public void assertEquals(String expected, String actual, String text) {
        try {
            Assert.assertNotNull(actual);
            Assert.assertEquals(expected, actual);
            debugMessageLogger.logInformation("<b>" + text + "</b> - " + "Actual value:: " + "<font color=green>" + actual + "</font>" + " ::Expected value::" + expected, ReportCreator.StatusInReport.PASS);
        } catch (AssertionError e) {
            debugMessageLogger.logInformation("<b>" + text + "</b> - " + "Actual value:: " + "<font color=red><b>" + actual + "</b></font>" + " is not equal ::Expected value::" + expected, ReportCreator.StatusInReport.WARNING);
        } catch (Exception e) {
            debugMessageLogger.logException(IAccessor.webElementsAccessor.getActiveElementName() + IAccessor.webElementsAccessor.getActiveElementFindbyWithValue(), e);
        }
    }

    /**
     * Verifies that the element value contains the expected passed value
     *
     * @param expected expected value
     * @param actual   actual value
     */
    public void contains(String expected, String actual, String text) {
        try {
            if (actual.contains(expected))
                debugMessageLogger.logInformation("<b>" + text + "</b> - " + "Actual value:: " + "<font color=green><b>" + actual + "</b></font>" + "::Expected value::" + expected, ReportCreator.StatusInReport.PASS);
            else
                debugMessageLogger.logError("<b>" + text + "</b> - " + "Actual value:: " + "<font color=red><b>" + actual + "</b></font>" + "does not contain ::Expected value::" + expected);
        } catch (Exception e) {
            debugMessageLogger.logException(text + " error " + expected, IAccessor.webElementsAccessor.getActiveElementName(), IAccessor.webElementsAccessor.getActiveElementFindbyWithValue(), e);
        }
    }

    /**
     * Verifies that the element is present
     *
     * @param elementName element name
     */
    public void isElementPresentOnPage(String elementName) {
        boolean elementPresent = false;
        try {
            if (IAccessor.webElementsAccessor.getWebElement(elementName).isDisplayed())
                elementPresent = true;
            if (elementPresent)
                debugMessageLogger.logInformation("Element present on page: " + true, ReportCreator.StatusInReport.PASS);
            else
                debugMessageLogger.logError("Element not present on page: " + false, IAccessor.webElementsAccessor.getActiveElementName(), IAccessor.webElementsAccessor.getActiveElementFindbyWithValue());
        } catch (Exception e) {
            debugMessageLogger.logException("Element not present on page: " + elementPresent, IAccessor.webElementsAccessor.getActiveElementName(), IAccessor.webElementsAccessor.getActiveElementFindbyWithValue(), e);
        }
        Assert.assertTrue(elementPresent);
    }


}

