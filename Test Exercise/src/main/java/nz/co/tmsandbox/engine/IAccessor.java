package nz.co.tmsandbox.engine;

import nz.co.tmsandbox.utilities.AccessResources;
import nz.co.tmsandbox.utilities.DateAndTime;
import nz.co.tmsandbox.utilities.IOOperations;


public interface IAccessor {
    WebElementsAccessor webElementsAccessor = new WebElementsAccessor();
    TestDataAccessor testDataAccessor = new TestDataAccessor();
    IOOperations ioOperations = new IOOperations() ;
    DateAndTime dateAndTime = new DateAndTime();
    AccessResources accessResources = new AccessResources();
}
