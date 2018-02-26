package bootwildfly;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;

@RestController
public class HelloWildFlyController {

    public static Capabilities chromeCapabilities = DesiredCapabilities.chrome();
    public static Capabilities firefoxCapabilities = DesiredCapabilities.firefox();

    @Value("${port:4444}")
    public Integer port;

    @Value("${host:selenium-hub}")
    public String host;

    private String grid_hub_url = "http://" + host + ":" + port + "/wd/hub";

    @RequestMapping("hello")
    public String sayHello() {
        return ("Hello, SpringBoot on Wildfly");
    }

    @RequestMapping("uitest")
    public String startSelenium() {
        try {
            // run against chrome
//            runWithChrome();

            // run against firefox
            runWithFireFox();

        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL!";
        }
        return "SUCCESS!";
    }

    private void runWithFireFox() throws MalformedURLException {
        RemoteWebDriver drive = new RemoteWebDriver(new URL(grid_hub_url), firefoxCapabilities);
        runGoogleTest(drive);
    }

    private void runWithChrome() throws MalformedURLException {
        RemoteWebDriver driver = new RemoteWebDriver(new URL(grid_hub_url), chromeCapabilities);
        runGoogleTest(driver);
    }

    private void runGoogleTest(RemoteWebDriver driver) {
        driver.get("https://www.google.com");
        driver.findElement(By.cssSelector("#tsf > div.tsf-p > div.jsb > center > input[type=\"submit\"]:nth-child(2)")).click();
        System.out.println(driver.getTitle());
        driver.quit();
    }
}