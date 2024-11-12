Have below Set up in your system:

IntelliJ or your preferred editor, Java, Maven, Git
Add JAVA_HOME and MAVEN_HOME in your environment variables. (location of Java and maven up to bin folder)

Install below plugins in your editor:
Cucumber for Java, Gherkin

Clone the repository or download the folder.

Open the project in intelliJ and build. 

Select the Feature file and run the tests.

alternatively launch terminal in intellij and run below command: 
mvn clean install exec:java "-Dexec.mainClass=nz.co.tmsandbox.BDDMain" "-Dexec.args=-a trademe -e test -t @test -th 1 -h false -rf false" "-Dexec.cleanupDaemonThreads=false"

