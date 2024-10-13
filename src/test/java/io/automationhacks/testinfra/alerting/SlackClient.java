package io.automationhacks.testinfra.alerting;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;

import io.automationhacks.testinfra.constants.Oncalls;
import io.automationhacks.testinfra.constants.SysProps;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SlackClient {
    private static final Logger logger = Logger.getLogger(SlackClient.class.getName());
    private static final String SLACK_BOT_TOKEN = SysProps.getSlackBotToken();
    private static final String CHANNEL = SysProps.getSlackChannel();

    public ChatPostMessageResponse sendMessage(Oncalls onCall, String message) {
        try {
            logger.info("Sending Slack message to %s with message %s".formatted(onCall, message));
            MethodsClient client = Slack.getInstance().methods(SLACK_BOT_TOKEN);

            var request =
                    ChatPostMessageRequest.builder()
                            .channel(CHANNEL)
                            .blocks(buildMessageBlocks(onCall, message))
                            .build();

            return client.chatPostMessage(request);
        } catch (IOException | SlackApiException e) {
            logger.log(Level.WARNING, "Failed to send Slack message", e.getMessage());
        }
        return null;
    }

    private List<LayoutBlock> buildMessageBlocks(Oncalls onCall, String message) {
        return Arrays.asList(
                SectionBlock.builder()
                        .text(
                                MarkdownTextObject.builder()
                                        .text("*OnCall*: *`<@%s>`*".formatted(onCall.getSlackId()))
                                        .build())
                        .build(),
                SectionBlock.builder()
                        .text(MarkdownTextObject.builder().text(message).build())
                        .build(),
                DividerBlock.builder().build());
    }

    public ChatPostMessageResponse sendMessageInThread(String message, String threadTs) {
        try {
            logger.info(
                    "Sending Slack message in thread %s with message %s"
                            .formatted(threadTs, message));
            MethodsClient client = Slack.getInstance().methods(SLACK_BOT_TOKEN);

            var request =
                    ChatPostMessageRequest.builder()
                            .channel(CHANNEL)
                            .text(message)
                            .threadTs(threadTs)
                            .build();

            return client.chatPostMessage(request);
        } catch (IOException | SlackApiException e) {
            logger.log(Level.WARNING, "Failed to send Slack message in thread", e.getMessage());
        }
        return null;
    }
}
