package selenium_liveness;

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
public class LivenessTestController {

    private static Capabilities chromeCapabilities = DesiredCapabilities.chrome();
    private static Capabilities firefoxCapabilities = DesiredCapabilities.firefox();

    @Value("${port:4444}")
    private Integer port;

    @Value("${host:selenium-hub}")
    private String host;

    private final static String GRID_HUB_URL = "http://selenium-hub:4444/wd/hub";

    @RequestMapping("uitest")
    public String startSelenium() {
        try {
            System.out.println(host);
            System.out.println(port);
            System.out.println(GRID_HUB_URL);
            // run against chrome
            runWithChrome();

            // run against firefox
            runWithFireFox();

        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL!";
        }
        return "SUCCESS!";
    }

    private void runWithFireFox() throws MalformedURLException {
        System.out.println(GRID_HUB_URL);
        RemoteWebDriver drive = new RemoteWebDriver(new URL(GRID_HUB_URL), firefoxCapabilities);
        runGoogleTest(drive);
    }

    private void runWithChrome() throws MalformedURLException {
        RemoteWebDriver driver = new RemoteWebDriver(new URL(GRID_HUB_URL), chromeCapabilities);
        runGoogleTest(driver);
    }

    private void runGoogleTest(RemoteWebDriver driver) {
        driver.get("https://www.google.com");
        driver.findElement(By.cssSelector("#tsf > div.tsf-p > div.jsb > center > input[type=\"submit\"]:nth-child(2)")).click();
        System.out.println(driver.getTitle());
        driver.quit();
    }
}