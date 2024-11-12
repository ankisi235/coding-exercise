package nz.co.tmsandbox.webinteractivities;

import nz.co.tmsandbox.engine.IAccessor;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Screenshots implements IWebActions {

    private String setCurrStepName = "";

    private String createScreenshotsFolder() {
        try {
            File screenshotDir = new File("." + File.separator + "screenshots");
            if (!screenshotDir.exists())
                screenshotDir.mkdir();
        } catch (Exception e) {
            debugMessageLogger.logException("Exception occurred in createScreenshotsFolder", e);
        }
        return "." + File.separator + "screenshots" + File.separator;
    }

    private String createPassedScreenshotsFolder() {
        File passedScreenshotDir = new File(createScreenshotsFolder() + "passed");
        if (!passedScreenshotDir.exists())
            passedScreenshotDir.mkdir();
        return "." + File.separator + "screenshots" + File.separator + "passed" + File.separator;
    }

    private String createFailedScreenshotsFolder() {
        try {
            File failedScreenshotDir = new File(createScreenshotsFolder() + "failed");
            if (!failedScreenshotDir.exists())
                failedScreenshotDir.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "." + File.separator + "screenshots" + File.separator + "failed" + File.separator;
    }

    private String getScreenshotFile(boolean passed) {
        String folderNm;
        if (passed)
            folderNm = createPassedScreenshotsFolder();
        else
            folderNm = createFailedScreenshotsFolder();
        return folderNm + IAccessor.accessResources.getCurrScenarioUnderTest() + "_" + setCurrStepName + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss.ss")) + ".jpg";
    }

    private void captureScreen(boolean passed) {
        try {
            File screenShot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenShot, new File(getScreenshotFile(passed)));
        } catch (Exception e) {
            debugMessageLogger.logException("Exception occurred while taking screenshot", e);
        }
    }

    /**
     * Takes a screenshot when scenario fails
     */
    public void takeScreenShotFailedScenario() {
        captureScreen(false);
    }

}