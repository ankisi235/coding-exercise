package nz.co.tmsandbox.webinteractivities;

import io.cucumber.datatable.DataTable;
import org.openqa.selenium.StaleElementReferenceException;

import java.util.List;
import java.util.function.Predicate;

public class Get implements IWebActions {
    /**
     * Gets the text of
     *
     * @param elementName web element name
     * @return text of web element passed
     */
    public String getTextOf(String elementName) {
        String value = "";
        Predicate<String> stringLen = s -> s.length() == 0;
        while (stringLen.test(value)) {
            try {
                value = webElementsAccessor.getWebElement(elementName).getText();
                value = (value.isEmpty()) ? webElementsAccessor.getWebElement(elementName).getAttribute("value") : value;
                break;
            } catch (StaleElementReferenceException e) {
                //debugMessageLogger.logInformation(e.getClass().getSimpleName(), ReportCreator.StatusInReport.INFO);
            } catch (Exception e) {
                debugMessageLogger.logException("Get text of web element", webElementsAccessor.getActiveElementName(), webElementsAccessor.getActiveElementFindbyWithValue(), e);
                break;
            }
        }
        if (value != null)
            debugMessageLogger.logInformation("Got text of web element " + value);
        return value;
    }


    /**
     * Gets the current url
     *
     * @return current url
     */
    public String getCurrentUrl() {
        String currentUrl = "";
        try {
            currentUrl = DriverFactory.getDriver().getCurrentUrl();
            if (currentUrl != "") {
                debugMessageLogger.logInformation("Current Url: " + currentUrl);
            } else {
                debugMessageLogger.logError("Current Url not returned: " + currentUrl);
            }
        } catch (Exception e) {
            debugMessageLogger.logException("Get Current Url", e);
        } finally {
            return currentUrl;
        }
    }

    public String getValueFromDataTable(DataTable dataTable, int row, int col) {
        try {
            List<List<String>> map = dataTable.asLists(String.class);
            if (!map.get(row).get(col).isEmpty())
                return map.get(row).get(col);
        } catch (Exception e) {
        }
        return null;
    }

}
