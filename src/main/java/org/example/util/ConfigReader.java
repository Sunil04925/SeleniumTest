package org.example.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";

    static {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Failed to load config.properties file: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        String value = System.getProperty(key); // Check system properties first (can be set via command line)
        if (value == null) {
            value = properties.getProperty(key); // Then check config file
        }
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    // Specific methods for common configurations
    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static int getDefaultTimeout() {
        return Integer.parseInt(getProperty("default.timeout", "30"));
    }
}