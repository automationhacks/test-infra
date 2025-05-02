package io.automationhacks.testinfra;

import io.automationhacks.testinfra.config.ConfigurationLoader;
import io.automationhacks.testinfra.model.report_portal.launch.GetLatestLaunchByProjectResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

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
        // First get the latest launch ID
        String latestLaunchEndpoint = String.format("%s/%s/launch/latest", baseUrl, projectName);
        String launchResponse = makeRequest(latestLaunchEndpoint);
        logger.fine("Latest launch response: " + launchResponse);

        // Parse launch response to get ID
        String launchId = extractLaunchIdFromLatest(launchResponse);
        if (launchId == null) {
            throw new IOException("Could not find latest launch ID");
        }

        // Construct filter for test items
        String itemsEndpoint = String.format("%s/%s/item?filter.eq.status=%s&filter.eq.launch=%s",
                baseUrl, projectName, status, launchId);
        logger.fine("Fetching test items from: " + itemsEndpoint);
        return makeRequest(itemsEndpoint);
    }

    private String extractLaunchIdFromLatest(String response) {
        try {
            var gson = new Gson();
            var launchResponse = (GetLatestLaunchByProjectResponse) gson.fromJson(response,
                    GetLatestLaunchByProjectResponse.class);
            return String.valueOf(launchResponse.getContent().get(0).getNumber());

        } catch (JSONException e) {
            logger.log(Level.SEVERE, "Error parsing launch response", e);
        }
        return null;
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
        connection.setRequestProperty("Authorization", "bearer " + authToken);
        connection.setRequestProperty("Accept", "application/json");

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
            String errorMessage;
            try (Scanner scanner = new Scanner(connection.getErrorStream())) {
                StringBuilder error = new StringBuilder();
                while (scanner.hasNextLine()) {
                    error.append(scanner.nextLine());
                }
                errorMessage = error.toString();
            }
            String error = String.format("Request failed with HTTP %d for endpoint: %s. Error: %s",
                    responseCode, endpoint, errorMessage);
            logger.log(Level.SEVERE, error);
            throw new IOException(error);
        }
    }
}