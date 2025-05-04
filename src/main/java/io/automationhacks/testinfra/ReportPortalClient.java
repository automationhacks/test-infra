package io.automationhacks.testinfra;

import io.automationhacks.testinfra.config.ConfigurationLoader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public List<FailedTestItem> getFailedTests() throws IOException {
        // Get latest launch ID
        String latestLaunchEndpoint = String.format("%s/api/v1/%s/launch/latest", baseUrl, projectName);
        String launchResponse = makeRequest(latestLaunchEndpoint, "application/json");
        logger.fine("Latest launch response: " + launchResponse);

        String launchId = extractLaunchIdFromLatest(launchResponse);
        if (launchId == null) {
            throw new IOException("Could not find latest launch ID");
        }

        // Get failed test items with correct filter format
        String itemsEndpoint = String.format(
                "%s/api/v1/%s/item?filter.eq.type=STEP&filter.eq.status=FAILED&filter.eq.launch=%s",
                baseUrl, projectName, launchId);
        String itemsResponse = makeRequest(itemsEndpoint, "application/json");
        logger.fine("Failed items response: " + itemsResponse);

        return parseFailedTestItems(itemsResponse);
    }

    public String fetchStackTrace(String testItemId) throws IOException {
        String endpoint = String.format("%s/api/v1/%s/item/%s/log", baseUrl, projectName, testItemId);
        logger.fine("Fetching stack trace for test item: " + testItemId);
        return makeRequest(endpoint, "text/plain");
    }

    private String extractLaunchIdFromLatest(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray content = jsonResponse.getJSONArray("content");
            if (content.length() > 0) {
                JSONObject launch = content.getJSONObject(0);
                return String.valueOf(launch.getInt("id"));
            }
        } catch (JSONException e) {
            logger.log(Level.SEVERE, "Error parsing launch response", e);
        }
        return null;
    }

    private String makeRequest(String endpoint, String acceptType) throws IOException {
        URL url = URI.create(endpoint).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "bearer " + authToken);
        connection.setRequestProperty("Accept", acceptType);

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            try (Scanner scanner = new Scanner(connection.getInputStream(), "UTF-8")) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } else {
            String errorMessage;
            try (Scanner scanner = new Scanner(connection.getErrorStream(), "UTF-8")) {
                scanner.useDelimiter("\\A");
                errorMessage = scanner.hasNext() ? scanner.next() : "";
            }
            String error = String.format("Request failed with HTTP %d for endpoint: %s. Error: %s",
                    responseCode, endpoint, errorMessage);
            logger.log(Level.SEVERE, error);
            throw new IOException(error);
        }
    }

    private List<FailedTestItem> parseFailedTestItems(String response) throws JSONException {
        List<FailedTestItem> failedTests = new ArrayList<>();
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray content = jsonResponse.getJSONArray("content");

        for (int i = 0; i < content.length(); i++) {
            JSONObject item = content.getJSONObject(i);
            FailedTestItem test = new FailedTestItem();
            test.setId(item.getString("id"));
            test.setName(item.getString("name"));
            test.setFullName(item.getString("codeRef"));
            if (item.has("description")) {
                test.setStackTrace(item.getString("description"));
            }
            failedTests.add(test);
        }

        return failedTests;
    }

    public static class FailedTestItem {
        private String id;
        private String name;
        private String fullName;
        private String stackTrace;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getStackTrace() {
            return stackTrace;
        }

        public void setStackTrace(String stackTrace) {
            this.stackTrace = stackTrace;
        }
    }
}