package ru.itmo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.itmo.Utils;

public class MainPage {
    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;

    @FindBy(xpath = "//p/a[@href='/questions']")
    private WebElement searchContentLinkPath;

    @FindBy(xpath = "//*[@id=\"mainbar\"]/div[1]/div/a")
    private WebElement askQuestionButton;

    @FindBy(xpath = "//*[@id=\"top-search\"]/div[4]/div[2]/div/div[1]/a")
    private WebElement askQuestionButtonInSearchSection;

    @FindBy(xpath = "//*[@id=\"search\"]/div/input")
    private WebElement searchField;

    @FindBy(xpath = "/html/body/header/div/ol[2]/li[2]/a[1]")
    private WebElement loginButton;

    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        webDriver = driver;
        webDriverWait = new WebDriverWait(driver, 30);
        Utils.clickAcceptCookiesIfPresent(driver);
    }

    public void waitForUrl(String url) {
        webDriverWait.until(driver -> driver.getCurrentUrl().startsWith(url));
    }

    public void clickSearchContentLink() {
        searchContentLinkPath.click();
    }

    public WebElement getAskQuestionButton() {
        return askQuestionButton;
    }

    public WebElement getAskQuestionButtonInSearchSection() {
        return askQuestionButtonInSearchSection;
    }

    public WebElement getSearchField() {
        return searchField;
    }

    public WebElement getLoginButton() {
        return loginButton;
    }
}
