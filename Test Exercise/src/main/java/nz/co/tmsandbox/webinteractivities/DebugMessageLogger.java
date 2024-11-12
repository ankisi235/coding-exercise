package nz.co.tmsandbox.webinteractivities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

public class DebugMessageLogger implements IWebActions {

    Logger logger =LogManager.getLogger(DebugMessageLogger.class);
    Boolean exceptionFlag = false;

    public boolean isExceptionFlag() {
        return exceptionFlag;
    }

    public void setExceptionFlag(boolean exceptionflag) {
        exceptionFlag = exceptionflag;
    }

    public void logInformation(String msg, ReportCreator.StatusInReport status) {
        try {
            logger.info(msg);
            ReportCreator.logInReport(status, msg);
            //ReportCreator.logInReportWithScreenshot(status,msg,msg);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void logInformation(String msg) {
        try {
            logger.info(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logInformation(String msg, String webElement, String webElementValue) {
        try {
            String logMsg = msg + ": " + webElement + "(" + webElementValue + ")";
            logger.info(logMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logInformation(String msg, String testDataValue, String webElementName, String webElementValue) {
        try {
            String logMsg = msg + ": " + testDataValue + ": " + webElementName + "(" + webElementValue + ")";
            logger.info(logMsg);
            ReportCreator.logInReport(ReportCreator.StatusInReport.INFO, logMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logException(String msg, Exception e) {
        try {
            exceptionFlag = true;
            logger.error(msg, e);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void logError(String msg) {
        try {
            exceptionFlag = true;
            logger.error(msg);
            ReportCreator.logInReport(ReportCreator.StatusInReport.FAIL, msg);
            new Screenshots().takeScreenShotFailedScenario();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logFailure(String msg, String value) throws RuntimeException {
        exceptionFlag = true;
        String logMsg = msg + ": " + value;
        logger.error("Exception: Failed at: " + logMsg);
        new Screenshots().takeScreenShotFailedScenario();
        ReportCreator.logInReport(ReportCreator.StatusInReport.FAIL, logMsg);
        Assert.fail();
    }

    public void logException(String msg, String value, Exception e) throws RuntimeException {
        exceptionFlag = true;
        String logMsg = msg + ": " + value;
        logger.error("Exception: Failed at: " + logMsg, e);
        new Screenshots().takeScreenShotFailedScenario();
        ReportCreator.logInReport(ReportCreator.StatusInReport.FAIL, logMsg);
        Assert.fail();
    }

    public void logException(String msg, String webElementName, String webElementValue, Exception e) {
        exceptionFlag = true;
        String logMsg = msg + "; " + webElementName + "(" + webElementValue + ")";
        logger.error("Exception: Failed at: " + logMsg, e);
        new Screenshots().takeScreenShotFailedScenario();
        ReportCreator.logInReport(ReportCreator.StatusInReport.FAIL, logMsg);
        Assert.fail();
    }

    public void logException(String msg, String testDataValue, String webElementName, String webElementValue, Exception e) {
        exceptionFlag = true;
        String logMsg = msg + ": " + testDataValue + ": " + webElementName + "(" + webElementValue + ")";
        logger.error("Exception: Failed at: " + logMsg, e);
        new Screenshots().takeScreenShotFailedScenario();
        ReportCreator.logInReport(ReportCreator.StatusInReport.FAIL, logMsg);
        Assert.fail();
    }

    public void logError(String msg, String webElementName, String webElementValue) {
        try {
            exceptionFlag = true;
            String LogMsg = msg + ": " + webElementName + "(" + webElementValue + ") ";
            logger.error("Error: Failed at: " + LogMsg);
            new Screenshots().takeScreenShotFailedScenario();
            ReportCreator.logInReport(ReportCreator.StatusInReport.FAIL, LogMsg);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}



