package org.example.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import java.io.File;

public class BrowserFactory {
    // Default browser version if not specified in testng.xml
    private static final String DEFAULT_VERSION = "latest";

    /**
     * Creates and returns a WebDriver instance based on specified browser, version, and OS.
     * @param browser The browser type (chrome, firefox, edge)
     * @param browserVersion The specific browser version (or "latest")
     * @param os The operating system (e.g., Windows, Mac OS X)
     * @return WebDriver instance for the specified browser
     */
    public static WebDriver getDriver(String browser, String browserVersion, String os) {
        WebDriver driver;
        // Use specified version or fallback to "latest" if not provided
        String version = browserVersion != null && !browserVersion.isEmpty() ? browserVersion : DEFAULT_VERSION;

        // Switch based on browser type (case-insensitive)
        switch (browser.toLowerCase()) {
            case "chrome":
                // Set up ChromeDriver using WebDriverManager (downloads compatible version)
                WebDriverManager.chromedriver().setup();
                // Configure Chrome options for better performance
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--no-sandbox"); // Bypass OS security model for faster startup
                chromeOptions.addArguments("--disable-dev-shm-usage"); // Avoid shared memory issues
                // Uncomment below for headless mode (no UI, faster execution)
                // chromeOptions.addArguments("--headless");
                // If a specific version is requested, configure WebDriverManager accordingly
                if (!version.equals("latest")) {
                    WebDriverManager.chromedriver().browserVersion(version).setup();
                }
                // Instantiate ChromeDriver with the configured options
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                // Set up geckodriver for Firefox using WebDriverManager
                WebDriverManager.firefoxdriver().setup();
                // Configure Firefox options
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--no-sandbox"); // Improve startup compatibility

                // Specify Firefox binary path for macOS (adjust for other OS if needed)
                String firefoxBinaryPath = "/Applications/Firefox.app/Contents/MacOS/firefox";
                File firefoxBinary = new File(firefoxBinaryPath);
                // Validate that the Firefox binary exists
                if (!firefoxBinary.exists()) {
                    throw new IllegalStateException("Firefox binary not found at: " + firefoxBinaryPath +
                            ". Please ensure Firefox is installed or update the path.");
                }
                // Set the binary path in Firefox options
                firefoxOptions.setBinary(firefoxBinaryPath);

                // If a specific version is requested, configure WebDriverManager
                if (!version.equals("latest")) {
                    WebDriverManager.firefoxdriver().browserVersion(version).setup();
                }
                // Instantiate FirefoxDriver with the configured options
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                // Set up EdgeDriver using WebDriverManager
                WebDriverManager.edgedriver().setup();
                // Configure Edge options
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--no-sandbox"); // Improve startup performance
                // If a specific version is requested, configure WebDriverManager
                if (!version.equals("latest")) {
                    WebDriverManager.edgedriver().browserVersion(version).setup();
                }
                // Instantiate EdgeDriver with the configured options
                driver = new EdgeDriver(edgeOptions);
                break;

            default:
                // Throw an exception if an unsupported browser is specified
                throw new IllegalArgumentException("Browser " + browser + " is not supported");
        }

        // Return the initialized WebDriver instance
        return driver;
    }
}