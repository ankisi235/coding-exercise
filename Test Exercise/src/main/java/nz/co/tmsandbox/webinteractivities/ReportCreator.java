package nz.co.tmsandbox.webinteractivities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.awt.*;
import java.io.File;

public class ReportCreator {

    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    public static ExtentReports extentReports;
    public static ExtentSparkReporter rerun = null;
    public static ExtentSparkReporter extentHtmlReporter;
    public static final ThreadLocal<Integer> warning = ThreadLocal.withInitial(() -> 0);


    public static void initialiseReports() {
        try {
            extentReports = new ExtentReports();
            String reportLoc = "." + File.separator + "extentreport";
            File directory = new File(reportLoc);
            if (!directory.exists())
                directory.mkdir();
            extentHtmlReporter = new ExtentSparkReporter("." + File.separator + "extentreport" + File.separator + "Report.html");
            extentReports.attachReporter(extentHtmlReporter);
            extentHtmlReporter.config().setDocumentTitle("Pega BDD Test Automation");
            extentHtmlReporter.config().setReportName("Pega Testing");
            extentHtmlReporter.config().setTheme(Theme.STANDARD);
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logInformation("Error in initialiseReports" + e.getClass().getSimpleName());
        }

    }

    public static void initialisefailedReports() {
        try {
            rerun = new ExtentSparkReporter("." + File.separator + "extentreport" + File.separator + "RetryReport.html");
            extentReports.attachReporter(extentHtmlReporter, rerun);
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logException("Error in initialisefailedReports", e);
        }
    }

    public enum StatusInReport {
        PASS,
        FAIL,
        INFO,
        WARNING,
        SKIP
    }
    public static void logInReport(StatusInReport status, String msg) {
        switch (status) {
            case PASS -> test.get().log(Status.PASS, msg);
            case FAIL -> test.get().log(Status.FAIL, msg, MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(), msg + ".png").build());
            case INFO -> test.get().log(Status.INFO, msg);
            case WARNING -> {
                test.get().log(Status.WARNING, msg).addScreenCaptureFromBase64String(takeScreenshot());
                warning.set(warning.get() + 1);
            }
            case SKIP -> test.get().log(Status.SKIP, msg);
            default ->
                    DebugMessageLogger.debugMessageLogger.logInformation("Enter the correct status for Extent Reporting !!");
        }
    }

    public static String takeScreenshot() {
        return ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    public static void createTest(String createTestDesc) {
        try {
            test.set(extentReports.createTest(createTestDesc));
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logException("Error in createTest", e);
        }
    }

    public static void flushReport() {
        try {
            extentReports.flush();
            Desktop.getDesktop().open(new File("extentreport//Report.html"));
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logException("Error in flushReport", e);
        }

    }
}

