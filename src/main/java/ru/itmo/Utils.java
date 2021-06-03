package ru.itmo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
public class Utils {

    public static boolean waitForCaptchaIfExists(WebDriver webDriver) {
        waitUntilLoadingCompletes(webDriver, 5);
        try {
            new WebDriverWait(webDriver, 1)
                    .until(ExpectedConditions.titleIs("Human verification - Stack Overflow"));
        } catch (TimeoutException ignored) {}
        if (!webDriver.findElements(By.xpath("//iframe[@title='reCAPTCHA']")).isEmpty()) {
            new WebDriverWait(webDriver, 9999).until(driver ->
                    driver.findElements(By.xpath("//iframe[@title='reCAPTCHA']")).isEmpty());
            return true;
        }
        return false;
    }

    private final static String acceptAllCookiesElementPath = "//button[contains(@class, 'js-accept-cookies')]";

    public static void clickAcceptCookiesIfPresent(WebDriver webDriver) {
        if (!webDriver.findElements(By.xpath(acceptAllCookiesElementPath)).isEmpty()) {
            webDriver.findElement(By.xpath(acceptAllCookiesElementPath)).click();
            waitFixedTimeout(webDriver, 2);
        }
    }

    private final static String menuBarDismissBtnPath = "//*[@id=\"openid-buttons\"]/button[4]";

    public static void dismissMenuBarIfPresent(WebDriver webDriver) {
        if (!webDriver.findElements(By.xpath(menuBarDismissBtnPath)).isEmpty()) {
            webDriver.findElement(By.xpath(menuBarDismissBtnPath)).click();
            waitFixedTimeout(webDriver, 2);
        }
    }

    public static void waitUntilLoadingCompletes(WebDriver webDriver, int timeoutInSeconds) {
        new WebDriverWait(webDriver, timeoutInSeconds).until(driver -> {
            var executor = (JavascriptExecutor)driver;
            return executor.executeScript("return document.readyState").equals("complete");
        });
    }

    public static void waitFixedTimeout(WebDriver webDriver, int timeoutInSeconds) {
        try {
            new WebDriverWait(webDriver, timeoutInSeconds).until(dummy -> false);
        }
        catch (Exception ignore) {}
    }
}
