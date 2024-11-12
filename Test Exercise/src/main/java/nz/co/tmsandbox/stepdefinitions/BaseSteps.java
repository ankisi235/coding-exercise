package nz.co.tmsandbox.stepdefinitions;

import io.cucumber.java8.En;

import java.io.File;

import static nz.co.tmsandbox.engine.IAccessor.accessResources;
import static nz.co.tmsandbox.engine.IAccessor.testDataAccessor;
import static nz.co.tmsandbox.webinteractivities.IWebActions.navigate;

public class BaseSteps implements En {
    public BaseSteps() {

        Given("^load \"([^\"]*)\" website$", (String testdataKey) -> {
            testDataAccessor.setActiveTestdataPage("LoadUrl");
            testDataAccessor.setActiveTestdataKey(testdataKey);
            navigate.navigateToURL("baseUrl");
            //TODO: validate and report
        });

        Given("^set the file location| for \"([^\"]*)\"$", (String businessUnit) -> {
            accessResources.setBusinessUnitUnderTest(businessUnit);
            testDataAccessor.setCommonTestDataFilesPath("src" + File.separator + "test" + File.separator + "features" + File.separator + "testdata");
            testDataAccessor.setbUTestDataFilesPath("src" + File.separator + "test" + File.separator + "features" + File.separator + businessUnit + File.separator + "testdata");
        });

    }
}
