package io.automationhacks.testinfra;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AutoTestAnalyzer {
    private static final Logger logger = Logger.getLogger(AutoTestAnalyzer.class.getName());
    private final ReportPortalClient reportPortalClient;
    private final StackTraceAnalyzer stackTraceAnalyzer;

    public AutoTestAnalyzer() {
        this.reportPortalClient = new ReportPortalClient();
        this.stackTraceAnalyzer = new StackTraceAnalyzer();
    }

    /** Analyzes failing tests and provides suggestions. */
    public void analyzeFailingTests() {
        try {
            var failingTests = reportPortalClient.getFailedTests();
            List<String> testItemIds =
                    failingTests.stream().map(ReportPortalClient.FailedTestItem::getId).toList();

            for (String testItemId : testItemIds) {
                try {
                    String stackTrace = reportPortalClient.fetchStackTrace(testItemId);
                    String suggestion = stackTraceAnalyzer.analyze(stackTrace);
                    logAnalysisResult(testItemId, suggestion);
                } catch (IOException e) {
                    logger.warning(
                            "Error analyzing test item " + testItemId + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.severe("Error fetching failing tests: " + e.getMessage());
        }
    }

    private void logAnalysisResult(String testItemId, String suggestion) {
        logger.info(String.format("Test Item ID: %s%nSuggestion: %s%n", testItemId, suggestion));
    }
}
