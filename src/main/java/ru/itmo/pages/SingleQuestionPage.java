package ru.itmo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SingleQuestionPage {

    private WebDriver driver;

    @FindBy(xpath = "//textarea[@name='post-text']")
    private WebElement postAnswerTextarea;
    @FindBy(xpath = "//button[@type='submit']")
    private WebElement button;

    private final static String emailErrorMsgPath = "//div[@class='message-text']";

    @FindBy(xpath = "//div[@class='answer']//a[@title='Expand to show all comments on this post']")
    private WebElement showMoreComments;
    @FindBy(xpath = "//button[contains(@class, 'js-bookmark-btn')]")
    private WebElement addToBookmarksBtn;
    @FindBy(xpath = "//button[contains(@class, 'js-follow-post js-follow-question')]")
    private WebElement addToFollowingBtn;
    @FindBy(xpath = "//a[contains(@class, 'my-profile')]")
    private WebElement myProfileLink;
    @FindBy(xpath = "//a[@rel='nofollow' and @itemprop='url' and @class='js-share-link js-gps-track']")
    private WebElement shareLink;
    private static final String shareLinkPopupPath = "//div[@class='s-popover z-dropdown s-anchors s-anchors__default is-visible']";

    public SingleQuestionPage(WebDriver webDriver) {
        driver = webDriver;
        PageFactory.initElements(driver, this);
        new WebDriverWait(driver, 5).until(driver ->
                ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete")
                        && (driver.getCurrentUrl().startsWith("https://stackoverflow.com/questions/")));
    }

    public WebElement getPostAnswerTextarea() {
        return postAnswerTextarea;
    }

    public WebElement getButton() {
        return button;
    }

    public WebElement getShowMoreComments() {
        return showMoreComments;
    }

    public WebElement getAddToBookmarksBtn() {
        return addToBookmarksBtn;
    }

    public WebElement getAddToFollowingBtn() {
        return addToFollowingBtn;
    }

    public WebElement getMyProfileLink() {
        return myProfileLink;
    }

    public WebElement getShareLink() {
        return shareLink;
    }

    public boolean isSubmissionValid() {
        var found = driver.findElements(
                By.xpath("//div[contains(@class, 'js-stacks-validation-message')]")
        );
        return found.isEmpty();
    }

    public boolean hasErrors() {
        new WebDriverWait(driver, 10).until(driver ->
            !driver.findElements(
                    By.xpath("//div[contains(@class, 'js-general-error general-error')]")
            ).isEmpty()
        );
        return !driver.findElements(
                By.xpath("//div[contains(@class, 'js-general-error general-error')]")
        ).isEmpty()
                || driver.findElement(By.xpath("//div[@class='message-text']")).getText()
                .equals("To answer a question, you must either sign up for an account or post as a guest.");

    }

    public boolean hasEmailError() {
        new WebDriverWait(driver, 5).until(driver ->
                driver.findElement(By.xpath(emailErrorMsgPath)).getText().equals("An email is required to post.")
        );
        return driver.findElement(By.xpath(emailErrorMsgPath)).getText().equals("An email is required to post.");
    }

    public void dismiss() {
        driver.findElement(By.xpath("//button[@title='Dismiss']")).click();
    }

    public boolean clickShareQuestion() {
        shareLink.click();
        var found = !driver.findElements(By.xpath(shareLinkPopupPath)).isEmpty();
        return found
                && driver.findElement(By.xpath(
                        shareLinkPopupPath + "//input[@type='text']"))
                .getAttribute("value").startsWith("https://stackoverflow.com/q/");
    }
}
