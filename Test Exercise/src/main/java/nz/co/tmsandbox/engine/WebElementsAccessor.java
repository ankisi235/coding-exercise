package nz.co.tmsandbox.engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nz.co.tmsandbox.webinteractivities.DebugMessageLogger;
import nz.co.tmsandbox.webinteractivities.DriverFactory;
import nz.co.tmsandbox.webinteractivities.IWebActions;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

class WebElements {
    String page;
    String name;
    String value;
    String findBy;

    public WebElements(String webPage, String webElementName, String elementFindBy, String webElementValue) {
        this.page = webPage;
        this.name = webElementName;
        this.findBy = elementFindBy;
        this.value = webElementValue;
    }
}


//making WebElementAccessor as a singleton class
public class WebElementsAccessor {
    public static final ThreadLocal<WebElementsAccessor> instance = new ThreadLocal<>();
    String commonWebElementsPath = "";
    String buWebElementsPath = "";
    Set<WebElements> allElementsSet = new HashSet<>();
    String activeElementValue;
    String activeElementFindby;
    private String activeElementName;
    private String activePage = "";

    public WebElementsAccessor() {
    }

    public static WebElementsAccessor getInstance() {
        if (instance.get() == null)
            instance.set(new WebElementsAccessor());
        return instance.get();
    }

    private String getCommonWebElementsPath() {
        return commonWebElementsPath;
    }

    public void setCommonWebElementsPath(String commonWebElementsPath) {
        this.commonWebElementsPath = commonWebElementsPath;
    }

    private String getBuWebElementsPath() {
        return buWebElementsPath;
    }

    public void setBuWebElementsPath(String buWebElementsPath) {
        this.buWebElementsPath = buWebElementsPath;
        if (allElementsSet.size() != 0)
            allElementsSet.clear();
        readAndLoadWebElements();
    }

    public void setActiveWebElementsPage(String activePage) {
        this.activePage = activePage;
    }

    private String getActiveElementValue() {
        return this.activeElementValue;
    }

    private void setActiveElementValue(String activeElementValue) {
        this.activeElementValue = activeElementValue;
    }

    public String getActiveElementFindbyWithValue() {
        return activeElementFindby + ";" + activeElementValue;
    }

    private String getActiveElementFindby() {
        return this.activeElementFindby;
    }

    private void setActiveElementFindby(String activeElementFindby) {
        this.activeElementFindby = activeElementFindby;
    }

    public String getActiveElementName() {
        return activeElementName;
    }

    private void setActiveElementName(String activeElementName) {
        this.activeElementName = activeElementName;
    }

    public void readAndLoadWebElements() {
        List<String> webElementFolders = new ArrayList<>();
        webElementFolders.add(getCommonWebElementsPath());
        webElementFolders.add(getBuWebElementsPath());
        webElementFolders.forEach(folder -> {
            if (Paths.get(folder).toFile().exists()) {
                try (Stream<Path> path = Files.walk(Paths.get(folder))) {
                    path.filter(Files::isRegularFile).map(Path::toFile).forEach(
                            file -> allElementsSet.addAll(loadWebElements(file))
                    );
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    private Set<WebElements> loadWebElements(File webElementFile) {
        List<WebElements> webElementsList = new ArrayList<>();
        try (Stream<String> webElementStream = Files.lines(Paths.get(webElementFile.toString()))) {
            webElementStream
                    .filter(line -> line.contains(","))
                    .forEach(webElementEntry -> webElementsList.add(new WebElements(webElementFile.getName().replace(".csv", ""),
                            webElementEntry.split(",")[0], webElementEntry.split(",")[1], getValueOnSplit(webElementEntry))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return webElementsList.stream().collect(Collectors.toSet());
    }

    private String getValueOnSplit(String webElementEntry) {
        String[] arr = webElementEntry.split(",");
        String eleVal = "";
        if (arr.length == 3)
            eleVal = arr[2];
        if (arr.length > 3) {
            eleVal = webElementEntry;
            for (int i = 0; i < 2; i++) {
                eleVal = eleVal.replace(arr[i] + ",", "");
            }
        }
        return eleVal;
    }

    public boolean isElementPresent(String webEleName) {
        boolean elePresent = getWebElement(webEleName) != null;
        return elePresent;
    }

    private void setActiveElementValueAndFindByFor(String webEleName) {
        final String[] webEleValue = {""};
        final String[] webEleFindBy = {""};
        webEleFindBy[0] = webEleName.split(";")[0];
        webEleValue[0] = webEleName.split(";")[1];
        setActiveElementValue(webEleValue[0]);
        setActiveElementFindby(webEleFindBy[0]);
        if (webEleName.split(";").length == 3)
            setActiveElementName(webEleName.split(";")[2]);
    }

    private By getFindBy(String webEleName) {
        By findBy = null;
        String byMethod = getActiveElementFindby();
        String webEleValue = getActiveElementValue();
        switch (byMethod.toUpperCase()) {
            case "CLASS_NAME":
                findBy = By.className(webEleValue);
                break;
            case "XPATH":
                findBy = By.xpath(webEleValue);
                break;
            case "CSS":
                findBy = By.cssSelector(webEleValue);
                break;
            case "ID":
                findBy = By.id(webEleValue);
                break;
            case "NAME":
                findBy = By.name(webEleValue);
                break;
            case "LINK_TEXT":
                findBy = By.linkText(webEleValue);
                break;
            case "PARTIAL_LINK_TEXT":
                findBy = By.partialLinkText(webEleValue);
                break;
            case "TAG_NAME":
                findBy = By.tagName(webEleValue);
                break;
            default:
                try {
                    throw new Exception("Unknown Find By for the Element: " + webEleName + " Find By: " + byMethod);
                } catch (Exception e) {
                    DebugMessageLogger.debugMessageLogger.logException("Unknown Find By for the Element: " + webEleName + " Find By: " + byMethod, e);
                }
        }
        return findBy;
    }

    public WebElement getWebElement(String webEleName) {
        setActiveElementValueAndFindByFor(webEleName);
        By findBy = getFindBy(webEleName);
        try {
            DriverFactory.getFluentWait().until(ExpectedConditions.and(waitUntilElementNotNull(findBy), ExpectedConditions.presenceOfElementLocated(findBy)));
            //scrollToView(findBy);
            if (!IAccessor.accessResources.isHeadlessTrue())
                highlightElement(DriverFactory.getFluentWait().until(waitUntilElementNotNull(findBy)));
            return DriverFactory.getFluentWait().until(Objects.requireNonNull(waitUntilElementNotNull(findBy)));
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logException("Exception occurred while finding active element", getActiveElementName(), findBy.toString(), e);
            //IWebActions.screenshots.takeScreenShotFailedScenario();
        }
        return null;
    }

    public void scrollToViewAndClick(String findBy) {
        try {
            ((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].scrollIntoView(true);", DriverFactory.getDriver().findElement(getFindBy(findBy)));
            ((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].click();", DriverFactory.getDriver().findElement(getFindBy(findBy)));
            //((JavascriptExecutor) DriverFactory.getDriver()).executeScript("document.getElementById('ID').style.display='block';");
        } catch (Exception s) {
            DebugMessageLogger.debugMessageLogger.logInformation("while scrolling to view" + s.getClass().getSimpleName());
        }
    }

    //change the location
   /* public void waitForPageLoad() {
        IWebActions.navigate.switchToDefaultContent();
        IAccessor.webElementsAccessor.waitUntilInvisible(HomePage.dataStateBusy.get());
        IWebActions.navigate.switchToIFrame(HomePage.frmInteractionWorkArea.get());
        IAccessor.webElementsAccessor.waitUntilElementEnabled(Widgets.linkAttachNew.get());
    }*/

    public List<WebElement> getWebElements(String webEleName) {
        List<WebElement> activeWebElement;
        setActiveElementValueAndFindByFor(webEleName);
        By findBy = getFindBy(webEleName);
        try {
            DriverFactory.getFluentWait().until(waitUntilElementNotNull(findBy));
            activeWebElement = DriverFactory.getDriver().findElements(findBy);
            return activeWebElement;
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logException("Exception occurred while finding active element", getActiveElementName(), findBy.toString(), e);
        }
        return null;
    }

    public void highlightElement(WebElement activeWebElement) {
        try {
            ((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].style.border='3px solid red'", activeWebElement);
        } catch (StaleElementReferenceException e) {
            // handleStaleElementReferenceException(activeWebElement);
        }
    }

    public void waitForClick(WebElement webElement) {
        try {
            DriverFactory.getFluentWait().until(ExpectedConditions.elementToBeClickable(webElement));
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logInformation("Exception occurred while waiting for elementToBeClickable condition" + getActiveElementName() + getActiveElementFindbyWithValue());
        }
    }

    public void waitForClick(String element) {
        try {
            setActiveElementValueAndFindByFor(element);
            By findBy = getFindBy(element);
            DriverFactory.getFluentWait().until(ExpectedConditions.and(waitUntilElementNotNull(findBy), ExpectedConditions.presenceOfElementLocated(findBy),
                    ExpectedConditions.elementToBeClickable(findBy), elementEnabled(element)));
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logInformation("Exception occurred while waiting for elementToBeClickable condition" + getActiveElementName() + getActiveElementFindbyWithValue());
        }
    }

    public WebElement waitUntilElementEnabled(String activeElementName) {
        try {
            return DriverFactory.getFluentWait().until(elementEnabled(activeElementName));
        } catch (Exception e) {
            return null;
        }
    }


    public ExpectedCondition<WebElement> elementEnabled(String activeElementName) {
        setActiveElementValueAndFindByFor(activeElementName);
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    if (DriverFactory.getDriver().findElement(getFindBy(activeElementName)).isEnabled()) {
                        //DebugMessageLogger.debugMessageLogger.logInformation("Element", getActiveElementName(), getActiveElementFindbyWithValue(), "is enabled");
                        getWebElement(activeElementName);
                        return DriverFactory.getDriver().findElement(getFindBy(activeElementName));
                    }
                } catch (NoSuchElementException | StaleElementReferenceException ign) {
                }
                return null;
            }
        };
    }

    public Boolean waitUntilElementDisplayed(String activeElementName) {
        try {
            return DriverFactory.getFluentWait().until(IAccessor.webElementsAccessor.waitUntilElementDisplay(activeElementName));
        } catch (Exception e) {
            return false;
        }
    }


    public ExpectedCondition<Boolean> waitUntilElementDisplay(String activeElementName) {
        setActiveElementValueAndFindByFor(activeElementName);
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    if (DriverFactory.getDriver().findElement(getFindBy(activeElementName)).isDisplayed()) {
                        //DebugMessageLogger.debugMessageLogger.logInformation("Element", getActiveElementName(), getActiveElementFindbyWithValue(), "is displayed");
                        getWebElement(activeElementName);
                        return true;//return DriverFactory.getDriver().findElement(getFindBy(activeElementName));
                    }
                } catch (Exception ign) {
                    DebugMessageLogger.debugMessageLogger.logInformation("Element", getActiveElementName(), getActiveElementFindbyWithValue(), "is not displayed");
                }
                return false;
            }
        };
    }


    private ExpectedCondition<WebElement> waitUntilElementNotNull(By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    if (DriverFactory.getDriver().findElement(locator) != null) {
                        DriverFactory.getFluentWait().
                                until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, 0));
                        return DriverFactory.getDriver().findElement(locator);
                    }
                } catch (TimeoutException timeoutException) {
                    DebugMessageLogger.debugMessageLogger.logInformation(getActiveElementName() + " " + timeoutException.getClass().getSimpleName());
                } catch (Exception ign) {
                    DebugMessageLogger.debugMessageLogger.logInformation(getActiveElementName() + ign.getClass().getSimpleName());
                }
                return null;
            }
        };
    }

    public void waitUntilInvisible(String element) {
        try {
            setActiveElementValueAndFindByFor(element);
            if (DriverFactory.getDriver().findElement(getFindBy(element)) != null) {
                DriverFactory.getFluentWait().until(ExpectedConditions.invisibilityOfElementLocated(getFindBy(element)));
                //DebugMessageLogger.debugMessageLogger.logInformation("Here ", getActiveElementName(), getActiveElementFindbyWithValue());
            }
            //DebugMessageLogger.debugMessageLogger.logInformation("Invisible now", getActiveElementName(), getActiveElementFindbyWithValue());
        } catch (NoSuchElementException e) {
            DebugMessageLogger.debugMessageLogger.logInformation("element is invisible " + getActiveElementName() + e.getClass().getSimpleName());
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logInformation("Exception occurred while waiting for element to be invisible", getActiveElementName(),
                    getActiveElementFindbyWithValue() + e.getClass().getSimpleName());
        }
    }


    public void waitUntilPresenceOfElement(String element) {
        try {
            setActiveElementValueAndFindByFor(element);
            DriverFactory.getFluentWait().until(ExpectedConditions.presenceOfElementLocated(getFindBy(element)));
            //DriverFactory.getFluentWait().until(ExpectedConditions.attributeContains(getFindBy(element),"",""));
            //DriverFactory.getFluentWait().until(ExpectedConditions.visibilityOfElementLocated(getFindBy(element)));
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logInformation(e.getClass().getSimpleName());
        }
    }

    public ExpectedCondition<String> waitUntilTextPresent(String element) {
        setActiveElementValueAndFindByFor(element);
        return new ExpectedCondition<String>() {
            @Override
            public String apply(WebDriver driver) {
                try {
                    if (!DriverFactory.getDriver().findElement(getFindBy(element)).getText().equals("")) {//.length() != 0;
                        return DriverFactory.getDriver().findElement(getFindBy(element)).getText();
                    }
                } catch (NoSuchElementException | StaleElementReferenceException ign) {
                }
                return "";
            }
        };

    }
    public void waitUntilTextfElement(String element, String text) {
        setActiveElementValueAndFindByFor(element);
        By findBy = getFindBy(element);
        try {
            DriverFactory.getFluentWait().until(ExpectedConditions.textToBePresentInElementLocated(findBy, text));
            DebugMessageLogger.debugMessageLogger.logInformation(text + " is present in  " + element);
        } catch (TimeoutException t) {
            DebugMessageLogger.debugMessageLogger.logInformation("Timeout in waiting for text to be present");
        }
    }

    public void waitUntilExpectedURL(String text) {
        try {
            DriverFactory.getFluentWait().until(ExpectedConditions.urlContains(text));
        } catch (TimeoutException t) {
            DebugMessageLogger.debugMessageLogger.logInformation("Timeout in waiting for URL to be loaded" + DriverFactory.getDriver().getCurrentUrl());
        }
    }

    public void waitUntilExpectedTitle(String title) {
        try {
            DriverFactory.getFluentWait().until(ExpectedConditions.titleContains(title));
        } catch (TimeoutException t) {
            DebugMessageLogger.debugMessageLogger.logInformation("Timeout in waiting for URL to be loaded" + DriverFactory.getDriver().getCurrentUrl());
        }
    }

    /**
     * Verify if element is displayed without wait
     *
     * @param elementName
     */
    public boolean isDisplayed(String elementName) {
        setActiveElementValueAndFindByFor(elementName);
        try {
            if (DriverFactory.getDriver().findElement(getFindBy(elementName)).isDisplayed()) {
                DebugMessageLogger.debugMessageLogger.logInformation("web element: " + IAccessor.webElementsAccessor.getActiveElementName() + " is displayed");
                return true;
            } else {
                DebugMessageLogger.debugMessageLogger.logInformation("web element: " + IAccessor.webElementsAccessor.getActiveElementName() + " is not displayed");
            }
        } catch (StaleElementReferenceException e) {
            System.out.println(e.getClass().getSimpleName());
            return true;
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logInformation("Validate element displayed: " + IAccessor.webElementsAccessor.getActiveElementName() +
                    IAccessor.webElementsAccessor.getActiveElementFindbyWithValue() + " " + e.getClass().getSimpleName());
        }
        return false;
    }


    /**
     * Verify if element is enabled without wait
     *
     * @param elementName
     */
    public boolean isEnabled(String elementName) {
        setActiveElementValueAndFindByFor(elementName);
        try {
            if (DriverFactory.getDriver().findElement(getFindBy(elementName)).isEnabled()) {
                DebugMessageLogger.debugMessageLogger.logInformation("web element: " + IAccessor.webElementsAccessor.getActiveElementName() + " is enabled");
                return true;
            } else {
                DebugMessageLogger.debugMessageLogger.logInformation("web element: " + IAccessor.webElementsAccessor.getActiveElementName() + " is not enabled");
            }
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logInformation("Validate element displayed" + IAccessor.webElementsAccessor.getActiveElementName() +
                    IAccessor.webElementsAccessor.getActiveElementFindbyWithValue());
        }
        return false;
    }


    /**
     * Verify if element is enabled without wait
     *
     * @param elementName
     */
    public boolean isSelected(String elementName) {
        setActiveElementValueAndFindByFor(elementName);
        try {
            if (DriverFactory.getDriver().findElement(getFindBy(elementName)).isSelected()) {
                DebugMessageLogger.debugMessageLogger.logInformation("web element: " + IAccessor.webElementsAccessor.getActiveElementName() + " is selected");
                return true;
            } else {
                DebugMessageLogger.debugMessageLogger.logInformation("web element: " + IAccessor.webElementsAccessor.getActiveElementName() + " is not selected");
            }
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logInformation("Validate element selected" + IAccessor.webElementsAccessor.getActiveElementName() +
                    IAccessor.webElementsAccessor.getActiveElementFindbyWithValue());
        }
        return false;
    }

    /** * Gets the inner element text in a li... by Ankita Singh

     Ankita Singh12:44 PM
     /**
     * Gets the inner element text in a list of element
     *
     * @param elementName  List of elements
     * @param innerElement the element which text has to be returned
     */
    public String getInnerElementText(WebElement elementName, String innerElement) {
        setActiveElementValueAndFindByFor(innerElement);
        By findBy = getFindBy(innerElement);
        String value = "";
        try {
            if (elementName.findElement(findBy).getText().equals(""))
                return elementName.findElement(findBy).getAttribute("value");
            else
                return elementName.findElement(findBy).getText();
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logException("Get text of web element" + e.getClass().getSimpleName(), getActiveElementName(), getActiveElementFindbyWithValue(), e);
            return value;
        }
    }


    /**
     * Selects the inner element in a list of element filtered by text contains
     *
     * @param value        text to be present in the parent element
     * @param elementName  List of elements
     * @param innerElement the element to be clicked
     */
    public void selectInnerElementFromList(String elementName, String value, String innerElement) {
        try {
            setActiveElementValueAndFindByFor(innerElement);
            By findBy = getFindBy(innerElement);
            WebElement web = IAccessor.webElementsAccessor.getWebElements(elementName).stream().filter((element) -> element.getText().contains(value)).findFirst().orElse(null);
            highlightElement(Objects.requireNonNull(web).findElement(findBy));
            web.findElement(findBy).click();
            DebugMessageLogger.debugMessageLogger.logInformation("Selected inner element from the list", value, IAccessor.webElementsAccessor.getActiveElementName(),
                    IAccessor.webElementsAccessor.getActiveElementFindbyWithValue());
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logException("Select inner element from the list", value, IAccessor.webElementsAccessor.getActiveElementName(),
                    IAccessor.webElementsAccessor.getActiveElementFindbyWithValue(), e);
        }
    }


    public void openNewTab() {
        try {
            ((JavascriptExecutor) DriverFactory.getDriver()).executeScript("window.open()");
            ArrayList<String> tabs = new ArrayList<>(DriverFactory.getDriver().getWindowHandles());
            DriverFactory.getDriver().switchTo().window(tabs.get(1));
        } catch (Exception e) {
            System.out.println(IAccessor.webElementsAccessor.getActiveElementFindbyWithValue() + e.getClass().getSimpleName());
        }
    }

}
