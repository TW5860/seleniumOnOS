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

    private static Capabilities chromeCapabilities = DesiredCapabilities.chrome();
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
        success.labels("firefox").set(0);
        success.labels("chrome").set(0);
    }

    private static final Counter tests = Counter.build()
            .name("tests_total")
            .help("Total tests run.")
            .labelNames("browser")
            .register();

    private static final Gauge success = Gauge.build()
            .name("tests_successful")
            .help("Was last test run successful?")
            .labelNames("browser")
            .register();


    @Scheduled(fixedDelay = 15000)
    public void scheduledFirefox() {
        tests.labels("firefox").inc();
        try {
            LOG.info("Run against Firefox.");
            runWithFirefox();
            LOG.info("Test with Firefox was successful.");
            success.labels("firefox").set(1);
        } catch (Exception e) {
            LOG.info("Test with Firefox failed, caught exception.", e);
            success.labels("firefox").set(0);
        }
    }

    @Scheduled(fixedDelay = 15000)
    public void scheduledChrome() {
        tests.labels("chrome").inc();
        try {
            LOG.info("Run against Chrome.");
            runWithChrome();
            LOG.info("Test with Chrome was successful.");
            success.labels("chrome").set(1);
        } catch (Exception e) {
            LOG.info("Test with Chrome failed, caught exception.", e);
            success.labels("chrome").set(0);
        }
    }

    private void runWithFirefox() throws MalformedURLException {
        RemoteWebDriver drive = new RemoteWebDriver(new URL(gridHubUrl), firefoxCapabilities);
        runGoogleTest(drive);
    }

    private void runWithChrome() throws MalformedURLException {
        RemoteWebDriver driver = new RemoteWebDriver(new URL(gridHubUrl), chromeCapabilities);
        runGoogleTest(driver);
    }

    private void runGoogleTest(RemoteWebDriver driver) {
        try {
            driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
            LOG.info("Getting google.com");
            driver.get("https://www.google.com");
            LOG.info("Clicking a button");
            driver.findElement(By.cssSelector("#tsf > div.tsf-p > div.jsb > center > input[type=\"submit\"]:nth-child(2)")).click();
            LOG.info("Browser title is now: {}", driver.getTitle());
        } finally {
            driver.quit();
        }
    }
}