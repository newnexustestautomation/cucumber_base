package nl.newnexus.lab.framework;

/**
 * Created by robertvanbuiten on 01-09-16.
 */
import org.openqa.selenium.WebDriver;

public abstract class ParentPage extends DSL {

    public ParentPage(WebDriver driver) {
        super(driver);
    }

}
