package org.example.base;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.example.util.ConfigReader;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BaseLib {
    protected WebDriver driver;
    protected ExtentTest test;

    @Parameters({"browser", "browserVersion", "os"})
    @BeforeMethod
    public void setUp(@Optional String browser, @Optional String browserVersion, @Optional String os) {
        try {
            String finalBrowser = browser != null ? browser : ConfigReader.getProperty("browser");
            String finalVersion = browserVersion != null ? browserVersion : ConfigReader.getProperty("browser.version");
            String finalOs = os != null ? os : ConfigReader.getProperty("os");

            driver = BrowserFactory.getDriver(finalBrowser, finalVersion, finalOs);
            if (driver == null) {
                throw new RuntimeException("Failed to initialize WebDriver for browser: " + finalBrowser);
            }

            test = ExtentReportManager.createTest(getClass().getSimpleName() + " - " + finalBrowser);
            test.log(Status.INFO, "Browser: " + finalBrowser + ", Version: " + finalVersion + ", OS: " + finalOs);

            driver.get(ConfigReader.getProperty("url"));
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(ConfigReader.getDefaultTimeout(), TimeUnit.SECONDS);

            test.log(Status.INFO, "Navigated to: " + ConfigReader.getProperty("url"));
        } catch (Exception e) {
            String errorMsg = "Setup failed: " + e.getMessage();
            if (test != null) {
                test.log(Status.FAIL, errorMsg);
            }
            throw new RuntimeException(errorMsg, e);
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                String screenshotPath = captureScreenshot(result.getName());
                test.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());
                test.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                test.log(Status.PASS, "Test Passed");
            } else if (result.getStatus() == ITestResult.SKIP) {
                test.log(Status.SKIP, "Test Skipped");
            }
        } catch (Exception e) {
            test.log(Status.WARNING, "Error during teardown: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
                test.log(Status.INFO, "Browser closed");
            }
            ExtentReportManager.flush();
        }
    }

    private String captureScreenshot(String testName) {
        String screenshotPath = System.getProperty("user.dir") + "/Screenshots/" + testName + "_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";

        File screenshotDir = new File(System.getProperty("user.dir") + "/Screenshots");
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }

        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        try {
            Files.copy(source.toPath(), Paths.get(screenshotPath));
            return screenshotPath;
        } catch (IOException e) {
            test.log(Status.WARNING, "Failed to save screenshot: " + e.getMessage());
            return null;
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public ExtentTest getTest() {
        return test;
    }
}