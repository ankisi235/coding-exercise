package nz.co.tmsandbox.engine;

import nz.co.tmsandbox.utilities.AccessResources;
import nz.co.tmsandbox.webinteractivities.IWebActions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class TestDataAccessor {
    public String file;
    public String key;
    public String env;
    public String name;
    public String value;

    //    public String folder = ""; // for future use
    public TestDataAccessor(String testDataFile, String key, String env, String name, String value) {
        this.file = testDataFile;
        this.key = key;
        this.env = env;
        this.name = name;
        this.value = value;
    }

    public TestDataAccessor() {
    }

    static final ThreadLocal<List<TestDataAccessor>> completeTestDataList = ThreadLocal.withInitial(ArrayList::new);
    static final ThreadLocal<String> activeTestdataPage = ThreadLocal.withInitial(() -> "");
    static final ThreadLocal<String> activeTestdataKey = ThreadLocal.withInitial(() -> "");
    static final ThreadLocal<String> commonTestDataFilesPath = ThreadLocal.withInitial(() -> "");
    static final ThreadLocal<String> bUTestDataFilesPath = ThreadLocal.withInitial(() -> "");

    public static String getCommonTestDataFilesPath() {
        return commonTestDataFilesPath.get();
    }

    public void setCommonTestDataFilesPath(String commonTestDataFilesPath) {
        TestDataAccessor.commonTestDataFilesPath.set(commonTestDataFilesPath);
    }

    public String getBuTestDataFilesPath() {
        return bUTestDataFilesPath.get();
    }

    public void setbUTestDataFilesPath(String bUTestDataFilesPath) {
        TestDataAccessor.bUTestDataFilesPath.set(bUTestDataFilesPath);
        if (completeTestDataList.get().size() != 0) {
            completeTestDataList.get().clear();
        }
        readAndLoadTestdata();
    }

    private String getActiveTestdataKey() {
        return activeTestdataKey.get();
    }

    public void setActiveTestdataKey(String activeTestdataKey) {
        TestDataAccessor.activeTestdataKey.set(activeTestdataKey);
        IWebActions.debugMessageLogger.logInformation("Active test data key: " + activeTestdataKey);
    }

    public void setActiveTestdataPage(String activeTestdataPage) {
        TestDataAccessor.activeTestdataPage.set(activeTestdataPage);
        IWebActions.debugMessageLogger.logInformation("Active test data page: " + activeTestdataPage);
    }

    private String getActiveTestdataPage() {
        return activeTestdataPage.get();
    }

    public void readAndLoadTestdata() {
        List<String> testDataFolders = new ArrayList<>();
        testDataFolders.add(getCommonTestDataFilesPath());
        testDataFolders.add(getBuTestDataFilesPath());
        testDataFolders.forEach(folder -> {
            if (Paths.get(folder).toFile().exists()) {
                try (Stream<Path> path = Files.walk(Paths.get(folder))) {
                    path.filter(Files::isRegularFile).map(Path::toFile).forEach(
                            file -> {
                                if (file.toString().endsWith(".txt"))
                                    completeTestDataList.get().addAll(loadTestData(file));
                            }
                    );
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }


    private List<TestDataAccessor> loadTestData(File testDataFile) {
        List<TestDataAccessor> testDataList = new ArrayList<>();
        try (Stream<String> testDataStream = Files.lines(Paths.get(testDataFile.toString()))) {
            testDataStream
                    .filter(line -> line.contains("="))
                    .forEach(testdataEntry -> {
                        if (isEntryEnvSpecific(testdataEntry)) {
                            testDataList.add(new TestDataAccessor(testDataFile.getName().replace(".txt", ""),  //eg: key.env.varName=varValue
                                    ((testdataEntry.split("=")[0]).split("\\."))[0],
                                    ((testdataEntry.split("=")[0]).split("\\."))[1],
                                    ((testdataEntry.split("=")[0]).split("\\."))[2],
                                    (testdataEntry.replace(testdataEntry.split("=")[0], "")).substring(1))); //modified as part of api support to take care of entries like //eg: key.env.varName=varValue=123

                        } else
                            testDataList.add(new TestDataAccessor(testDataFile.getName().replace(".txt", ""), //eg: key.varName=varValue
                                    ((testdataEntry.split("=")[0]).split("\\."))[0],
                                    "",
                                    ((testdataEntry.split("=")[0]).split("\\."))[1],
                                    (testdataEntry.replace(testdataEntry.split("=")[0], "")).substring(1))); //modified as part of api support to take care of entries like //eg: key.env.varName=varValue=123

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return testDataList;
    }

    private boolean isEntryEnvSpecific(String testdataEntry) {
        //eg: key.env.varName=varValue
        return (testdataEntry.split("=")[0]).split("\\.").length > 2;//eg: key.varName=varValue
    }


    public String getTestDataValue(String testdataVar) {
        ArrayList<TestDataAccessor> specifictDataList = new ArrayList<>();
        ArrayList<TestDataAccessor> defaultDataList = new ArrayList<>();
        ArrayList<TestDataAccessor> testDataListInUse = new ArrayList<>();
        final String[] retValue = {""};
        final boolean[] flag = {false};
        completeTestDataList.get().forEach(dataEntry -> {
            if (dataEntry.file.equalsIgnoreCase(getActiveTestdataPage())) {
                if (dataEntry.key.equalsIgnoreCase(getActiveTestdataKey()))
                    specifictDataList.add(new TestDataAccessor(getActiveTestdataPage(), dataEntry.key, dataEntry.env, dataEntry.name, dataEntry.value));
                if (dataEntry.key.equalsIgnoreCase("default"))
                    defaultDataList.add(new TestDataAccessor(getActiveTestdataPage(), dataEntry.key, dataEntry.env, dataEntry.name, dataEntry.value));
            }
        });

        for (TestDataAccessor defaultEntry : defaultDataList) {
            specifictDataList.forEach(specificEntry -> {
                        if (defaultEntry.name.equalsIgnoreCase(specificEntry.name)) {
                            flag[0] = true;
                        }
                    }
            );
            if (!flag[0]) {
                testDataListInUse.add(new TestDataAccessor(defaultEntry.file, defaultEntry.key, defaultEntry.env, defaultEntry.name, defaultEntry.value));
            }
            if (flag[0])
                flag[0] = false;
        }
        testDataListInUse.addAll(specifictDataList);
        List<TestDataAccessor> list = new ArrayList<>();
        for (TestDataAccessor testDataAccessor : testDataListInUse) {
            if (testDataAccessor.name.equalsIgnoreCase(testdataVar)) {
                if (testDataAccessor.env.equals("") || testDataAccessor.env.equalsIgnoreCase(AccessResources.getEnvironmentUnderTest())) {
                    TestDataAccessor dataAccessor = new TestDataAccessor(testDataAccessor.file, testDataAccessor.key, testDataAccessor.env, testDataAccessor.name, testDataAccessor.value);
                    list.add(dataAccessor);
                }
            }
        }
        ArrayList<TestDataAccessor> testDataValues = new ArrayList<>(list);
        testDataValues.stream().filter(testData -> testData.env.equalsIgnoreCase(AccessResources.getEnvironmentUnderTest())).forEach(testData -> retValue[0] = testData.value);
        if (retValue[0].equals("")) {
            testDataValues.stream().filter(testData -> testData.env.equals("")).forEach(testData -> retValue[0] = testData.value);
        }

        IWebActions.debugMessageLogger.logInformation("Test data value from " + getActiveTestdataPage() + " page for: " + getActiveTestdataKey() + "." + testdataVar + "=" + retValue[0]);
        return retValue[0];
    }

}
