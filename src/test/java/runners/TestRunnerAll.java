package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"src/test/java/features"},
        glue = {"stepDefinitions"},
        plugin = {
                "pretty",
                "html:target/cucumber-report-all.html",
                "json:target/cucumber-report-all.json"
        },
        dryRun = false
)
public class TestRunnerAll extends AbstractTestNGCucumberTests {
}
