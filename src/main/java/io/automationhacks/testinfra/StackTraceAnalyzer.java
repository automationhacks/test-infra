package io.automationhacks.testinfra;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class StackTraceAnalyzer {
    private static final Logger logger = Logger.getLogger(StackTraceAnalyzer.class.getName());

    private static final Pattern ASSERTION_PATTERN =
            Pattern.compile("expected:<?([^>]+)>? but was:<?([^>]+)>?");
    private static final Pattern HTTP_ERROR_PATTERN = Pattern.compile("HTTP Response: (\\d+).*");
    private static final Pattern TIMEOUT_PATTERN = Pattern.compile(".*TimeoutException: (.+)");
    private static final Pattern XML_ASSERTION_PATTERN =
            Pattern.compile("<message>Expected status code &lt;(\\d+)&gt; but was &lt;(\\d+)&gt;</message>");
    private static final Pattern MULTIPLE_FAILURES_PATTERN =
            Pattern.compile("Multiple Failures \\((\\d+) failures\\)");
    
    private final String openAIApiKey;
    private final HttpClient httpClient;

    public StackTraceAnalyzer() {
        this.openAIApiKey = System.getenv("OPENAI_API_KEY");
        this.httpClient = HttpClient.newHttpClient();
    }

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

        // Try specialized format analyzers first
        if (stackTrace.startsWith("{")) {
            try {
                return analyzeReportPortalJson(stackTrace);
            } catch (Exception e) {
                logger.fine("Not a valid ReportPortal JSON: " + e.getMessage());
            }
        }

        if (stackTrace.startsWith("<")) {
            try {
                return analyzeXmlReport(stackTrace);
            } catch (Exception e) {
                logger.fine("Not a valid XML report: " + e.getMessage());
            }
        }

        // Check for multiple failures
        Matcher multipleFailuresMatcher = MULTIPLE_FAILURES_PATTERN.matcher(stackTrace);
        if (multipleFailuresMatcher.find()) {
            return analyzeMultipleFailures(stackTrace, Integer.parseInt(multipleFailuresMatcher.group(1)));
        }

        // Try pattern-based analysis
        String patternBasedSuggestion = analyzeWithPatterns(stackTrace);
        if (!patternBasedSuggestion.equals("Unable to determine the root cause from the stack trace.")) {
            return patternBasedSuggestion;
        }

        // Fall back to LLM analysis for complex cases
        try {
            return analyzeWithLLM(stackTrace);
        } catch (IOException e) {
            logger.warning("Failed to get LLM analysis: " + e.getMessage());
            return patternBasedSuggestion;
        }
    }

    private String analyzeWithPatterns(String stackTrace) {
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

    private String analyzeWithLLM(String stackTrace) throws IOException {
        if (openAIApiKey == null || openAIApiKey.isEmpty()) {
            throw new IOException("OpenAI API key not configured");
        }

        String prompt = String.format(
                "Analyze this test failure stack trace and provide a concise, specific suggestion for fixing the issue:\n\n%s",
                stackTrace);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openAIApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(new JSONObject()
                        .put("model", "gpt-3.5-turbo")
                        .put("messages", new JSONObject[]{ new JSONObject()
                                .put("role", "user")
                                .put("content", prompt)
                        })
                        .put("temperature", 0.7)
                        .put("max_tokens", 150)
                        .toString()))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("OpenAI API error: " + response.body());
            }

            JSONObject jsonResponse = new JSONObject(response.body());
            return jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("LLM analysis interrupted", e);
        }
    }

    private String analyzeMultipleFailures(String stackTrace, int failureCount) {
        StringBuilder suggestion = new StringBuilder();
        suggestion.append(String.format("Found %d failures:\n", failureCount));
        
        String[] lines = stackTrace.split("\n");
        int failuresFound = 0;
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            
            // Check for assertion errors
            Matcher assertionMatcher = ASSERTION_PATTERN.matcher(line);
            if (assertionMatcher.find()) {
                failuresFound++;
                suggestion.append(String.format("%d. ", failuresFound));
                suggestion.append(analyzeAssertionError(assertionMatcher));
                suggestion.append("\n");
                continue;
            }
            
            // Check for response body mismatches
            if (line.contains("Response body")) {
                failuresFound++;
                suggestion.append(String.format("%d. ", failuresFound));
                
                // Try to extract expected and actual values from following lines
                StringBuilder bodyMismatch = new StringBuilder("Response body mismatch. ");
                if (i + 2 < lines.length) {
                    String expected = lines[i + 1].replaceAll("Expected:\\s*", "").trim();
                    String actual = lines[i + 2].replaceAll("Actual:\\s*", "").trim();
                    bodyMismatch.append(String.format("Expected %s but got %s", expected, actual));
                }
                
                suggestion.append(bodyMismatch.toString());
                suggestion.append("\n");
            }
        }
        
        if (failuresFound > 0) {
            return suggestion.toString().trim();
        }
        
        return String.format("Multiple failures detected (%d). Review each failure in the stack trace.", failureCount);
    }

    private String analyzeXmlReport(String stackTrace) {
        Matcher xmlMatcher = XML_ASSERTION_PATTERN.matcher(stackTrace);
        if (xmlMatcher.find()) {
            return String.format(
                    "Assertion failed: Expected status code '%s' but got '%s'. Verify the test data and assertions.",
                    xmlMatcher.group(1), xmlMatcher.group(2));
        }
        return "Unable to parse XML test report.";
    }

    private String analyzeReportPortalJson(String stackTrace) {
        JSONObject json = new JSONObject(stackTrace);
        String status = json.getString("status");
        JSONObject issue = json.optJSONObject("issue");
        String actualStackTrace = json.optString("stackTrace", "");

        StringBuilder suggestion = new StringBuilder();
        suggestion.append(String.format("Test '%s' %s. ", json.getString("name"), status.toLowerCase()));

        if (issue != null) {
            suggestion.append(String.format("Issue type: %s (%s). ", 
                    issue.getString("issueType"), 
                    issue.optString("comment", "Unknown")));
        }

        if (!actualStackTrace.isEmpty()) {
            // Try to extract assertion details from the stack trace
            Matcher assertionMatcher = ASSERTION_PATTERN.matcher(actualStackTrace);
            if (assertionMatcher.find()) {
                suggestion.append(analyzeAssertionError(assertionMatcher));
            } else {
                String detailedAnalysis = analyzeWithPatterns(actualStackTrace);
                if (!detailedAnalysis.equals("Unable to determine the root cause from the stack trace.")) {
                    suggestion.append(detailedAnalysis);
                }
            }
        }

        return suggestion.toString().trim();
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

        // Clean up the values by removing []
        expected = expected.replaceAll("[\\[\\]]", "");
        actual = actual.replaceAll("[\\[\\]]", "");

        return String.format(
                "Assertion failed: Expected '%s' but got '%s'. Verify the test data and assertions.",
                expected, actual);
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
