package com.tf2center.discordbot.parser.discord.listeners.commands;

import com.tf2center.discordbot.parser.discord.notifications.csv.CSVActions;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class NotificationsOnCommand implements SlashCommand {

    private static final Logger logger = LoggerFactory.getLogger(NotificationsOnCommand.class);

    private GatewayDiscordClient gatewayDiscordClient;

    @Autowired
    public NotificationsOnCommand(GatewayDiscordClient gatewayDiscordClient) {
        this.gatewayDiscordClient = gatewayDiscordClient;
    }

    @Override
    public String getName() {
        return "notifications_on";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        logger.info("Person trying to subscribe: " + event.getInteraction().getUser().getGlobalName().get());
        CSVActions.appendToSubscribers(
            event.getInteraction().getUser().getId(),
            event.getInteraction().getChannelId()
        );
        logger.info("Person successfully subbed");
        return event.reply()
            .withContent("Notifications are ON.")
            .withEphemeral(false);
    }
}
