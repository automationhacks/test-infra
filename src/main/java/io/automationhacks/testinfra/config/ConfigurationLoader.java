package io.automationhacks.testinfra.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationLoader {
    private static final Logger logger = Logger.getLogger(ConfigurationLoader.class.getName());
    private static final String CONFIG_FILE = "reportportal-analyzer.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = ConfigurationLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading configuration", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static String getReportPortalBaseUrl() {
        return properties.getProperty("reportportal.baseUrl");
    }

    public static String getReportPortalProject() {
        return properties.getProperty("reportportal.project");
    }

    public static String getReportPortalAuthToken() {
        return properties.getProperty("reportportal.authToken");
    }

    public static boolean isSchedulerEnabled() {
        return Boolean.parseBoolean(properties.getProperty("analysis.scheduler.enabled", "false"));
    }

    public static long getSchedulerInterval() {
        return Long.parseLong(properties.getProperty("analysis.scheduler.interval", "3600000"));
    }
}