package ru.itmo;

import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class CaptchaAnalyzer {
    public static boolean isCaptchaSolved(WebDriver driver, WebElement captchaElement) {
        var bytes = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES); // Taking full screenshot so browser does not flicker
        BufferedImage image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        var pageOffset = Double.parseDouble(((JavascriptExecutor)driver).executeScript("return window.pageYOffset").toString());
        var scale = Double.parseDouble(((JavascriptExecutor)driver).executeScript("return window.devicePixelRatio").toString());
        var x = (int)(captchaElement.getLocation().x*scale);
        var y = (int)((captchaElement.getLocation().y - pageOffset)*scale);
        var w = (int)(captchaElement.getSize().width*scale);
        var h = (int)(captchaElement.getSize().height*scale);

        if (x + w >= image.getWidth() || y + h >= image.getHeight()) {
            return false;
        }
        var rawRgba = image.getData().getPixels(x, y, w, h, (int[])null);
        var cnt = 0;
        for (int i = 0; i * 4 < rawRgba.length; i++) {
            var r = rawRgba[i * 4 + 0];
            var g = rawRgba[i * 4 + 1];
            var b = rawRgba[i * 4 + 2];
            if (r < 100 && g > 150 & b < 95)
                cnt++;
        }
        System.out.println("Found green pixels:" + cnt);
        return cnt > 0.0002 * rawRgba.length;
    }
}
