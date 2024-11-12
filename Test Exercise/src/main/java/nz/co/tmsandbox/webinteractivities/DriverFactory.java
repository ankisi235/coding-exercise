package nz.co.tmsandbox.webinteractivities;

import nz.co.tmsandbox.engine.IAccessor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ThreadGuard;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    public static final Duration TIMEOUT_SECONDS = Duration.ofSeconds(17);
    public static final long IMPLICIT_TIMEOUT_SECONDS = 5;
    public static final long POLLING_INTERVAL_MILLIS = 100;
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<String> browser = ThreadLocal.withInitial(() -> "chrome");

    public DriverFactory() {
        initialiseDriver();
    }

    public String getBrowser() {
        return browser.get();
    }

    public void setBrowser(String browser) {
        DriverFactory.browser.set(browser);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver driver) {
        DriverFactory.driver.set(driver);
    }

    public static FluentWait<WebDriver> getFluentWait() {
        return new WebDriverWait(getDriver(), DriverFactory.TIMEOUT_SECONDS)
                .pollingEvery(Duration.ofMillis(DriverFactory.POLLING_INTERVAL_MILLIS))
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class)
                .ignoring(NullPointerException.class)
                .ignoring(ElementNotInteractableException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .ignoring(NoSuchWindowException.class)
                .ignoring(ElementClickInterceptedException.class)
                .ignoring(ScriptTimeoutException.class);
    }

    private void initialiseDriver() {
        if (getDriver() == null) {
            createDriverInstance();
        }
    }

    private void createDriverInstance() {
        switch (getBrowser().toLowerCase()) {
            case "chrome" -> setDriver(createChromeDriver());
            case "edge" -> setDriver(createEdgeDriver());
        }
        DebugMessageLogger.debugMessageLogger.logInformation("Driver created for: " + getBrowser(), ReportCreator.StatusInReport.INFO);
    }

    private WebDriver createChromeDriver() {
        WebDriver newWebDriver;
        try {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            chromeOptions.setExperimentalOption("prefs", prefs);
            chromeOptions.setAcceptInsecureCerts(true);
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--window-size=1920,1080");
            chromeOptions.addArguments("--disk-cache-dir=/tmp");
            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            chromeOptions.addArguments("--dns-prefetch-disable");
            chromeOptions.addArguments("--aggressive-cache-discard");
            chromeOptions.addArguments("--disable-cache");
            chromeOptions.addArguments("--disable-application-cache");
            chromeOptions.addArguments("--disable-offline-load-stale-cache");
            chromeOptions.addArguments("--remote-allow-origins=*");
            chromeOptions.addArguments("disable-notifications");
            chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            chromeOptions.addArguments("start-maximized");
            newWebDriver = new ChromeDriver(chromeOptions);

            if (!IAccessor.accessResources.isLocalExecution()) {
                String seleniumGrid = IAccessor.accessResources.getRemoteUrlForRemoteExecution();
                newWebDriver = new RemoteWebDriver((new URI(seleniumGrid)).toURL(), chromeOptions);
            }
            System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
            newWebDriver.manage().deleteAllCookies();
            newWebDriver.manage().window().maximize();
            newWebDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_TIMEOUT_SECONDS));
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logInformation("Error in createChromeDriver" + e.getClass().getSimpleName());
            throw new RuntimeException(e);
        }
        return newWebDriver;
    }

    private WebDriver createEdgeDriver() {
        WebDriver newWebDriver;
        try {
            System.setProperty("webdriver.edge.logfile", "edge_driver.log");
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--no-sandbox");
            edgeOptions.addArguments("--remote-allow-origins=*");
            edgeOptions.addArguments("--disable-dev-shm-usage");
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            edgeOptions.setExperimentalOption("prefs", prefs);
            edgeOptions.setAcceptInsecureCerts(true);
            edgeOptions.addArguments("--disable-gpu");
            edgeOptions.addArguments("--window-size=1920,1080");
            edgeOptions.addArguments("--disk-cache-dir=/tmp");
            edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
            edgeOptions.addArguments("--dns-prefetch-disable");
            edgeOptions.addArguments("--aggressive-cache-discard");
            edgeOptions.addArguments("--disable-cache");
            edgeOptions.addArguments("--disable-application-cache");
            edgeOptions.addArguments("--disable-offline-load-stale-cache");
            edgeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            edgeOptions.addArguments("start-maximized");
            newWebDriver = ThreadGuard.protect(new EdgeDriver(edgeOptions));

            if (!IAccessor.accessResources.isLocalExecution()) {
                String seleniumGrid = IAccessor.accessResources.getRemoteUrlForRemoteExecution();
                newWebDriver = new RemoteWebDriver((new URI(seleniumGrid)).toURL(), edgeOptions);
            }
            System.setProperty(EdgeDriverService.EDGE_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
            newWebDriver.manage().deleteAllCookies();
            newWebDriver.manage().window().maximize();
            newWebDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_TIMEOUT_SECONDS));
        } catch (Exception e) {
            DebugMessageLogger.debugMessageLogger.logInformation("Error in createEdgeDriver" + e.getClass().getSimpleName());
            throw new RuntimeException(e);
        }
        return newWebDriver;
    }


    public void destroyDriver() {
        if (getDriver() != null) {
            getDriver().close();
            getDriver().quit();
        }
        setDriver(null);
    }
}
