package io.automationhacks.testinfra.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final String CONFIG_FILE = "/config.properties";
    private static final Properties properties = new Properties();

    public static void loadProperties() {
        try (InputStream input = ConfigReader.class.getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                System.out.println("Sorry, unable to find " + CONFIG_FILE);
                return;
            }

            properties.load(input);
            properties.forEach(
                    (key, value) -> System.setProperty(key.toString(), value.toString()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
