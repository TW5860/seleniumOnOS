package selenium_liveness;

import com.google.common.collect.ImmutableMap;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
public class LivenessTestService {

    private static final Logger LOG = LoggerFactory.getLogger(LivenessTestService.class);

    // private static Capabilities chromeCapabilities = DesiredCapabilities.chrome();
    private static Capabilities firefoxCapabilities = DesiredCapabilities.firefox();

    private String gridHubUrl;
    private static final UriTemplate GRID_URI_TEMPLATE = new UriTemplate("http://{host}:{port}/wd/hub");

    @Autowired
    public LivenessTestService(@Value("${selenium-hub.host:selenium-hub}") String host,
                               @Value("${selenium-hub.http-port:4444}") String port) {
        System.out.println(port);
        gridHubUrl = GRID_URI_TEMPLATE.expand(ImmutableMap.of(
                "host", host,
                "port", port)).toString();
    }

    private static final Counter tests = Counter.build()
            .name("tests_total").help("Total tests run.").register();

    private static final Gauge success = Gauge.build()
            .name("tests_successful")
            .help("Was last test run successful?")
            .register();

    @Scheduled(fixedDelay = 15000)
    public void startSelenium() {
        tests.inc();
        try {
            LOG.info("Starting test with Selenium Hub at: {}", gridHubUrl);

            // runWithChrome();

            LOG.info("Run against Firefox.");
            runWithFirefox();

        } catch (Exception e) {
            LOG.info("Test failed, caught exception.", e);
            success.set(0);
            return;
        }
        LOG.info("Test was successful.");
        success.set(1);
    }

    private void runWithFirefox() throws MalformedURLException {
        RemoteWebDriver drive = new RemoteWebDriver(new URL(gridHubUrl), firefoxCapabilities);
        drive.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        drive.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        drive.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
        runGoogleTest(drive);
    }
//
//    private void runWithChrome() throws MalformedURLException {
//        RemoteWebDriver driver = new RemoteWebDriver(new URL(gridHubUrl), chromeCapabilities);
//        runGoogleTest(driver);
//    }

    private void runGoogleTest(RemoteWebDriver driver) {
        LOG.info("Getting google.com");
        driver.get("https://www.google.com");
        LOG.info("Clicking a button");
        driver.findElement(By.cssSelector("#tsf > div.tsf-p > div.jsb > center > input[type=\"submit\"]:nth-child(2)")).click();
        LOG.info("Browser title is now: {}", driver.getTitle());
        driver.quit();
    }
}