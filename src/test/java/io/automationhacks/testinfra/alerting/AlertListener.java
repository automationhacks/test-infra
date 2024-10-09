package io.automationhacks.testinfra.alerting;

import io.automationhacks.testinfra.model.TestResult;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlertListener implements ITestListener {
    private static final Logger logger = Logger.getLogger(AlertListener.class.getName());
    private final SlackNotifier slackNotifier = new SlackNotifier();
    private final TestResultManager testResultManager = new TestResultManager();
    private String parentThreadTs = null;
    private int totalTests = 0;
    private int passedTests = 0;
    private int failedTests = 0;
    private int skippedTests = 0;
    private final List<TestResult> failedTestResults = new ArrayList<>();

    @Override
    public void onTestFailure(ITestResult result) {
        failedTests++;
        failedTestResults.add(testResultManager.createTestResult(result));
    }

    @Override
    public void onTestStart(ITestResult result) {}

    @Override
    public void onTestSuccess(ITestResult result) {
        passedTests++;
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skippedTests++;
    }

    @Override
    public void onStart(ITestContext context) {
        totalTests = context.getAllTestMethods().length;
    }

    @Override
    public void onFinish(ITestContext context) {
        sendSummaryNotification();
    }

    private void sendSummaryNotification() {
        sendSlackAlert();
    }

    private void sendSlackAlert() {
        String onCall = failedTestResults.isEmpty() ? "Team" : failedTestResults.get(0).getOnCall();

        if (parentThreadTs == null) {
            StringBuilder message = new StringBuilder();
            message.append("*Test Execution Summary*\n");
            message.append(
                    String.format(
                            "*Total: %d | ✅ Passed: %d | ❌ Failed: %d | ⚠️ Skipped: %d*\n\n",
                            totalTests, passedTests, failedTests, skippedTests));

            var response = slackNotifier.sendMessage(onCall, message.toString());
            if (response.isOk()) {
                parentThreadTs = response.getTs();
                logger.log(Level.FINE, "Parent thread ts: %s".formatted(parentThreadTs));
            }
        }

        if (!failedTestResults.isEmpty()) {

            for (TestResult testResult : failedTestResults) {

                StringBuilder message = new StringBuilder();
                message.append("*🔴 Test Failure alert*\n");
                message.append("*Test Class:* *`").append(testResult.getTestClass()).append("`*\n");
                message.append("*Test method:* *`").append(testResult.getName()).append("`*\n");
                message.append("*Error Message:* ```")
                        .append(testResult.getErrorMessage())
                        .append("```\n");
                message.append("*Timestamp:* *`").append(testResult.getTimestamp()).append("`*\n");
                message.append("*Functional Flow:* *`")
                        .append(testResult.getFunctionalFlow())
                        .append("`*\n");
                message.append("*Service Method:* *`")
                        .append(testResult.getServiceMethod())
                        .append("`*\n");
                message.append("*OnCall:* *`").append(testResult.getOnCall()).append("`*\n\n");

                var response =
                        slackNotifier.sendMessageInThread(
                                onCall, message.toString(), parentThreadTs);
                if (response.isOk()) {
                    logger.info("Slack notification sent successfully");
                } else {
                    logger.log(Level.WARNING, "Failed to send Slack notification");
                }
            }
        }
    }
}
