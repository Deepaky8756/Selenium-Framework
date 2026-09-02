package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        tags = "@Smoke",
        features = {"src/test/java/features"},
        glue = {"stepDefinitions"},
        plugin = {
                "pretty",
                "html:target/cucumber-report-smoke.html",
                "json:target/cucumber-report-smoke.json"
        },
        dryRun = false
)
public class TestRunnerSmoke extends AbstractTestNGCucumberTests {
}
