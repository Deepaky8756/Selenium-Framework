package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        tags = "@Regression",
        features = {"src/test/java/features"},
        glue = {"stepDefinitions"},
        plugin = {
                "pretty",
                "html:target/cucumber-report-regression.html",
                "json:target/cucumber-report-regression.json"
        },
        dryRun = false
)
public class TestRunnerRegression extends AbstractTestNGCucumberTests {
}
