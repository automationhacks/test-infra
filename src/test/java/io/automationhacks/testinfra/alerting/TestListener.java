package io.automationhacks.testinfra.alerting;

import io.automationhacks.testinfra.model.TestResult;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.*;

public class TestListener implements ITestListener {
    private final TestResultManager testResultManager = new TestResultManager();
    private final List<TestResult> failedTestResults = new ArrayList<>();
    private int totalTests = 0;
    private int passedTests = 0;
    private int failedTests = 0;
    private int skippedTests = 0;

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
        new SlackPublisher()
                .publishSlackMessage(
                        failedTestResults, totalTests, passedTests, failedTests, skippedTests);
    }
}
