package com.upside.bbc.selenium.setupDriver;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static com.upside.bbc.selenium.setupDriver.DriverType.CHROME_HEADLESS;
import static com.upside.bbc.selenium.setupDriver.DriverType.valueOf;
import static org.openqa.selenium.Proxy.ProxyType.MANUAL;

public class DriverFactory {

    private RemoteWebDriver webdriver;
    private DriverType selectedDriverType;

    private final DriverType defaultDriverType = CHROME_HEADLESS;
    private final String browser = System.getProperty("browser", defaultDriverType.toString()).toUpperCase();
    private final String operatingSystem = System.getProperty("os.name").toUpperCase();
    private final String systemArchitecture = System.getProperty("os.arch");
    private final boolean useRemoteWebDriver = Boolean.getBoolean("remoteDriver");
    private final boolean proxyEnabled = Boolean.getBoolean("proxyEnabled");
    private final String proxyHostname = System.getProperty("proxyHost");
    private final String jobName = System.getProperty("name");
    private final Integer proxyPort = Integer.getInteger("proxyPort");
    private final String proxyDetails = String.format("%s:%d", proxyHostname, proxyPort);
    public static final Logger LOG = LoggerFactory.getLogger(DriverFactory.class);


    public WebDriver getDriver() throws Exception {
        if (null == webdriver) {
            Proxy proxy = null;
            if (proxyEnabled) {
                proxy = new Proxy();
                proxy.setProxyType(MANUAL);
                proxy.setHttpProxy(proxyDetails);
                proxy.setSslProxy(proxyDetails);
            }
            determineEffectiveDriverType();
            MutableCapabilities mutableCapabilities = selectedDriverType.getDesiredCapabilities(proxy);
            instantiateWebDriver(mutableCapabilities);
        }

        return webdriver;
    }

    public void quitDriver() {
        if (null != webdriver) {
            webdriver.quit();
        }
    }

    // method to determine the Driver Type

    private void determineEffectiveDriverType() {
        DriverType driverType = defaultDriverType;
        try {
            driverType = valueOf(browser);
        } catch (IllegalArgumentException ignored) {
            System.err.println("Unknown driver specified, defaulting to '" + driverType + "'...");
        } catch (NullPointerException ignored) {
            System.err.println("No driver specified, defaulting to '" + driverType + "'...");
        }
        selectedDriverType = driverType;
    }

    // instantiate web driver

    private void instantiateWebDriver(MutableCapabilities mutableCapabilities) throws MalformedURLException {

        LOG.info("Current OS: " + operatingSystem + ",  Architecture: " + systemArchitecture + ", Browser: " + selectedDriverType);

        if (useRemoteWebDriver) {
            URL seleniumGridURL = new URL(System.getProperty("gridURL"));
            String desiredBrowserVersion = System.getProperty("desiredBrowserVersion");
            String desiredPlatform = System.getProperty("desiredPlatform");
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.merge(mutableCapabilities);

            if (null != desiredPlatform && !desiredPlatform.isEmpty()) {
                desiredCapabilities.setPlatform(Platform.valueOf(desiredPlatform.toUpperCase()));
            }

            if (null != desiredBrowserVersion && !desiredBrowserVersion.isEmpty()) {
                desiredCapabilities.setVersion(desiredBrowserVersion);
            }
            desiredCapabilities.setCapability("name", jobName);

            webdriver = new RemoteWebDriver(seleniumGridURL, desiredCapabilities);
        } else {
            webdriver = selectedDriverType.getWebDriverObject(mutableCapabilities);
        }

        //Always wait for page to load
        webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
}

