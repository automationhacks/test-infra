package io.automationhacks.testinfra;

import com.google.gson.Gson;

import io.automationhacks.testinfra.config.ConfigurationLoader;
import io.automationhacks.testinfra.model.report_portal.testitem.get_test_items_response.GetItemResponse;

import lombok.Data;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReportPortalClient {
    private static final Logger logger =
            LoggerFactory.getLogger(ReportPortalClient.class.getName());
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
        var launchId = getLatestLaunchId();
        var itemsResponse = getFailedItems(launchId);
        return parseFailedTestItems(itemsResponse);
    }

    @NotNull
    private String getLatestLaunchId() throws IOException {
        String latestLaunchEndpoint =
                String.format("%s/api/v1/%s/launch/latest", baseUrl, projectName);
        String launchResponse = makeRequest(latestLaunchEndpoint, "application/json");
        logger.info("Latest launch response: " + launchResponse);

        String launchId = extractLaunchIdFromLatest(launchResponse);
        if (launchId == null) {
            throw new IOException("Could not find latest launch ID");
        }
        return launchId;
    }

    private String getFailedItems(String launchId) throws IOException {
        String itemsEndpoint =
                String.format(
                        "%s/api/v1/%s/item/v2?filter.in.type=STEP&filter.in.status=FAILED&providerType=launch&launchId=%s",
                        baseUrl, projectName, launchId);
        String itemsResponse = makeRequest(itemsEndpoint, "application/json");
        logger.info("Failed items response: " + itemsResponse);

        return itemsResponse;
    }

    public String fetchStackTrace(String testItemId) throws IOException {
        String endpoint =
                String.format("%s/api/v1/%s/item/%s/log", baseUrl, projectName, testItemId);
        logger.info("Fetching stack trace for test item: " + testItemId);
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
            logger.error("Error parsing launch response", e);
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
            String error =
                    String.format(
                            "Request failed with HTTP %d for endpoint: %s. Error: %s",
                            responseCode, endpoint, errorMessage);
            logger.error(error);
            throw new IOException(error);
        }
    }

    private List<FailedTestItem> parseFailedTestItems(String response) throws JSONException {
        List<FailedTestItem> failedTests = new ArrayList<>();

        var getItemResponse = new Gson().fromJson(response, GetItemResponse.class);

        for (var item : getItemResponse.getContent()) {
            FailedTestItem testItem = new FailedTestItem();

            testItem.setId(String.valueOf(item.getId()));
            testItem.setName(item.getName());
            testItem.setFullName(item.getCodeRef());
            testItem.setStackTrace(item.getDescription());

            failedTests.add(testItem);
        }

        return failedTests;
    }

    @Data
    public static class FailedTestItem {
        private String id;
        private String name;
        private String fullName;
        private String stackTrace;
    }
}
