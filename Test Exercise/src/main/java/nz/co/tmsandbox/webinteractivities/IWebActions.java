package nz.co.tmsandbox.webinteractivities;

import nz.co.tmsandbox.engine.IAccessor;

public interface IWebActions  extends IAccessor {
    Navigate navigate = new Navigate();
    Validate validate = new Validate();
    Set set = new Set();
    Get get = new Get();
    Click click = new Click();
    Select select = new Select();
    DebugMessageLogger debugMessageLogger = new DebugMessageLogger();
}
