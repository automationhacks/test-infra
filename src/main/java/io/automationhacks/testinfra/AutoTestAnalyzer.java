package io.automationhacks.testinfra;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class AutoTestAnalyzer {
    private static final Logger logger = Logger.getLogger(AutoTestAnalyzer.class.getName());
    private final ReportPortalClient reportPortalClient;
    private final StackTraceAnalyzer stackTraceAnalyzer;
    private final ObjectMapper objectMapper;

    public AutoTestAnalyzer() {
        this.reportPortalClient = new ReportPortalClient();
        this.stackTraceAnalyzer = new StackTraceAnalyzer();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Analyzes failing tests and provides suggestions.
     */
    public void analyzeFailingTests() {
        try {
            String failingTests = reportPortalClient.fetchTestItems("?filter.eq.status=FAILED");
            List<String> testItemIds = extractTestItemIds(failingTests);

            for (String testItemId : testItemIds) {
                try {
                    String stackTrace = reportPortalClient.fetchStackTrace(testItemId);
                    String suggestion = stackTraceAnalyzer.analyze(stackTrace);
                    logAnalysisResult(testItemId, suggestion);
                } catch (IOException e) {
                    logger.warning("Error analyzing test item " + testItemId + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.severe("Error fetching failing tests: " + e.getMessage());
        }
    }

    private List<String> extractTestItemIds(String response) throws IOException {
        List<String> testItemIds = new ArrayList<>();
        JsonNode root = objectMapper.readTree(response);
        JsonNode content = root.path("content");

        if (content.isArray()) {
            for (JsonNode item : content) {
                String id = item.path("id").asText();
                if (!id.isEmpty()) {
                    testItemIds.add(id);
                }
            }
        }

        return testItemIds;
    }

    private void logAnalysisResult(String testItemId, String suggestion) {
        logger.info(String.format("Test Item ID: %s%nSuggestion: %s%n", testItemId, suggestion));
    }

    /**
     * Schedules the analysis to run at a fixed interval.
     *
     * @param intervalMillis Interval in milliseconds.
     */
    public void scheduleAnalysis(long intervalMillis) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                analyzeFailingTests();
            }
        }, 0, intervalMillis);
    }

    public static void main(String[] args) {
        AutoTestAnalyzer analyzer = new AutoTestAnalyzer();

        // Run analysis immediately
        analyzer.analyzeFailingTests();

        // Schedule analysis every hour
        analyzer.scheduleAnalysis(3600000);
    }
}