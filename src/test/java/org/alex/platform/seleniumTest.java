package org.alex.platform;

import org.alex.platform.config.DriverPathConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class seleniumTest {
    @Autowired
    DriverPathConfig driverPathConfig;
    @Test
    public void testOps() {
        System.setProperty("webdriver.chrome.driver", driverPathConfig.getChrome());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        WebDriverWait webDriverWait = new WebDriverWait(driver,3);
        driver.get("http://localhost:7778/#/ifCase");
        driver.findElementByXPath("//*[@id=\"app\"]/div/section/form/div[1]/div/div/input").sendKeys(Keys.ADD);
        driver.findElementByXPath("//*[@id=\"app\"]/div/section/form/div[2]/div/div/input").sendKeys("123");
        driver.findElementByXPath("//*[@id=\"app\"]/div/section/form/div[3]/div/button").click();
    }
}
