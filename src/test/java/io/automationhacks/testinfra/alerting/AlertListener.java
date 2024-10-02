package io.automationhacks.testinfra.alerting;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlertListener implements ITestListener {
    private static final Logger logger = Logger.getLogger(AlertListener.class.getName());
    private static final int HISTORY_SIZE = 5;
    private final Map<String, Deque<TestResult>> testHistory = new HashMap<>();
    private final SlackNotifier slackNotifier = new SlackNotifier();
    private String parentThreadTs = null;

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Method testMethod = result.getMethod().getConstructorOrMethod().getMethod();

        String onCall = getOnCallPerson(testMethod);
        String functionalFlow = getFlow(testMethod);
        String serviceMethod = getService(testMethod);

        TestResult testResult =
                new TestResult(
                        testName,
                        result.getStatus(),
                        result.getThrowable().getMessage(),
                        new Date(),
                        onCall,
                        functionalFlow,
                        serviceMethod);

        updateTestHistory(testName, testResult);
        sendSlackAlert(testResult);
    }

    private void updateTestHistory(String testName, TestResult testResult) {
        testHistory.computeIfAbsent(testName, k -> new LinkedList<>());
        Deque<TestResult> history = testHistory.get(testName);

        history.addFirst(testResult);
        if (history.size() > HISTORY_SIZE) {
            history.removeLast();
        }
    }

    private void sendSlackAlert(TestResult testResult) {
        if (parentThreadTs == null) {
            String summaryMessage = createSummaryMessage();
            var response = slackNotifier.sendMessage(testResult.getOnCall(), summaryMessage);
            if (response.isOk()) {
                parentThreadTs = response.getTs();
                logger.log(Level.FINE, "Parent thread ts: %s".formatted(parentThreadTs));
            }
        }

        StringBuilder message = new StringBuilder();

        message.append("Test Failure Alert\n");
        message.append("Test Name: ").append(testResult.getName()).append("\n");
        message.append("Status: FAILED\n");
        message.append("Error Message: ").append(testResult.getErrorMessage()).append("\n");
        message.append("Timestamp: ").append(testResult.getTimestamp()).append("\n");
        message.append("Functional Flow: ").append(testResult.getFunctionalFlow()).append("\n");
        message.append("Service Method: ").append(testResult.getServiceMethod()).append("\n");
        message.append("OnCall: ").append(testResult.getOnCall()).append("\n\n");

        message.append("Test History (Last 5 runs):\n");
        Deque<TestResult> history = testHistory.get(testResult.getName());

        for (TestResult historicalResult : history) {
            message.append("- ")
                    .append(historicalResult.getTimestamp())
                    .append(": ")
                    .append(getStatusString(historicalResult.getStatus()))
                    .append("\n");
        }

        var response =
                slackNotifier.sendMessageInThread(
                        testResult.getOnCall(), message.toString(), parentThreadTs);
        if (response.isOk()) {
            logger.info("Slack message sent successfully in thread");
        }
    }

    private String createSummaryMessage() {
        int totalTests = testHistory.values().stream().mapToInt(Deque::size).sum();
        long passedTests =
                testHistory.values().stream()
                        .flatMap(Collection::stream)
                        .filter(result -> result.getStatus() == ITestResult.SUCCESS)
                        .count();
        long failedTests =
                testHistory.values().stream()
                        .flatMap(Collection::stream)
                        .filter(result -> result.getStatus() == ITestResult.FAILURE)
                        .count();
        long skippedTests =
                testHistory.values().stream()
                        .flatMap(Collection::stream)
                        .filter(result -> result.getStatus() == ITestResult.SKIP)
                        .count();

        return String.format(
                "Test Execution Summary:\nTotal: %d\nPassed: %d\nFailed: %d\nSkipped: %d",
                totalTests, passedTests, failedTests, skippedTests);
    }

    private String getOnCallPerson(Method testMethod) {
        OnCall onCallAnnotation = testMethod.getAnnotation(OnCall.class);
        if (onCallAnnotation == null) {
            onCallAnnotation = testMethod.getDeclaringClass().getAnnotation(OnCall.class);
        }
        return onCallAnnotation != null ? onCallAnnotation.value() : "Unknown";
    }

    private String getFlow(Method testMethod) {
        Flow flowAnnotation = testMethod.getAnnotation(Flow.class);

        if (flowAnnotation == null) {
            flowAnnotation = testMethod.getDeclaringClass().getAnnotation(Flow.class);
        }
        return flowAnnotation != null ? flowAnnotation.value() : "Unknown";
    }

    private String getService(Method testMethod) {
        Service serviceMethodAnnotation = testMethod.getAnnotation(Service.class);
        return serviceMethodAnnotation != null ? serviceMethodAnnotation.value() : "Unknown";
    }

    private String getStatusString(int status) {
        return switch (status) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP -> "SKIPPED";
            default -> "UNKNOWN";
        };
    }

    @Override
    public void onTestStart(ITestResult result) {}

    @Override
    public void onTestSuccess(ITestResult result) {}

    @Override
    public void onTestSkipped(ITestResult result) {}

    @Override
    public void onStart(ITestContext context) {}

    @Override
    public void onFinish(ITestContext context) {}
}

@Data
@Builder
@AllArgsConstructor
class TestResult {
    private String name;
    private int status;
    private String errorMessage;
    private Date timestamp;
    private String onCall;
    private String functionalFlow;
    private String serviceMethod;
}
