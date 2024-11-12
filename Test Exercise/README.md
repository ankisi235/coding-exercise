Have below Set up in your system:

IntelliJ or your preferred editor,

Java,

Maven,

Git


Install below plugins in your editor:

Cucumber for Java

Gherkin

Clone the repository - git clone 


Add JAVA_HOME and MAVEN_HOME in your environment variables. (location of Java and maven up to bin folder)

Open the project in intelliJ and build. 

Select a Feature file and run the tests.

alternatively run below in Terminal : 
mvn clean install exec:java "-Dexec.mainClass=nz.co.tmsandbox.BDDMain" "-Dexec.args=-a trademe -e test -t @test -th 1 -h false -rf false" "-Dexec.cleanupDaemonThreads=false"



