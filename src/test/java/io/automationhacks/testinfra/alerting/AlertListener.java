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
    private final SlackNotifier slackNotifier = new SlackNotifier();
    private String parentThreadTs = null;
    private int totalTests = 0;
    private int passedTests = 0;
    private int failedTests = 0;
    private int skippedTests = 0;
    private final List<TestResult> failedTestResults = new ArrayList<>();

    @Override
    public void onTestFailure(ITestResult result) {
        failedTests++;
        failedTestResults.add(createTestResult(result));
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

    private TestResult createTestResult(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Method testMethod = result.getMethod().getConstructorOrMethod().getMethod();

        String onCall = getOnCallPerson(testMethod);
        String functionalFlow = getFlow(testMethod);
        String serviceMethod = getService(testMethod);

        return new TestResult(
                result.getTestClass().getName(),
                testName,
                result.getStatus(),
                result.getThrowable().getMessage(),
                new Date(),
                onCall,
                functionalFlow,
                serviceMethod);
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

@Data
@Builder
@AllArgsConstructor
class TestResult {
    private String testClass;
    private String name;
    private int status;
    private String errorMessage;
    private Date timestamp;
    private String onCall;
    private String functionalFlow;
    private String serviceMethod;
}
