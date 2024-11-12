package nz.co.tmsandbox.stepdefinitions;

import nz.co.tmsandbox.engine.IAccessor;
import nz.co.tmsandbox.webinteractivities.DriverFactory;
import nz.co.tmsandbox.webinteractivities.IWebActions;
import nz.co.tmsandbox.webinteractivities.ReportCreator;
import io.cucumber.java8.En;
import io.cucumber.java8.Scenario;


public class Hook implements En, IAccessor, IWebActions {

    public static Scenario currScenario;
    public String scenarioStartTime = "";
    public String scenarioEndTime = "";

    public Hook() {
        Before((Scenario scenario) -> {
            currScenario = scenario;
            IAccessor.accessResources.setCurrScenarioUnderTest(currScenario.getName());
            IWebActions.debugMessageLogger.logInformation("Testing Feature: " + scenario.getId());
            IWebActions.debugMessageLogger.logInformation("Testing scenario: " + scenario.getName());
            IWebActions.debugMessageLogger.logInformation(scenario.getId());
            IWebActions.debugMessageLogger.setExceptionFlag(false);
            scenarioStartTime = IAccessor.dateAndTime.createDateTime("now+0d", "dd-MM-yyyy hh:mm:ss");
            IWebActions.debugMessageLogger.logInformation("Scenario start time: " + scenarioStartTime);
            if (ReportCreator.extentReports == null)
                ReportCreator.initialiseReports();
            if (ReportCreator.rerun == null)
                ReportCreator.createTest(scenario.getName());
            else ReportCreator.createTest("RE-RUN " + scenario.getName());
        });
        After(() -> {
            IWebActions.debugMessageLogger.logInformation("Tested scenario: " + currScenario.getName());
            IWebActions.debugMessageLogger.logInformation("Status of Exception Flag: " + IWebActions.debugMessageLogger.isExceptionFlag());
            IWebActions.debugMessageLogger.logInformation("Status: " + currScenario.getStatus() + "\n");
            if (currScenario.getStatus().toString().equals("FAILED")) {
                ReportCreator.logInReport(ReportCreator.StatusInReport.FAIL, currScenario.getName());
            }
            scenarioEndTime = IAccessor.dateAndTime.createDateTime("now+0d", "dd-MM-yyyy hh:mm:ss");
            IWebActions.debugMessageLogger.logInformation("Scenario end time: " + scenarioEndTime);
            ReportCreator.flushReport();
            if (DriverFactory.getDriver() != null)
                new DriverFactory().destroyDriver();
        });
    }
}
