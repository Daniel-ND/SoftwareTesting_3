package ru.itmo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import ru.itmo.pages.LoginPage;
import ru.itmo.pages.MainPage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class MainPageTest {
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
                new ChromeDriver(),
                new FirefoxDriver()
        );
    }


    @Test
    public void testNavigation() {
        drivers.forEach(driver -> {
            driver.get("https://stackoverflow.com/");
            MainPage page = new MainPage(driver);
            page.clickSearchContentLink();
            assertEquals("https://stackoverflow.com/questions", driver.getCurrentUrl());
        });
    }

    @Test
    public void authorizedAskQuestion() {
        drivers.forEach(driver -> {
            driver.get("https://stackoverflow.com/");
            MainPage page = new MainPage(driver);
            page.getLoginButton().click();
            page.waitForUrl("https://stackoverflow.com/users/login");

            var loginPage = new LoginPage(driver);
            loginPage.login("ohz71219@eoopy.com", "pass1234");
            loginPage.waitForUrl("https://stackoverflow.com/");
            page.getAskQuestionButton().click();
            assertEquals("https://stackoverflow.com/questions/ask", driver.getCurrentUrl());
        });
    }

    @Test
    public void unauthorizedAskQuestion() {
        drivers.forEach(driver -> {
            driver.get("https://stackoverflow.com/");
            MainPage page = new MainPage(driver);
            page.getSearchField().click();
            page.getAskQuestionButtonInSearchSection().click();
            assertNotEquals("https://stackoverflow.com/questions/ask", driver.getCurrentUrl());
            assertTrue(driver.getCurrentUrl().startsWith("https://stackoverflow.com/users/login"));
        });
    }
}
