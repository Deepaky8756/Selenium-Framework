package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriverManager {
    private static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();
    private static final ThreadLocal<String> threadBrowserName = new ThreadLocal<>();

    public static WebDriver getDriver() {

        Locale.setDefault(Locale.ENGLISH);
        System.setProperty("user.language", "en");

        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.SEVERE);

        if (threadBrowserName.get() == null) {
            threadBrowserName.set(System.getProperty("browser", "chrome").toLowerCase(Locale.ROOT));
        }

        if (threadDriver.get() == null) {
            threadDriver.set(createDriver(threadBrowserName.get()));
        }

        WebDriver driver = threadDriver.get();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        if (headless) {
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
        } else {
            driver.manage().window().maximize();
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }

    private static WebDriver createDriver(String browser) {
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        switch (browser) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                }
                return new FirefoxDriver(firefoxOptions);

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--no-sandbox");
                    edgeOptions.addArguments("--disable-dev-shm-usage");
                    edgeOptions.addArguments("--window-size=1920,1080");
                }
                return new EdgeDriver(edgeOptions);

            case "safari":
                if (headless) {
                    throw new IllegalArgumentException("Safari does not support headless execution. Use Chrome, Firefox or Edge.");
                }
                return new SafariDriver();

            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                return new ChromeDriver(chromeOptions);
        }
    }

    public static void quitDriver() {
        WebDriver driver = threadDriver.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                threadDriver.remove();
                threadBrowserName.remove();
            }
        }
    }

    public static String getThreadBrowserName() {
        return threadBrowserName.get();
    }

    public static void setThreadBrowserName(String browser) {
        threadBrowserName.set(browser == null ? "chrome" : browser.toLowerCase(Locale.ROOT));
    }
}
