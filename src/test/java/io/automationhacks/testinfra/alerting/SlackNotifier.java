package io.automationhacks.testinfra.alerting;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;

import io.automationhacks.testinfra.constants.SysProps;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class SlackNotifier {
    private static final Logger logger = Logger.getLogger(SlackNotifier.class.getName());
    private static final String SLACK_BOT_TOKEN = SysProps.getSlackBotToken();
    private static final String CHANNEL = SysProps.getSlackChannel();

    public void sendMessage(String onCall, String message) {
        try {
            logger.info(
                    "Sending Slack message to %s with message %s and token %s"
                            .formatted(onCall, message, SLACK_BOT_TOKEN));
            MethodsClient methods = Slack.getInstance().methods(SLACK_BOT_TOKEN);

            ChatPostMessageRequest request =
                    ChatPostMessageRequest.builder()
                            .channel(CHANNEL)
                            .text("<!here> Test Failure Alert for @" + onCall)
                            .blocks(buildMessageBlocks(onCall, message))
                            .build();

            var response = methods.chatPostMessage(request);
            logger.info("Slack message sent: %s".formatted(response));
        } catch (IOException | SlackApiException e) {
            e.printStackTrace();
        }
    }

    private List<LayoutBlock> buildMessageBlocks(String onCall, String message) {
        // Build Slack message blocks for better formatting
        // This is a simplified version. You can enhance it for better visual representation.
        return Arrays.asList(
                SectionBlock.builder()
                        .text(
                                MarkdownTextObject.builder()
                                        .text("*Test Failure Alert*\nOn-Call: @" + onCall)
                                        .build())
                        .build(),
                DividerBlock.builder().build(),
                SectionBlock.builder()
                        .text(MarkdownTextObject.builder().text(message).build())
                        .build());
    }
}
