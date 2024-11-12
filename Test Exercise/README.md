Have below Set up in your system:

IntelliJ IDEA CE 2022.3 or above, Java 20, Maven 3.8.6 or above, Git
For windows, Add JAVA_HOME and MAVEN_HOME in your environment variables. (location of Java and maven up to bin folder)

Install below plugins in your editor:
Cucumber for Java, Gherkin

Clone the repository or download the folder.

Open the project in intelliJ and build. 

Select the Feature file and run the tests.

Please troubleshoot any environmental issue by checking suitable software version or project setting in the editor.

alternatively launch terminal, check for java n mvn verions, and run below command: 
mvn clean install exec:java "-Dexec.mainClass=nz.co.tmsandbox.BDDMain" "-Dexec.args=-a trademe -e test -t @test -th 1 -h false -rf false" "-Dexec.cleanupDaemonThreads=false"

