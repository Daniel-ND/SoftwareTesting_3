package ru.itmo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;

    @FindBy(xpath  = "//input[@type='email']")
    private WebElement emailInputPath;
    @FindBy(xpath = "//input[@type='password']")
    private WebElement passwordInputPath;
    @FindBy(xpath = "//button[@name='submit-button']")
    private WebElement loginButton;
    private String errorMsgPath = "//p[contains(@class, 'js-error-message')]";
    @FindBy(xpath = "//button[@data-provider='google' and @data-oauthserver='https://accounts.google.com/o/oauth2/auth']")
    private WebElement loginWithGoogleButtonPath;
    @FindBy(xpath = "//button[@data-provider='github' and @data-oauthserver='https://github.com/login/oauth/authorize']")
    private WebElement loginWithGithubButtonPath;
    @FindBy(xpath = "//button[@data-provider='facebook' and @data-oauthserver='https://www.facebook.com/v2.0/dialog/oauth']")
    private WebElement loginWithFacebookButtonPath;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        webDriver = driver;
        webDriverWait = new WebDriverWait(driver, 30);
    }

    public void login(String email, String password) {
        emailInputPath.sendKeys(email);
        passwordInputPath.sendKeys(password);
        loginButton.click();
    }

    public void waitForUrl(String url) {
        webDriverWait.until(driver -> driver.getCurrentUrl().equals(url));
    }

    public boolean isErrorAppear() {
        return !webDriver.findElements(By.xpath(errorMsgPath)).isEmpty();
    }

    public WebElement getLoginWithGoogleButtonPath() {
        return loginWithGoogleButtonPath;
    }

    public WebElement getLoginWithGithubButtonPath() {
        return loginWithGithubButtonPath;
    }

    public WebElement getLoginWithFacebookButtonPath() {
        return loginWithFacebookButtonPath;
    }
}
