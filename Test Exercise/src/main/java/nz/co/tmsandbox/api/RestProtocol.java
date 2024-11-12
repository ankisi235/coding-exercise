package nz.co.tmsandbox.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nz.co.tmsandbox.engine.IAccessor;
import nz.co.tmsandbox.webinteractivities.DebugMessageLogger;
import nz.co.tmsandbox.webinteractivities.ReportCreator;

public class RestProtocol {

    private String baseUri = "";
    private String endPoint = "";
    private Response apiResponse;

    private String getBaseUri() {
        return baseUri;
    }

    /*
     * Sets Base URI
     *
     * @param uri set the api uri, the value of the api uri is set from the testdata file,
     *            eg: car.baseUri=https://api.trademe.co.nz/v1
     **/


    public void setBaseUri(String uri) {
        try {
            this.baseUri = IAccessor.testDataAccessor.getTestDataValue(uri);
            DebugMessageLogger.debugMessageLogger.logInformation("Set base uri: " + this.baseUri, ReportCreator.StatusInReport.INFO);
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logException("Set base uri: " + this.baseUri, e);
        }
    }


    private String getEndPoint() {
        return endPoint;
    }

    /*
     * Sets the API end point
     *
     * @param apiEndPoint set the api endpoint,
     *                    this can be set directly by passing a string value*/


    public void setEndPoint(String apiEndPoint) {
        try {
            this.endPoint = apiEndPoint;
            DebugMessageLogger.debugMessageLogger.logInformation("Set api endpoint: " + this.endPoint, ReportCreator.StatusInReport.INFO);
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logException("Set api endpoint: " + this.endPoint, e);
        }
    }

    public Response getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(Response apiResponse) {
        this.apiResponse = apiResponse;
    }

    /* * Performs an API get operation*/

    public void performGetOperation() {
        try {
            DebugMessageLogger.debugMessageLogger.logInformation("Get url: " + getBaseUri() + getEndPoint());
            RequestSpecification request = apiTriggerSetup();
            setApiResponse(request.get(getBaseUri() + getEndPoint()));
            DebugMessageLogger.debugMessageLogger.logInformation("Perform get api operation", ReportCreator.StatusInReport.INFO);
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logException("Exception in performing get api operation", e);
        }
    }

    private RequestSpecification apiTriggerSetup() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(getBaseUri());
        RequestSpecification requestSpecification = requestSpecBuilder.build();
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.spec(requestSpecification);
        return request;
    }
}
