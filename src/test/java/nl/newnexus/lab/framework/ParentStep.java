package nl.newnexus.lab.framework;

/**
 * Created by robertvanbuiten on 01-09-16.
 */

import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.testng.GalenTestNgTestBase;
import cucumber.api.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ParentStep extends GalenTestNgTestBase
{

    protected static Logger log = LogManager.getLogger(ParentStep.class);

    protected WebDriver driver;
    protected List<GalenTestInfo> tests =  new LinkedList<GalenTestInfo>();
    protected String testcase;

    public static void setBrowserType(String browserType) {
        ParentStep.browserType = browserType;
    }
    private static String browserType = "chrome";

    public void initTest(Scenario scenario) {}

    public void teardownTest(Scenario scenario)
    {
        destroyDriver(scenario);
        HtmlReportBuilder report = new HtmlReportBuilder();
        try {
            report.build(tests, "target/cucumber/galen-html-reports/" + testcase);
        }catch (IOException EX) {}

        destroyDriver(scenario);
    }

    protected void initDriver()
    {
        if (browserType.equalsIgnoreCase("phantom")) {

            Capabilities caps = new DesiredCapabilities();
            ((DesiredCapabilities) caps).setJavascriptEnabled(true);
            ((DesiredCapabilities) caps).setCapability("takesScreenshot", true);
            ((DesiredCapabilities) caps).setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] {"--ssl-protocol=tlsv1"});
            ((DesiredCapabilities) caps).setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "drivers//phantomjs.exe");

            try {
                if (driver == null) {
                    driver = new PhantomJSDriver(caps);
                    driver.manage()
                            .timeouts()
                            .implicitlyWait(5, TimeUnit.SECONDS)
                            .pageLoadTimeout(30, TimeUnit.SECONDS)
                            .setScriptTimeout(30, TimeUnit.SECONDS);
                    driver.manage().deleteAllCookies();
                }
            } catch (Exception ex)
            {
                log.error(ex.getMessage());
            }

        } else if (browserType.equalsIgnoreCase("chrome")) {

            if (driver == null  ) {
                System.setProperty("webdriver.chrome.driver", "drivers//chromedriver.exe");
                driver = new ChromeDriver();
                driver.manage().timeouts()     .implicitlyWait(5, TimeUnit.SECONDS)
                        .pageLoadTimeout(30, TimeUnit.SECONDS)
                        .setScriptTimeout(30, TimeUnit.SECONDS);
                driver.manage().deleteAllCookies();
            }

        } else if (browserType.equalsIgnoreCase("ie")) {

            DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            capabilities.setCapability("requireWindowFocus", true);

            if (driver == null)
            {
                System.setProperty("webdriver.ie.driver", "drivers//iedriverserver.exe");
                driver = new InternetExplorerDriver(capabilities);

            }


        } else if (browserType.equalsIgnoreCase("firefox")) {


        } else if (browserType.equalsIgnoreCase("safari")) {
            if (driver == null) {
                driver = new SafariDriver();
                driver.manage()
                        .timeouts()
                        .implicitlyWait(5, TimeUnit.SECONDS)
                        .pageLoadTimeout(30, TimeUnit.SECONDS)
                        .setScriptTimeout(30, TimeUnit.SECONDS);
                driver.manage().deleteAllCookies();
            }
        }
    }

    @Override
    public WebDriver createDriver(Object[] objects) {
        initDriver();
        return driver;
    }

    public WebDriver getDriver()
    {
        return driver;
    }

    protected void closeDriver() {
        if (driver != null) {
            driver.close();
            driver = null;
        }
    }


    protected void destroyDriver(Scenario scenario)
    {
        if (driver != null) {
            try {
                if (scenario.isFailed()) {
                    takeScreenshot(scenario);
                }
            } finally {
                driver.quit();
            }
        }

        driver = null;
    }

    private void takeScreenshot(Scenario scenario)
    {
        final byte[] screenshot = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES);
        scenario.embed(screenshot, "image/png");
    }


    protected void navigateTo() {
        driver.navigate().to("http://www.tweakers.net");
    }

}
