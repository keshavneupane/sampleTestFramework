package com.upside.bbc.selenium;

import com.upside.bbc.selenium.listeners.ScreenshotListener;
import com.upside.bbc.selenium.setupDriver.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 *Base Driver Class to setup the WebDriver, clear cookies at the end and gracefully kill the driver
 * at the end of the Suite
 *
 */

@Listeners(ScreenshotListener.class)
public class DriverBase {

    private static List<DriverFactory> webDriverThreadPool = Collections.synchronizedList(new ArrayList<DriverFactory>());
    private static ThreadLocal<DriverFactory> driverFactory;
    public static final Logger LOG = LoggerFactory.getLogger(DriverBase.class);

    // Always run this method before the suite to instantiate driver
    @BeforeSuite(alwaysRun = true)
    public static void instantiateDriverObject() {
        driverFactory = new ThreadLocal<DriverFactory>() {
            @Override
            protected DriverFactory initialValue() {
                DriverFactory driverFactory = new DriverFactory();
                webDriverThreadPool.add(driverFactory);
                return driverFactory;
            }
        };
    }

    public static WebDriver getDriver() throws Exception {
        return driverFactory.get().getDriver();
    }

    // method to get text by using id of the element

    public static String getText(String id) throws Exception {
        return getDriver().findElement(By.id(id)).getText();
    }

    // method to get text by using xpath

    public static String getTextByXPath(String xpath) throws Exception {
        return getDriver().findElement(By.xpath(xpath)).getText();
    }

    // method to get text of the element by using class name

    public static String getTextByClass(String className) throws Exception {
        return getDriver().findElement(By.className(className)).getText();
    }
    // method to click by using id

    public static void click(String id) throws Exception {
        getDriver().findElement(By.id(id)).click();
    }

    // method to click by class name

    public static void clickByClass(String className) throws Exception {
        getDriver().findElement(By.className(className)).click();
    }

    // method to fill text

    public static void fill(String id,String fillText) throws Exception {
        getDriver().findElement(By.id(id)).sendKeys(fillText);
    }

    public static void select(String id, String value) throws Exception {
        new Select(getDriver().findElement(By.id(id))).selectByValue(value);
    }
    // method to click by xpath
    public static void clickByxpath(String xpath) throws Exception {
        getDriver().findElement(By.xpath(xpath)).click();
    }
    // methid to getAttribbuteValue by xpath
    public static String getAttributeValueByxpath(String xpath, String attributeName) throws Exception{
        return getDriver().findElement(By.xpath(xpath)).getAttribute(attributeName);
    }
    // method to get attribute value by classname
    public static String getAttributeValue(String className, String attributeName) throws Exception{
        return getDriver().findElement(By.className(className)).getAttribute(attributeName);
    }

    // method to check if the element is displayed - by using css
    public static boolean isElementDisplayedCSS(String className) throws Exception{
        return getDriver().findElement(By.className(className)).isDisplayed();
    }
    // method to check if the element is displayed - by using xpath
    public static boolean isElementDisplayedxpath(String xpath) throws Exception{
        return getDriver().findElement(By.xpath(xpath)).isDisplayed();
    }

    public static void restartBrowser() throws Exception {
        closeDriverObjects();
        instantiateDriverObject();
    }
    // method to get the current url
    public static String getURLCurrent() throws Exception{
        return getDriver().getCurrentUrl();
    }

    // clear cookies
    @AfterMethod(alwaysRun = true)
    public static void clearCookies() throws Exception {
        getDriver().manage().deleteAllCookies();
    }

    // quit driver at the end
    @AfterSuite(alwaysRun = true)
    public static void closeDriverObjects() {
        for (DriverFactory driverFactory : webDriverThreadPool) {
            driverFactory.quitDriver();
        }
    }

}