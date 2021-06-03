package ru.itmo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.itmo.Utils;

public class AskQuestionPage {

    private final WebDriver driver;

    public AskQuestionPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        new WebDriverWait(driver, 5).until(webDriver ->
                ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete")
                        && (driver.getCurrentUrl().equals("https://stackoverflow.com/questions/ask")));

        // всплывающее окно
        if (!driver.findElements(By.xpath("//button[@class='grid--cell s-btn s-btn__primary js-modal-primary-btn js-modal-close js-first-tabbable js-modal-initial-focus js-gps-track']")).isEmpty())
            driver.findElement(By.xpath("//button[@class='grid--cell s-btn s-btn__primary js-modal-primary-btn js-modal-close js-first-tabbable js-modal-initial-focus js-gps-track']")).click();

        Utils.clickAcceptCookiesIfPresent(driver);
    }
}
