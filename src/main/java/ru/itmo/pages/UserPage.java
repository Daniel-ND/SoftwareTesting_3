package ru.itmo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserPage {
    private WebDriver driver;

    @FindBy(xpath = "//a[@class='s-navigation--item' and @title='Questions you have bookmarked']")
    private WebElement bookmarksLink;
    @FindBy(xpath = "//a[@class='s-navigation--item' and @title='Your overall summary']")
    private WebElement summaryLink;
    @FindBy(xpath = "//a[@class='s-navigation--item' and @title='Posts you are following']")
    private WebElement followingLink;
    private static final String removeBookmarkBtnPath = "//button[contains(@class, 'js-bookmark-btn')]";
    @FindBy(xpath = "//a[contains(@href, '/users/edit/') and @data-shortcut='E']")
    private WebElement editProfileLink;
    @FindBy(xpath = "//button[contains(@class, 'js-unfollow-post')]")
    private WebElement unfollowBtn;

    public UserPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        driver = webDriver;
        new WebDriverWait(driver, 1000).until(driver ->
                ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete")
                && driver.getCurrentUrl().startsWith("https://stackoverflow.com/users/"));
    }

    public WebElement getBookmarksLink() {
        return bookmarksLink;
    }

    public WebElement getSummaryLink() {
        return summaryLink;
    }

    public WebElement getFollowingLink() {
        return followingLink;
    }

    public WebElement getEditProfileLink() {
        return editProfileLink;
    }

    public WebElement getUnfollowBtn() {
        return unfollowBtn;
    }

    public void removeFromBookmarks(String xpath) {
        driver.findElement(By.xpath(xpath + removeBookmarkBtnPath)).click();
    }
}
