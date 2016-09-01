package nl.newnexus.lab.adapters;

import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.model.LayoutReport;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.nl.Als;
import cucumber.api.java.nl.Dan;
import cucumber.api.java.nl.En;
import cucumber.api.java.nl.Gegeven;
import nl.newnexus.lab.framework.ParentStep;

import static java.util.Arrays.asList;

/**
 * Created by robertvanbuiten on 01-09-16.
 */
public class StepDefinitions extends ParentStep {

    @Before
    public void startTest(Scenario scenario)
    {
        initTest(scenario);
    }

    @After
    public void createGalenReport(Scenario scenario)
    {
        teardownTest(scenario);
    }


    @Gegeven("^Start new nexus$")
    public void startNewNexus() throws Throwable {

        setBrowserType("chrome");
        driver = createDriver(null);
        load("http://www.newnexus.nl");
        String item = getDriver().getTitle();
        log.info(item);

        // Write code here that turns the phrase above into concrete actions
        LayoutReport layoutReport = Galen.checkLayout(driver, "/specs/example.spec", asList("desktop"));

        // Creating an object that will contain the information about the test
        GalenTestInfo test = GalenTestInfo.fromString("Start pagina");
        test.getReport().layout(layoutReport, "Start pagina");
        tests.add(test);



    }

}
