package selenium_liveness;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class LivenessTestService {

    private static Capabilities chromeCapabilities = DesiredCapabilities.chrome();
    private static Capabilities firefoxCapabilities = DesiredCapabilities.firefox();

    @Value("${port:4444}")
    private Integer port;

    @Value("${host:selenium-hub}")
    private String host;

    private final static String GRID_HUB_URL = "http://selenium-hub:4444/wd/hub";

    private static final Counter tests = Counter.build()
            .name("tests_total").help("Total tests run.").register();

    private static final Gauge success = Gauge.build()
            .name("tests_successful")
            .help("Was last test run successful?")
            .register();

    @Scheduled(fixedDelay = 5000)
    public void startSelenium() {
        tests.inc();
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
            success.set(0);
            return;
        }
        success.set(1);
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