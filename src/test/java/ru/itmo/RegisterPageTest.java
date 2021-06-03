package ru.itmo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.FluentWait;
import ru.itmo.pages.LoginPage;
import ru.itmo.pages.RegisterPage;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterPageTest {
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
            driver.get("https://stackoverflow.com/users/signup");
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
    public void testRegisterViaGithub() {
        drivers.forEach(driver -> {
            var registerPage = new RegisterPage(driver);
            registerPage.getRegisterWithGithubButton().click();
            Utils.waitForCaptchaIfExists(driver);
            assertNotEquals("https://stackoverflow.com/users/signup", driver.getCurrentUrl());
            assertTrue(driver.getCurrentUrl().startsWith("https://github.com/login"));
        });
    }

    @Test
    public void testRegisterViaFacebook() {
        drivers.forEach(driver -> {
            var registerPage = new RegisterPage(driver);
            registerPage.getRegisterWithFacebookButton().click();
            Utils.waitForCaptchaIfExists(driver);
            assertTrue(driver.getCurrentUrl().startsWith("https://www.facebook.com/login.php?"));
        });
    }

    @Test
    public void testRegisterViaGoogle() {
        drivers.forEach(driver -> {
            var registerPage = new RegisterPage(driver);
            registerPage.getRegisterWithGoogleButton().click();
            Utils.waitForCaptchaIfExists(driver);
            assertNotEquals("https://stackoverflow.com/users/signup", driver.getCurrentUrl());
            assertTrue(driver.getCurrentUrl().startsWith("https://accounts.google.com/"));
        });
    }

    @Test
    public void testRegister() {
        drivers.forEach(driver -> {
            var registerPage = new RegisterPage(driver);
            registerPage.getCaptcha().click();
            registerPage.getName().sendKeys("new_user_test");
            registerPage.getEmail().sendKeys("new_user_email"
                    + System.currentTimeMillis()
                    + "@register.com");
            registerPage.getPassword().sendKeys("pass1234");

            new FluentWait<>(driver)
                    .pollingEvery(Duration.ofSeconds(2))
                    .withTimeout(Duration.ofMinutes(10))
                    .ignoring(StaleElementReferenceException.class)
                .until(dummyParam -> CaptchaAnalyzer.isCaptchaSolved(
                        driver, registerPage.getCaptchaElement()
                ));
            registerPage.getRegisterButton().click();
            assertTrue(registerPage.isRegistrationSucceeded(driver));
        });
    }

}
