package org.example.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>(); // Thread-safe for parallel execution

    static {
        try {
            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String reportPath = System.getProperty("user.dir") + "/TestReports/ExtentReport_" + timestamp + ".html";

            File reportDir = new File(System.getProperty("user.dir") + "/TestReports");
            if (!reportDir.exists()) {
                if (!reportDir.mkdirs()) {
                    throw new RuntimeException("Failed to create TestReports directory");
                }
            }

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Advanced Selenium Test Report");
            sparkReporter.config().setReportName("Test Execution Summary");
            sparkReporter.config().setTheme(Theme.DARK); // Dark theme for better visuals
            sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
            sparkReporter.config().setEncoding("UTF-8");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Tester", System.getProperty("user.name"));

            System.out.println("ExtentReports initialized at: " + reportPath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ExtentReports: " + e.getMessage(), e);
        }
    }

    public static ExtentTest createTest(String testName) {
        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void flush() {
        try {
            if (extent != null) {
                extent.flush();
                System.out.println("Extent report flushed successfully");
            }
        } catch (Exception e) {
            System.err.println("Failed to flush Extent report: " + e.getMessage());
        }
    }
}