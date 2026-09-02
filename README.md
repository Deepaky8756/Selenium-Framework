# Selenium-Framework

## Jenkins Integration

This project is Maven-based and is ready to run from Jenkins using the `Jenkinsfile` in the project root.

### Jenkins prerequisites

Install/configure on the Jenkins agent:
- JDK 21
- Maven 3.x
- Git
- Chrome, Firefox or Edge (depending on the selected browser)

### Jenkins job

Create a **Pipeline** job and select **Pipeline script from SCM**. Use Git as the SCM, select the repository/branch, and set the script path to:

```text
Jenkinsfile
```

### Parameters

The pipeline exposes:
- `TEST_SUITE`: Smoke, Regression or All
- `BROWSER`: chrome, firefox or edge
- `HEADLESS`: true or false

The default Jenkins execution uses headless Chrome.

### Equivalent Maven commands

```bash
mvn clean test -Dtest=TestRunnerSmoke -Dbrowser=chrome -Dheadless=true
mvn clean test -Dtest=TestRunnerRegression -Dbrowser=chrome -Dheadless=true
mvn clean test -Dtest=TestRunnerAll -Dbrowser=chrome -Dheadless=true
```

Cucumber HTML/JSON reports are generated under `target/` and Jenkins archives the generated test reports.
