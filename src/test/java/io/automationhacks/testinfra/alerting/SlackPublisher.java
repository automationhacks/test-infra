package io.automationhacks.testinfra.alerting;

import io.automationhacks.testinfra.model.TestResult;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SlackPublisher {
    private static final Logger logger = Logger.getLogger(TestListener.class.getName());
    private final SlackClient slackClient = new SlackClient();

    public void publishSlackMessage(
            List<TestResult> failedTestResults,
            int totalTests,
            int passedTests,
            int failedTests,
            int skippedTests) {
        String onCall = failedTestResults.isEmpty() ? "Team" : failedTestResults.get(0).getOnCall();

        String parentThreadTs = null;
        StringBuilder summaryMsg = new StringBuilder();
        summaryMsg.append("*Test Execution Summary*\n");
        summaryMsg.append(
                String.format(
                        "*Total: %d | ✅ Passed: %d | ❌ Failed: %d | ⚠️ Skipped: %d*\n\n",
                        totalTests, passedTests, failedTests, skippedTests));

        var response = slackClient.sendMessage(onCall, summaryMsg.toString());
        if (response.isOk()) {
            parentThreadTs = response.getTs();
            logger.log(Level.FINE, "Parent thread ts: %s".formatted(parentThreadTs));
        }

        if (!failedTestResults.isEmpty()) {

            for (TestResult testResult : failedTestResults) {

                StringBuilder testFailureMsg = new StringBuilder();
                testFailureMsg.append("*🔴 Test Failure alert*\n");
                testFailureMsg
                        .append("*Test Class:* *`")
                        .append(testResult.getTestClass())
                        .append("`*\n");
                testFailureMsg
                        .append("*Test method:* *`")
                        .append(testResult.getName())
                        .append("`*\n");
                testFailureMsg
                        .append("*Error Message:* ```")
                        .append(testResult.getErrorMessage())
                        .append("```\n");
                testFailureMsg
                        .append("*Timestamp:* *`")
                        .append(testResult.getTimestamp())
                        .append("`*\n");
                testFailureMsg
                        .append("*Functional Flow:* *`")
                        .append(testResult.getFunctionalFlow())
                        .append("`*\n");
                testFailureMsg
                        .append("*Service Method:* *`")
                        .append(testResult.getServiceMethod())
                        .append("`*\n");
                testFailureMsg
                        .append("*OnCall:* *`")
                        .append(testResult.getOnCall())
                        .append("`*\n\n");

                var testFailureResponse =
                        slackClient.sendMessageInThread(testFailureMsg.toString(), parentThreadTs);
                if (testFailureResponse.isOk()) {
                    logger.info("Slack notification sent successfully");
                } else {
                    logger.log(Level.WARNING, "Failed to send Slack notification");
                }
            }
        }
    }
}
