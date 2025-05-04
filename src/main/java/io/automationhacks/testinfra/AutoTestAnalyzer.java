package io.automationhacks.testinfra;

import java.io.IOException;
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
            logger.info("Fetching failing tests from Report Portal...");
            var failingTests = reportPortalClient.getFailedTests();
            logger.info(
                    "Found "
                            + failingTests.size()
                            + " failing tests. Analyzing stack traces for suggestions...");
            for (var testItem : failingTests) {
                String suggestion = stackTraceAnalyzer.analyze(testItem.getStackTrace());
                logAnalysisResult(testItem.getId(), suggestion);
            }
        } catch (IOException e) {
            logger.severe("Error fetching failing tests: " + e.getMessage());
        }
    }

    private void logAnalysisResult(String testItemId, String suggestion) {
        logger.info(String.format("Test Item ID: %s%nSuggestion: %s%n", testItemId, suggestion));
    }
}
