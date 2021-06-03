package ru.itmo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import ru.itmo.pages.LoginPage;
import ru.itmo.pages.QuestionsPage;
import ru.itmo.pages.SingleQuestionPage;
import ru.itmo.pages.UserPage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionsTest {
    private List<WebDriver> drivers;

    @BeforeAll
    public static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", "/home/daniel/IdeaProjects/TPO3/WebDrivers/chromedriver");
        System.setProperty("webdriver.gecko.driver", "/home/daniel/IdeaProjects/TPO3/WebDrivers/geckodriver");
    }
    @BeforeEach
    public void setUp() {
        drivers = getDrivers();
        drivers.forEach(driver -> {
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        });
    }

    @AfterEach
    public void tearDown() {
        drivers.forEach(WebDriver::close);
    }

    public static List<WebDriver> getDrivers() {
        return List.of(
//                new ChromeDriver(),
                new FirefoxDriver()
        );
    }

    @Test
    public void testAnswerQuestion1() {
        drivers.forEach(driver -> {
            driver.get("https://stackoverflow.com/questions");
            var questionsPage = new QuestionsPage(driver);
            questionsPage.getQuestion().click();

            var singleQuestionPage = new SingleQuestionPage(driver);
            Utils.clickAcceptCookiesIfPresent(driver);
            Utils.dismissMenuBarIfPresent(driver);
            singleQuestionPage.getPostAnswerTextarea().sendKeys("asdfghjk qwer ty ui");
            singleQuestionPage.getButton().click();

            assertFalse(singleQuestionPage.isSubmissionValid());
            assertTrue(singleQuestionPage.hasErrors());
        });
    }

    @Test
    public void testAnswerQuestion2() {
        drivers.forEach(driver -> {
            driver.get("https://stackoverflow.com/questions");
            var questionsPage = new QuestionsPage(driver);
            questionsPage.getQuestion().click();

            var singleQuestionPage = new SingleQuestionPage(driver);
            Utils.clickAcceptCookiesIfPresent(driver);
            Utils.dismissMenuBarIfPresent(driver);
            singleQuestionPage.getPostAnswerTextarea().sendKeys("asdfghjk qwer ty ui");
            singleQuestionPage.getButton().click();

            assertTrue(singleQuestionPage.hasErrors()
                    || singleQuestionPage.hasEmailError());
        });
    }

    @Test
    public void testSearchQuestion(){
        drivers.forEach(driver -> {
            driver.get("https://stackoverflow.com/users/login");
            var loginPage = new LoginPage(driver);
            loginPage.login("ohz71219@eoopy.com", "pass1234");

            if (Utils.waitForCaptchaIfExists(driver)) {
                return;
            }
            Utils.clickAcceptCookiesIfPresent(driver);

            var questionsPage = new QuestionsPage(driver);

            questionsPage.search("answers:518");
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.findElement(
                    By.xpath("//a[contains(@href, '/questions/184618/what-is-the-best-comment-in-source-code-you-have-ever-encountered')]"))
                    .click();

            var questionPage = new SingleQuestionPage(driver);
            var path = "//div[@itemprop='mainEntity']//a[contains(@class, 'question-hyperlink')]";
            assertEquals("What is the best comment in source code you have ever encountered? [closed]",
                    driver.findElement(By.xpath(path)).getText());
        });
    }

    @Test
    public void testBookmarkQuestion() {
        drivers.forEach(driver -> {
            driver.get("https://stackoverflow.com/users/login");
            var loginPage = new LoginPage(driver);
            loginPage.login("ohz71219@eoopy.com", "pass1234");

            if (Utils.waitForCaptchaIfExists(driver)) {
                return;
            }
            Utils.clickAcceptCookiesIfPresent(driver);

            var questionsPage = new QuestionsPage(driver);

            questionsPage.search("answers:518");
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            driver.findElement(
                    By.xpath("//a[contains(@href, '/questions/184618/what-is-the-best-comment-in-source-code-you-have-ever-encountered')]"))
                    .click();

            var singleQuestionPage = new SingleQuestionPage(driver);

            singleQuestionPage.getAddToBookmarksBtn().click();
            singleQuestionPage.getMyProfileLink().click();

            var userPage = new UserPage(driver);
            userPage.getBookmarksLink().click();
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

            assertEquals("What is the best comment in source code you have ever encountered? [closed]", driver.findElement(By.xpath(
                    "//div[@class='user-questions']/div[contains(@class, 'question-summary')]//a[@class='question-hyperlink']"
            )).getText());

            userPage.removeFromBookmarks("//div[@class='user-questions']/div[contains(@class, 'question-summary')]");
            driver.navigate().refresh();
            Utils.waitUntilLoadingCompletes(driver, 5);

            Assertions.assertTrue(driver.findElements(By.xpath(
                    "//div[@class='user-questions']/div[contains(@class, 'question-summary')]//a[@class='question-hyperlink']"
            )).isEmpty());
        });
    }

    @Test
    public void testFollowQuestion() {
        drivers.forEach(driver -> {
            driver.get("https://stackoverflow.com/users/login");
            var loginPage = new LoginPage(driver);
            loginPage.login("ohz71219@eoopy.com", "pass1234");

            if (Utils.waitForCaptchaIfExists(driver)) {
                return;
            }
            Utils.clickAcceptCookiesIfPresent(driver);

            var questionsPage = new QuestionsPage(driver);

            questionsPage.search("answers:107");
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.findElement(
                    By.xpath("//a[contains(@href, '/questions/5767325/how-can-i-remove-a-specific-item-from-an-array')]"))
                    .click();

            var singleQuestionPage = new SingleQuestionPage(driver);

            singleQuestionPage.getAddToFollowingBtn().click();
            singleQuestionPage.getMyProfileLink().click();

            var userPage = new UserPage(driver);
            userPage.getFollowingLink().click();

            assertEquals("How can I remove a specific item from an array?", driver.findElement(By.xpath(
                    "//div[@class='user-questions']/div[contains(@class, 'js-followed-post')]//a[@class='question-hyperlink']"
            )).getText());

            userPage.getUnfollowBtn().click();
            driver.navigate().refresh();
            Utils.waitUntilLoadingCompletes(driver, 5);

            Assertions.assertTrue(driver.findElements(By.xpath(
                    "//div[@class='user-questions']/div[contains(@class, 'js-followed-post')]//a[@class='question-hyperlink']"
            )).isEmpty());
        });
    }


    @Test
    public void testShareQuestion() {
        drivers.forEach(driver -> {
            driver.get("https://stackoverflow.com/users/login");
            var loginPage = new LoginPage(driver);
            loginPage.login("ohz71219@eoopy.com", "pass1234");

            if (Utils.waitForCaptchaIfExists(driver)) {
                return;
            }
            Utils.clickAcceptCookiesIfPresent(driver);

            var questionsPage = new QuestionsPage(driver);

            questionsPage.search("answers:518");
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.findElement(
                    By.xpath("//a[contains(@href, '/questions/184618/what-is-the-best-comment-in-source-code-you-have-ever-encountered')]"))
                    .click();

            var questionPage = new SingleQuestionPage(driver);
            assertTrue(questionPage.clickShareQuestion());
        });
    }
}
