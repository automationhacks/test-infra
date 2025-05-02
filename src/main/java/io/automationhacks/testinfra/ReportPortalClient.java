package io.automationhacks.testinfra;

import io.automationhacks.testinfra.config.ConfigurationLoader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportPortalClient {
    private static final Logger logger = Logger.getLogger(ReportPortalClient.class.getName());
    private final String baseUrl;
    private final String projectName;
    private final String authToken;

    public ReportPortalClient() {
        this.baseUrl = ConfigurationLoader.getReportPortalBaseUrl();
        this.projectName = ConfigurationLoader.getReportPortalProject();
        this.authToken = ConfigurationLoader.getReportPortalAuthToken();
        logger.info("Initialized Report Portal client for project: " + projectName);
    }

    public String fetchTestItems(String status) throws IOException {
        String endpoint = String.format("%s/%s/item?filter.eq.status=%s", baseUrl, projectName, status);
        logger.fine("Fetching test items from: " + endpoint);
        return makeRequest(endpoint);
    }

    public String fetchStackTrace(String testItemId) throws IOException {
        String endpoint = String.format("%s/%s/item/%s/log", baseUrl, projectName, testItemId);
        logger.fine("Fetching stack trace for test item: " + testItemId);
        return makeRequest(endpoint);
    }

    private String makeRequest(String endpoint) throws IOException {
        URL url = URI.create(endpoint).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + authToken);

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }
                return response.toString();
            }
        } else {
            String error = String.format("Request failed with HTTP %d for endpoint: %s", responseCode, endpoint);
            logger.log(Level.SEVERE, error);
            throw new IOException(error);
        }
    }
}