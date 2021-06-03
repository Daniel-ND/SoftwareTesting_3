package ru.itmo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class QuestionsPage {

    private WebDriver driver;

    private final static String searchPath = "//input[@aria-label='Search' and @type='text' and @placeholder='Search…']";
    @FindBy(xpath = "//a[@href='/users']")
    private WebElement usersPageLink;
    @FindBy(xpath = "//a[@href='/questions/ask']")
    private WebElement askQuestionButton;
    @FindBy(xpath = "//div[@class='question-summary']/div[@class='summary']//a")
    private WebElement question;
    @FindBy(xpath = "//a[@class='my-profile js-gps-track' and @data-gps-track='profile_summary.click()']")
    private WebElement profileLink;

    public QuestionsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        new WebDriverWait(driver, 5).until(webDriver ->
                ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete")
                && (driver.getCurrentUrl().equals("https://stackoverflow.com/")
                    || driver.getCurrentUrl().equals("https://stackoverflow.com/questions")));
    }

    public WebElement getQuestion() {
        return question;
    }

    public void search(String value) {
        new WebDriverWait(driver, 10).until(driver ->
                ExpectedConditions.elementToBeSelected(By.xpath(searchPath)));
        driver.findElement(By.xpath(searchPath)).sendKeys(value);
        driver.findElement(By.xpath(searchPath)).submit();
    }
}
