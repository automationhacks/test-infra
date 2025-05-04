package io.automationhacks.testinfra.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationLoader {
    private static final Logger logger = Logger.getLogger(ConfigurationLoader.class.getName());
    private static final String RP_CONFIG_FILE = "reportportal.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = ConfigurationLoader.class.getClassLoader().getResourceAsStream(RP_CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + RP_CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading Report Portal configuration", e);
            throw new RuntimeException("Failed to load Report Portal configuration", e);
        }
    }

    public static String getReportPortalBaseUrl() {
        return properties.getProperty("rp.endpoint");
    }

    public static String getReportPortalProject() {
        return properties.getProperty("rp.project");
    }

    public static String getReportPortalAuthToken() {
        return properties.getProperty("rp.api.key");
    }
}