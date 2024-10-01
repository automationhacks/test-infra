package io.automationhacks.testinfra.constants;

import io.automationhacks.testinfra.file.ConfigReader;

public class SysProps {

    static {
        ConfigReader.loadProperties();
    }

    public static String getSlackChannel() {
        return System.getProperty("slack.channel", "#test-alerts");
    }
}
