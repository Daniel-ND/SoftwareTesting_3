package ru.itmo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import ru.itmo.pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginPageTest {
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
            driver.get("https://stackoverflow.com/users/login");
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
    public void loginWithWrongEmail() {
        drivers.forEach(driver -> {
            var loginPage = new LoginPage(driver);
            loginPage.login("kek", "lol");
            assertTrue(loginPage.isErrorAppear());
        });
    }

    @Test
    public void loginWithWrongPassword() {
        drivers.forEach(driver -> {
            var loginPage = new LoginPage(driver);
            loginPage.login("ohz71219@eoopy.com", "lol");
            assertTrue(loginPage.isErrorAppear());
        });
    }


    @Test
    public void loginViaEmail() {
        drivers.forEach(driver -> {
            var loginPage = new LoginPage(driver);
            loginPage.login("ohz71219@eoopy.com", "pass1234");
            loginPage.waitForUrl("https://stackoverflow.com/");
            assertEquals("https://stackoverflow.com/", driver.getCurrentUrl());
        });
    }

    @Test
    public void testLoginViaGoogle() {
        drivers.forEach(driver -> {
            var loginPage = new LoginPage(driver);
            loginPage.getLoginWithGoogleButtonPath().click();
            if (Utils.waitForCaptchaIfExists(driver)) {
                assertNotEquals("https://stackoverflow.com/", driver.getCurrentUrl());
                return;
            }
            assertNotEquals("https://stackoverflow.com/users/login", driver.getCurrentUrl());
            assertTrue(driver.getCurrentUrl().startsWith("https://accounts.google.com/"));
        });
    }

    @Test
    public void testLoginViaFacebook() {
        drivers.forEach(driver -> {
            var loginPage = new LoginPage(driver);
            loginPage.getLoginWithFacebookButtonPath().click();

            if (Utils.waitForCaptchaIfExists(driver)) {
                assertNotEquals("https://stackoverflow.com/", driver.getCurrentUrl());
                return;
            }

            assertNotEquals("https://stackoverflow.com/users/login", driver.getCurrentUrl());
            assertTrue(driver.getCurrentUrl().startsWith("https://www.facebook.com/login.php?"));
        });
    }

    @Test
    public void testLoginViaGithub() {
        drivers.forEach(driver -> {
            var loginPage = new LoginPage(driver);
            loginPage.getLoginWithGithubButtonPath().click();

            if (Utils.waitForCaptchaIfExists(driver)) {
                assertNotEquals("https://stackoverflow.com/", driver.getCurrentUrl());
                return;
            }

            assertNotEquals("https://stackoverflow.com/users/login", driver.getCurrentUrl());
            assertTrue(driver.getCurrentUrl().startsWith("https://github.com/login?client_id"));
        });
    }
}
