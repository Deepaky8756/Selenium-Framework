package base;

import java.io.File;
import java.time.Duration;

import utils.Constants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.sun.org.apache.bcel.internal.classfile.Method;

import io.github.bonigarcia.wdm.WebDriverManager;



public class baseTest{
	ExtentHtmlReporter reporter;
	ExtentReports extent;
	public static  WebDriver driver;
	ExtentTest logger;

	@BeforeTest
	public void beforeTestMethod() {

		String path=System.getProperty("user.dir")+File.separator+"reports"+File.separator+"index.html";
		reporter = new ExtentHtmlReporter(path);
		reporter.config().setReportName("WebAutomation Report");
		reporter.config().setDocumentTitle("result");

		extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Tester", "Deepak");
	}

	@BeforeMethod
	@Parameters("Browser")
	public void beforeMethod(String browser) {
		logger=extent.createTest("First test");
		setupDriver(browser);
		driver.manage().window().maximize();
		driver.get(Constants.url);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		if(result.getStatus()==ITestResult.FAILURE) {
			logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+"- Test Case Failed", ExtentColor.RED));
			logger.log(Status.FAIL, MarkupHelper.createLabel(result.getThrowable()+"- Test Case Failed", ExtentColor.RED));
		}else if(result.getStatus()==ITestResult.SKIP) {
			logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName()+"- Test Case skiped", ExtentColor.ORANGE));
		}else if(result.getStatus()==ITestResult.SUCCESS) {
			logger.log(Status.PASS, MarkupHelper.createLabel(result.getName()+"- Test Case pass", ExtentColor.GREEN));
		}
		driver.quit();
	}
	
	@AfterTest
	public void afterTestMethod() {
		extent.flush();
	}
	public void setupDriver(String browser) {
		if(browser.equalsIgnoreCase("Chrome")) {
		//	System.setProperty("webdriver.chrome.driver", "F:\\Deepak\\Java_Selenium_full_course\\SeleniumFramework\\driver\\chromedriver-win64\\chromedriver.exe");
			 WebDriverManager.chromedriver().setup();
			driver=new ChromeDriver();
		}

	}
}
