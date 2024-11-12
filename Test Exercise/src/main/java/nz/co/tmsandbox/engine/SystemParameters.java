package nz.co.tmsandbox.engine;

import nz.co.tmsandbox.webinteractivities.IWebActions;
import org.apache.commons.cli.*;

public class SystemParameters implements IAccessor, IWebActions {


    public static void systemParameters(String[] args) {
        Options options = new Options();
        Option aut = new Option("a", "applicationundertest", true, "Application under test");
        aut.setRequired(false);
        options.addOption(aut);
        Option env = new Option("e", "environment under test", true, "Environment under test");
        aut.setRequired(false);
        options.addOption(env);
        Option tags = new Option("t", "tags under test", true, "Tags under test");
        aut.setRequired(false);
        options.addOption(tags);
        Option threads = new Option("th", "threads under test", true, "number of Threads");
        aut.setRequired(false);
        options.addOption(threads);
        Option headless = new Option("h", "headless execution", true, "Headless execution by default its True");
        aut.setRequired(false);
        options.addOption(headless);
        Option remoteUrl = new Option("r", "remote url", true, "Remote url for remote execution");
        aut.setRequired(false);
        options.addOption(remoteUrl);
        Option browser = new Option("b", "execute with browser", true, "Set the browser");
        aut.setRequired(false);
        options.addOption(browser);
        Option localDriver = new Option("d", "use local driver", true, "Use driver in local driver folder (true or false)");
        aut.setRequired(false);
        options.addOption(localDriver);
        Option reRunFailedScenarios = new Option("rf", "re-run failed scenarios", true, "Re-run failed scenarios (true or false)");
        aut.setRequired(false);
        options.addOption(reRunFailedScenarios);

        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            helpFormatter.printHelp("--help", options);
        }

        debugMessageLogger.logInformation("Arguments set::");
        debugMessageLogger.logInformation(String.format("Application under test: %s", commandLine.getOptionValue("a")));
        debugMessageLogger.logInformation(String.format("Environment under test: %s", commandLine.getOptionValue("e")));
        debugMessageLogger.logInformation(String.format("Tags under test: %s", commandLine.getOptionValue("t")));
        debugMessageLogger.logInformation(String.format("Threads: %s", commandLine.getOptionValue("th")));
        debugMessageLogger.logInformation(String.format("Use driver in local driver folder: %s", commandLine.getOptionValue("d")));
        accessResources.setThreads(commandLine.getOptionValue("th"));
        accessResources.setApplicationUnderTest(commandLine.getOptionValue("a"));
        accessResources.setEnvironmentUnderTest(commandLine.getOptionValue("e"));
        accessResources.setTestTags(commandLine.getOptionValue("t"));
        accessResources.setRemoteUrlForRemoteExecution(commandLine.getOptionValue("r"));
        if (commandLine.getOptionValue("r") != null) {
            accessResources.setLocalExecution(false);
            //accessResources.setHeadlessTrue(true);
            if (commandLine.getOptionValue("r").contains("http") && commandLine.getOptionValue("r").contains("wd"))
                accessResources.setRemoteUrlForRemoteExecution(commandLine.getOptionValue("r"));
            else {
                debugMessageLogger.logInformation(String.format("Remote url for remote execution: %s", commandLine.getOptionValue("r")));
                debugMessageLogger.logInformation("Enter a valid remote url for remote execution. Eg.: http://bastion.epa.extnp.national.com.au:443/wd/hub");
                System.exit(-1);
            }
        } else {
            accessResources.setLocalExecution(true);
            if (commandLine.getOptionValue("h") == null)
                accessResources.setHeadlessTrue(true); //by default its headless execution
            else
                accessResources.setHeadlessTrue(Boolean.parseBoolean(commandLine.getOptionValue("h")));
        }

        if (commandLine.getOptionValue("rf") != null)
            accessResources.setRerunFailedScenarios(Boolean.parseBoolean((commandLine.getOptionValue("rf"))));
        else
            accessResources.setRerunFailedScenarios(false);
        if (commandLine.getOptionValue("th") == null) {
            accessResources.setThreads("1");
        }
        debugMessageLogger.logInformation(String.format("Remote url for remote execution: %s", commandLine.getOptionValue("r")));
        debugMessageLogger.logInformation(String.format("Headless execution: %s", accessResources.isHeadlessTrue()));
        debugMessageLogger.logInformation(String.format("Local execution: %s", accessResources.isLocalExecution()));
        debugMessageLogger.logInformation(String.format("Re-run failed scenarios: %s", commandLine.getOptionValue("rf")));
        debugMessageLogger.logInformation(String.format("App Url: %s", commandLine.getOptionValue("url")));
        debugMessageLogger.logInformation("\n\n");
    }

}
