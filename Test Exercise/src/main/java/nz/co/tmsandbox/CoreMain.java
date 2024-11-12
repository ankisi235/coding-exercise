package nz.co.tmsandbox;

import nz.co.tmsandbox.engine.IAccessor;
import nz.co.tmsandbox.engine.SystemParameters;
import nz.co.tmsandbox.webinteractivities.ReportCreator;
import io.cucumber.core.cli.Main;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(Cucumber.class)
public class CoreMain implements IAccessor {
    static String featureFilesPath;
    static String cucumberStepDefGluePackage;

    private static String getCucumberStepDefGluePackage() {
        return cucumberStepDefGluePackage;
    }

    public static void setCucumberStepDefGluePackage(String cucumberStepDefGluePackage) {
        CoreMain.cucumberStepDefGluePackage = cucumberStepDefGluePackage;
    }

    private static String getFeatureFilesPath() {
        return featureFilesPath;
    }

    public static void setFeatureFilesPath(String featureFilesPath) {
        CoreMain.featureFilesPath = featureFilesPath;
    }

    public static void main(String[] args) {
        SystemParameters.systemParameters(args);
        ExecuteTests();
    }

    private static void ExecuteTests() {
        if (IAccessor.accessResources.isLocalExecution())
            Main.run(new String[]{"-g", getCucumberStepDefGluePackage(), "-p", "json:target/cucumber.json", "-p", "rerun:target/rerun.txt", getFeatureFilesPath(), "-t",
                    IAccessor.accessResources.getTestTags(), "-t", "not @WIP"}, Thread.currentThread().getContextClassLoader());
        else
            Main.run(new String[]{"--threads", IAccessor.accessResources.getThreads(), "-g", getCucumberStepDefGluePackage(),
                    "-p", "json:target/cucumber.json", "-p", "rerun:target/rerun.txt", getFeatureFilesPath(), "-t", IAccessor.accessResources.getTestTags()}, Thread.currentThread().getContextClassLoader());

        if (IAccessor.accessResources.getRerunFailedScenarios() && IAccessor.ioOperations.getFileSize("." + File.separator + "target" + File.separator + "rerun.txt") != 0) {
            IAccessor.ioOperations.deleteFile("." + File.separator + "log" + File.separator + "debug.log");
            IAccessor.ioOperations.deleteFolder("." + File.separator + "screenshots");
            IAccessor.ioOperations.deleteFolder("." + File.separator + "extentreport");
            ReportCreator.initialisefailedReports();//new extent report for rerun
            Main.run(new String[]{"-g", getCucumberStepDefGluePackage(),
                    "-p", "json:targetRerun/cucumber.json", "@target/rerun.txt"}, Thread.currentThread().getContextClassLoader());
        }
    }
}


