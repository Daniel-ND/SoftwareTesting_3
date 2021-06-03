package ru.itmo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage {
    private WebDriver driver;

//    @FindBy(xpath = "//p/a[@href='/questions']")
//    private WebElement searchContentLinkPath;
//    @FindBy(xpath = "//p/a[@href='https://stackoverflow.com/teams']")
//    private WebElement discoverTeamsContentLinkPath;
//    @FindBy(xpath = "//a[@href='/users/signup']")
//    private WebElement joinCommunityBtnPath;
//    @FindBy(xpath = "//a[@href='https://stackoverflow.com/teams/create/free']")
//    private WebElement createTeamLinkPath;

    @FindBy(xpath = "//button[@data-provider='google' and @data-oauthserver='https://accounts.google.com/o/oauth2/auth']")
    private WebElement registerWithGoogleButton;
    @FindBy(xpath = "//button[@data-provider='github' and @data-oauthserver='https://github.com/login/oauth/authorize']")
    private WebElement registerWithGithubButton;
    @FindBy(xpath = "//button[@data-provider='facebook' and @data-oauthserver='https://www.facebook.com/v2.0/dialog/oauth']")
    private WebElement registerWithFacebookButton;

    @FindBy(xpath = "//input[@name='display-name']")
    private WebElement name;
    @FindBy(xpath = "//input[@name='email' and @type='email']")
    private WebElement email;
    @FindBy(xpath = "//input[@name='password' and @type='password']")
    private WebElement password;
    @FindBy(xpath = "//button[@name='submit-button']")
    private WebElement registerButton;
    @FindBy(xpath = "//input[@type='checkbox' and @name='EmailOptIn']")
    private WebElement captcha;


    public RegisterPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public WebElement getRegisterWithGoogleButton() {
        return registerWithGoogleButton;
    }

    public WebElement getRegisterWithGithubButton() {
        return registerWithGithubButton;
    }

    public WebElement getRegisterWithFacebookButton() {
        return registerWithFacebookButton;
    }

    public WebElement getName() {
        return name;
    }

    public WebElement getPassword() {
        return password;
    }

    public WebElement getEmail() {
        return email;
    }

    public WebElement getRegisterButton() {
        return registerButton;
    }

    public WebElement getCaptcha() {
        return captcha;
    }

    public boolean isRegistrationSucceeded(WebDriver driver) {
        var found = driver.findElements(By.xpath("//*[@id='content']/div[contains(@class, 's-notice__success')]"));
        return !found.isEmpty();
    }

    public boolean isCaptchaWindowShowed() {
        var found = driver.findElements(By.xpath("//div[contains(@style, 'visibility: visible;') and .//div[@class='g-recaptcha-bubble-arrow']]"));
        return !found.isEmpty();
    }

    public WebElement getCaptchaElement() {
        return driver.findElement(By.xpath("//div[@id='no-captcha-here']//iframe"));
    }
}
