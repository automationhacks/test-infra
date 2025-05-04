package io.automationhacks.testinfra;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackTraceAnalyzer {
    private static final Logger logger = Logger.getLogger(StackTraceAnalyzer.class.getName());

    private static final Pattern ASSERTION_PATTERN =
            Pattern.compile("(?:expected:<(.+)> but was:<(.+)>|AssertionError: (.+))");
    private static final Pattern HTTP_ERROR_PATTERN = Pattern.compile("HTTP Response: (\\d+).*");
    private static final Pattern TIMEOUT_PATTERN = Pattern.compile(".*TimeoutException: (.+)");

    /**
     * Analyzes a stack trace to identify the root cause of a failure.
     *
     * @param stackTrace The stack trace as a String.
     * @return A suggestion for fixing the issue.
     */
    public String analyze(String stackTrace) {
        if (stackTrace == null || stackTrace.isEmpty()) {
            return "No stack trace provided.";
        }

        logger.fine("Analyzing stack trace: " + stackTrace);

        // Check for common exceptions
        if (stackTrace.contains("NullPointerException")) {
            return analyzeNullPointerException(stackTrace);
        }

        // Check for assertion failures
        Matcher assertionMatcher = ASSERTION_PATTERN.matcher(stackTrace);
        if (assertionMatcher.find()) {
            return analyzeAssertionError(assertionMatcher);
        }

        // Check for HTTP errors
        Matcher httpMatcher = HTTP_ERROR_PATTERN.matcher(stackTrace);
        if (httpMatcher.find()) {
            return analyzeHttpError(httpMatcher.group(1));
        }

        // Check for timeouts
        Matcher timeoutMatcher = TIMEOUT_PATTERN.matcher(stackTrace);
        if (timeoutMatcher.find()) {
            return String.format(
                    "Test timed out: %s. Consider increasing the timeout or checking for performance issues.",
                    timeoutMatcher.group(1));
        }

        // Extract location information for general analysis
        Pattern locationPattern = Pattern.compile("at ([^(]+)\\(([^:]+):(\\d+)\\)");
        Matcher locationMatcher = locationPattern.matcher(stackTrace);
        if (locationMatcher.find()) {
            String method = locationMatcher.group(1);
            String file = locationMatcher.group(2);
            String line = locationMatcher.group(3);
            return String.format(
                    "Issue detected in method %s at %s:%s. Review this location for potential issues.",
                    method.trim(), file, line);
        }

        return "Unable to determine the root cause from the stack trace.";
    }

    private String analyzeNullPointerException(String stackTrace) {
        Pattern variablePattern = Pattern.compile("Cannot (read|invoke|write) \"([^\"]+)\"");
        Matcher matcher = variablePattern.matcher(stackTrace);
        if (matcher.find()) {
            String action = matcher.group(1);
            String variable = matcher.group(2);
            return String.format(
                    "NullPointerException when trying to %s %s. Ensure this value is properly initialized.",
                    action, variable);
        }
        return "NullPointerException detected. Check for null values before accessing objects.";
    }

    private String analyzeAssertionError(Matcher matcher) {
        String expected = matcher.group(1);
        String actual = matcher.group(2);
        String message = matcher.group(3);

        if (expected != null && actual != null) {
            return String.format(
                    "Assertion failed: Expected '%s' but got '%s'. Verify the test data and assertions.",
                    expected, actual);
        } else if (message != null) {
            return String.format("Assertion failed: %s. Review the assertion conditions.", message);
        }

        return "Assertion failed. Review the test conditions and expected values.";
    }

    private String analyzeHttpError(String statusCode) {
        switch (statusCode) {
            case "400":
                return "Bad Request (400). Check the request payload and parameters.";
            case "401":
                return "Unauthorized (401). Verify authentication credentials.";
            case "403":
                return "Forbidden (403). Check authorization and permissions.";
            case "404":
                return "Not Found (404). Verify the endpoint URL and resource existence.";
            case "500":
                return "Internal Server Error (500). Check server logs for details.";
            case "502":
                return "Bad Gateway (502). Check if the backend service is available.";
            case "503":
                return "Service Unavailable (503). The service might be down or overloaded.";
            default:
                return String.format(
                        "HTTP error %s occurred. Check the request and server status.", statusCode);
        }
    }
}
