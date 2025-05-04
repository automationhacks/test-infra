package io.automationhacks.testinfra;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StackTraceAnalyzerTest {
    private StackTraceAnalyzer analyzer;

    @BeforeClass
    public void setup() {
        analyzer = new StackTraceAnalyzer();
    }

    @Test
    public void testAnalyzeNullPointerException() {
        String stackTrace = """
            java.lang.NullPointerException: Cannot invoke "String.length()" because "str" is null
                at com.example.MyClass.method(MyClass.java:42)
                at com.example.Test.test(Test.java:23)
            """;
        
        String suggestion = analyzer.analyze(stackTrace);
        assertEquals(suggestion, 
            "NullPointerException when trying to invoke String.length(). Ensure this value is properly initialized.");
    }

    @Test
    public void testAnalyzeAssertionError() {
        String stackTrace = """
            java.lang.AssertionError: expected:<200> but was:<201>
                at com.example.MyTest.testEndpoint(MyTest.java:15)
            """;
        
        String suggestion = analyzer.analyze(stackTrace);
        assertEquals(suggestion,
            "Assertion failed: Expected '200' but got '201'. Verify the test data and assertions.");
    }

    @Test
    public void testAnalyzeHttpError() {
        String stackTrace = """
            io.restassured.internal.ResponseSpecificationImpl$HamcrestAssertionClosure.validate
            HTTP Response: 401 Unauthorized
                at com.example.MyTest.testAuth(MyTest.java:25)
            """;
        
        String suggestion = analyzer.analyze(stackTrace);
        assertEquals(suggestion, "Unauthorized (401). Verify authentication credentials.");
    }

    @Test
    public void testAnalyzeTimeout() {
        String stackTrace = """
            java.util.concurrent.TimeoutException: Request timed out after 30 seconds
                at com.example.MyTest.testEndpoint(MyTest.java:35)
            """;
        
        String suggestion = analyzer.analyze(stackTrace);
        assertTrue(suggestion.contains("Request timed out after 30 seconds"),
            "Suggestion should mention the timeout duration");
    }

    @Test
    public void testAnalyzeMultipleAssertions() {
        String stackTrace = """
            java.lang.AssertionError: Multiple Failures (2 failures)
            \tjava.lang.AssertionError: expected:<200> but was:<400>
            \tjava.lang.AssertionError: Response body doesn't match expectation. 
            Expected: {id=123}
            Actual: {error="Bad Request"}
                at com.example.MyTest.validateResponse(MyTest.java:42)
            """;
        
        String suggestion = analyzer.analyze(stackTrace);
        assertTrue(suggestion.contains("400"), 
            "Suggestion should mention the HTTP status code");
        assertTrue(suggestion.contains("Response body"), 
            "Suggestion should mention the response body mismatch");
    }

    @Test
    public void testAnalyzeComplexFailure() {
        // Only run this test if OpenAI API key is configured
        if (System.getenv("OPENAI_API_KEY") == null) {
            return;
        }

        String stackTrace = """
            org.opentest4j.AssertionFailedError: 
            Multiple Failures (2 failures)
                org.junit.jupiter.api.Assertions.assertNotNull(String, Object)
                expected: <not null> but was: <null>
                at com.example.ComplexTest.validateResponse(ComplexTest.java:42)
            
            org.junit.jupiter.api.Assertions.assertTrue(String, boolean)
                Data validation failed: Invalid format
                at com.example.ComplexTest.validateFormat(ComplexTest.java:45)
            """;
        
        String suggestion = analyzer.analyze(stackTrace);
        assertTrue(suggestion.length() > 0, "Should get a non-empty suggestion from LLM");
    }

    @Test
    public void testAnalyzeXMLTestReport() {
        String stackTrace = """
            <failure type="java.lang.AssertionError">
                <message>Expected status code &lt;200&gt; but was &lt;201&gt;</message>
                <full-stacktrace>
                    at org.example.CreateUserTest.testCreate(CreateUserTest.java:36)
                </full-stacktrace>
            </failure>
            """;

        String suggestion = analyzer.analyze(stackTrace);
        assertTrue(suggestion.contains("201"), 
            "Suggestion should mention the actual status code");
    }

    @Test
    public void testAnalyzeReportPortalFailure() {
        String stackTrace = """
            {
                "status": "FAILED",
                "issue": {
                    "issueType": "PB001",
                    "comment": "Product Bug"
                },
                "name": "testCreate",
                "stackTrace": "java.lang.AssertionError: Expected status code <200> but was <201>"
            }
            """;

        String suggestion = analyzer.analyze(stackTrace);
        assertTrue(suggestion.contains("200") && suggestion.contains("201"), 
            "Suggestion should mention both expected and actual status codes");
        assertTrue(suggestion.contains("Product Bug"), 
            "Suggestion should mention the issue type from ReportPortal");
    }
}