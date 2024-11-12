package nz.co.tmsandbox.stepdefinitions;

import io.cucumber.java8.En;
import nz.co.tmsandbox.api.IApiActions;
import nz.co.tmsandbox.webinteractivities.DebugMessageLogger;
import nz.co.tmsandbox.webinteractivities.IWebActions;

import java.util.List;

import static nz.co.tmsandbox.engine.IAccessor.testDataAccessor;

public class ApiSteps implements En {

    String numberOfUniqCars;
    String endPoint = "/Categories/UsedCars.json";

    public ApiSteps() {

        When("^get the number of the named \"([^\"]*)\" brands available$", (String testdataKey) -> {
            testDataAccessor.setActiveTestdataPage("LoadUrl");
            testDataAccessor.setActiveTestdataKey(testdataKey);
            IApiActions.restProtocol.setBaseUri("baseUri");
            IApiActions.restProtocol.setEndPoint(endPoint);

            IApiActions.restProtocol.performGetOperation();

            // Check if the response was successful
            if (IApiActions.restProtocol.getApiResponse().getStatusCode() == 200) {
                // Extract the list of car names using JsonPath
                List<String> carNames = IApiActions.restProtocol.getApiResponse().jsonPath().getList("Subcategories.Name");

                // Count the number of unique car names
                numberOfUniqCars = String.valueOf(carNames.stream().distinct().toList().size());

            } else {
                DebugMessageLogger.debugMessageLogger.logInformation("Failed to fetch the data. Status code: " + IApiActions.restProtocol.getApiResponse().getStatusCode());
            }
        });


        Then("^verify the count of unique cars is \"([^\"]*)\"$", (String numberOfUniqueCars) -> {
            IWebActions.validate.assertEquals(numberOfUniqueCars, numberOfUniqCars, "Total number of unique car names");
        });


    }
}
