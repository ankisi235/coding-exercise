package nz.co.tmsandbox;

import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
public class BDDMain {
    public static void main(String[] args){
        CoreMain.setFeatureFilesPath("src/test/features");
        CoreMain.setCucumberStepDefGluePackage("nz/co/tmsandbox/stepdefinitions");
        CoreMain.main(args);
    }
}
