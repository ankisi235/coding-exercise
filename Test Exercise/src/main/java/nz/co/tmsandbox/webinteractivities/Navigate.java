package nz.co.tmsandbox.webinteractivities;

import nz.co.tmsandbox.engine.IAccessor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Iterator;
import java.util.Set;

public class Navigate implements IWebActions {


    /**
     * Navigates the browser to the url which is in the test data file
     *
     * @param testdataVarName test data variable name for the url which is to be loaded
     *                        eg: trademe.baseUrl=https://www.tmsandbox.co.nz/
     *                        pass 'baseUrl', testdata key 'pega' will be set in the step definition
     */
    public void navigateToURL(String testdataVarName) {
        String urlVal = IAccessor.testDataAccessor.getTestDataValue(testdataVarName);
        try {
            new DriverFactory();
            DriverFactory.getDriver().navigate().to(urlVal);
            debugMessageLogger.logInformation("Navigated to url: <b>" + urlVal + "</b>", ReportCreator.StatusInReport.INFO);
        } catch (WebDriverException e) {
            refreshPage();
        } catch (Exception e) {
            debugMessageLogger.logException("Navigate to url", urlVal, e);
        }
    }

    /**
     * Refreshes the page
     */
    public void refreshPage() {
        DriverFactory.getDriver().navigate().refresh();
    }

}
