package nz.co.tmsandbox.utilities;

public class AccessResources {

    private String applicationUnderTest = "";
    private String businessUnitUnderTest = "";
    private String testTags = "";
    private String remoteUrl = "";
    private String currScenarioUnderTest = "";
    private static String environmentUnderTest = "test";
    private boolean headlessTrue = false;
    private boolean localExecution = true;
    private boolean rerunFailedScenarios = false;
    private String threads = "";
    private String url = "";

    public String getTestTags() {
        return testTags;
    }

    public void setTestTags(String testTags) {
        String ignoreTags = " ";
        if (!getUrl().contains("uat"))
            this.testTags = testTags;
        else
            this.testTags = testTags + ignoreTags;
    }

    public String getApplicationUnderTest() {
        return applicationUnderTest;
    }

    public void setApplicationUnderTest(String applicationUnderTest) {
        this.applicationUnderTest = applicationUnderTest;
    }

    public String getBusinessUnitUnderTest() {
        return businessUnitUnderTest;
    }

    public void setBusinessUnitUnderTest(String businessUnitUnderTest) {
        this.businessUnitUnderTest = businessUnitUnderTest;
    }

    public static String getEnvironmentUnderTest() {
        return environmentUnderTest;
    }

    public void setEnvironmentUnderTest(String environmentUnderTest) {
        AccessResources.environmentUnderTest = environmentUnderTest;
    }


    public void setHeadlessTrue(boolean headlessTrue) {
        this.headlessTrue = headlessTrue;
    }

    public boolean isHeadlessTrue() {
        return headlessTrue;
    }

    public void setLocalExecution(boolean localExecutionTrue) {
        this.localExecution = localExecutionTrue;
    }

    public boolean isLocalExecution() {
        return localExecution;
    }

    public void setRemoteUrlForRemoteExecution(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getRemoteUrlForRemoteExecution() {
        return remoteUrl;
    }

    public String getCurrScenarioUnderTest() {
        return currScenarioUnderTest;
    }

    public void setCurrScenarioUnderTest(String currScenarioUnderTest) {
        this.currScenarioUnderTest = currScenarioUnderTest;
    }
    public void setRerunFailedScenarios(boolean rerunFailedScenarios) {
        this.rerunFailedScenarios = rerunFailedScenarios;
    }

    public boolean getRerunFailedScenarios() {
        return rerunFailedScenarios;
    }

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
